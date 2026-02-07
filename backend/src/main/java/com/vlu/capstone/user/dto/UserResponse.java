package com.vlu.capstone.user.dto;

import com.vlu.capstone.user.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String username;
    private String role;
    private User.AuthProvider provider;
    private LocalDateTime createdAt;

    public static UserResponse from(User u) {
        return UserResponse.builder()
                .id(u.getId())
                .email(u.getEmail())
                .username(u.getUsername())
                .role(u.getRole())
                .provider(u.getProvider())
                .createdAt(u.getCreatedAt())
                .build();
    }
}
