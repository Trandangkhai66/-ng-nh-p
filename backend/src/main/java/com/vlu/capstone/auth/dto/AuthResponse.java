package com.vlu.capstone.auth.dto;

import com.vlu.capstone.user.dto.UserResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String refreshToken;
    private UserResponse user;
}
