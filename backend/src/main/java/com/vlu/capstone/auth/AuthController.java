    package com.vlu.capstone.auth;

    import com.vlu.capstone.auth.dto.AuthResponse;
    import com.vlu.capstone.auth.dto.ResetPasswordRequest;
    import com.vlu.capstone.common.dto.ApiResponse;
    import com.vlu.capstone.common.security.JwtUtil;
    import com.vlu.capstone.user.UserRepository;
    import com.vlu.capstone.user.dto.UserResponse;
    import io.swagger.v3.oas.annotations.Operation;
    import org.springframework.web.bind.annotation.RequestBody;
    import io.swagger.v3.oas.annotations.tags.Tag;
    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.annotation.AuthenticationPrincipal;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.bind.annotation.RestController;
    import java.util.Collections;
    import java.util.Map;
    import lombok.extern.slf4j.Slf4j;
    @Slf4j
    @RestController
    @RequestMapping("/api/auth")
    @RequiredArgsConstructor
    @Tag(name = "Auth", description = "Authentication APIs")
    public class AuthController {

        private final UserRepository userRepository;
        private final JwtUtil jwtUtil;
        private final AuthService authService; 
        // Thêm injection này

        // --- CODE CŨ CỦA TRƯỞNG NHÓM (GIỮ NGUYÊN) ---
        @GetMapping("/me")
        @Operation(summary = "Get current user from JWT")
        public ResponseEntity<ApiResponse<UserResponse>> me(@AuthenticationPrincipal String email) {
            if (email == null) return ResponseEntity.ok(ApiResponse.success(null));
            return userRepository.findByEmail(email)
                    .map(u -> ResponseEntity.ok(ApiResponse.success(UserResponse.from(u))))
                    .orElse(ResponseEntity.ok(ApiResponse.success(null)));
        }

        // --- CODE MỚI THÊM VÀO ---
    // --- VIẾT THÊM CHO FRONTEND ---
        @PostMapping("/forgot-password")
        public ResponseEntity<ApiResponse<String>> forgot(@RequestParam String email) {
            authService.initiateForgotPassword(email);
            return ResponseEntity.ok(ApiResponse.success("OTP đã được gửi"));
        }

        @PostMapping("/verify-login-otp")
        public ResponseEntity<ApiResponse<AuthResponse>> verify(@RequestParam String email, @RequestParam String otp) {
            AuthResponse res = authService.verifyOtpAndCreateFullTokens(email, otp);
            return ResponseEntity.ok(ApiResponse.success(res));
        }
        @PostMapping("/refresh")
        @Operation(summary = "Lấy Access Token mới bằng Refresh Token")
        public ResponseEntity<ApiResponse<AuthResponse>> refresh(
                @RequestParam String email, 
                @RequestParam String refreshToken) {
            
            AuthResponse res = authService.refreshAccessToken(email, refreshToken);
            return ResponseEntity.ok(ApiResponse.success(res));
        }
    @PostMapping("/reset-password")
        public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        // Thêm dòng log này để kiểm tra xem dữ liệu đã vào đến Controller chưa
        log.info("Nhận yêu cầu reset password cho email: {}", request.getEmail()); 
        authService.resetPassword(request);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Đổi mật khẩu thành công"
            ));
    }
    }