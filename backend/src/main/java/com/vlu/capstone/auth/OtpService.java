package com.vlu.capstone.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final StringRedisTemplate redisTemplate;

    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(1000000));
        redisTemplate.opsForValue().set("OTP:" + email, otp, 5, TimeUnit.MINUTES);
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        String cachedOtp = redisTemplate.opsForValue().get("OTP:" + email);
        return otp != null && otp.equals(cachedOtp);
    }

    public void clearOtp(String email) {
        redisTemplate.delete("OTP:" + email);
    }
}