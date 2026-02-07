package com.vlu.capstone.auth;

import com.vlu.capstone.auth.dto.AuthResponse;
import com.vlu.capstone.auth.dto.ResetPasswordRequest;
import com.vlu.capstone.common.security.JwtUtil;
import com.vlu.capstone.user.User;
import com.vlu.capstone.user.UserRepository;
import com.vlu.capstone.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.UUID;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final com.vlu.capstone.user.UserService userService;
    private final OtpService otpService;
    private final StringRedisTemplate redisTemplate;
    private final PasswordEncoder passwordEncoder; // 🔴 Thêm dòng này để hết lỗi ảnh 3923ba


    // --- GIỮ NGUYÊN CODE CỦA TRƯỞNG NHÓM ---
    public AuthResponse processOAuth2AndCreateToken(OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String sub = oauth2User.getAttribute("sub");
        if (email == null || sub == null) throw new IllegalArgumentException("Missing email or sub");
        
        User user = userService.createOrUpdateOAuthUser(email, name != null ? name : email, sub);
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return AuthResponse.builder()
                .token(token)
                .user(UserResponse.from(user))
                .build();
    }

    // --- VIẾT THÊM: QUÊN MẬT KHẨU ---
    public void initiateForgotPassword(String email) {
        userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email không tồn tại"));
        String otp = otpService.generateOtp(email);
        log.info("Mã OTP quên mật khẩu gửi tới {}: {}", email, otp);
    }
    public void resetPassword(ResetPasswordRequest request) {
    // 1. Kiểm tra OTP trong Redis
    String savedOtp = redisTemplate.opsForValue().get("OTP:" + request.getEmail());
    if (savedOtp == null || !savedOtp.equals(request.getOtp())) {
        throw new RuntimeException("Mã OTP không chính xác hoặc đã hết hạn");
    }

    // 2. Tìm user và cập nhật mật khẩu đã mã hóa
    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);

    // 3. Xóa OTP sau khi đổi mật khẩu thành công
    redisTemplate.delete("OTP:" + request.getEmail());
}
    

 // ... Giữ nguyên các hàm processOAuth2AndCreateToken và initiateForgotPassword ...

    // --- CẬP NHẬT HÀM verifyOtpAndCreateFullTokens ---
    public AuthResponse verifyOtpAndCreateFullTokens(String email, String otp) {
        if (!otpService.verifyOtp(email, otp)) throw new RuntimeException("OTP không hợp lệ");
        
        User user = userRepository.findByEmail(email).orElseThrow();
        String accessToken = jwtUtil.generateToken(user.getEmail(), user.getRole());
        
        // 🔴 THÊM LOGIC LƯU REFRESH TOKEN VÀO REDIS
        String refreshToken = UUID.randomUUID().toString();
        // Lưu với Key là "RT:email", thời gian sống 7 ngày (giống Docker config của bạn)
        redisTemplate.opsForValue().set("RT:" + email, refreshToken, 7, TimeUnit.DAYS);
        
        otpService.clearOtp(email);
        return AuthResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .user(UserResponse.from(user))
                .build();
    }

    // --- 🔴 VIẾT THÊM: HÀM ĐỔI REFRESH TOKEN LẤY ACCESS TOKEN MỚI ---
    public AuthResponse refreshAccessToken(String email, String refreshToken) {
        // 1. Lấy token đã lưu trong Redis ra so sánh
        String savedToken = redisTemplate.opsForValue().get("RT:" + email);
        
        if (savedToken == null || !savedToken.equals(refreshToken)) {
            throw new RuntimeException("Refresh Token không hợp lệ hoặc đã hết hạn");
        }

        // 2. Nếu khớp, cấp Access Token mới
        User user = userRepository.findByEmail(email).orElseThrow();
        String newAccessToken = jwtUtil.generateToken(user.getEmail(), user.getRole());

        return AuthResponse.builder()
                .token(newAccessToken)
                .refreshToken(refreshToken) // Trả lại chính nó hoặc tạo mới tùy ý
                .user(UserResponse.from(user))
                .build();
    }
}