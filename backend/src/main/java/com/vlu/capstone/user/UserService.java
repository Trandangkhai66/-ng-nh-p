package com.vlu.capstone.user;

import com.vlu.capstone.common.exception.ErrorCode;
import com.vlu.capstone.common.exception.ResourceNotFoundException;
import com.vlu.capstone.common.exception.ValidationException;
import com.vlu.capstone.user.dto.UserResponse;
import com.vlu.capstone.user.dto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_001));
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_001));
        user.setUsername(dto.getUsername());
        return UserResponse.from(userRepository.save(user));
    }

    public User createOrUpdateOAuthUser(String email, String name, String providerId) {
        return userRepository.findByProviderAndProviderId(User.AuthProvider.GOOGLE, providerId)
                .map(u -> {
                    u.setEmail(email);
                    u.setUsername(name);
                    return userRepository.save(u);
                })
                .orElseGet(() -> userRepository.findByEmail(email)
                        .map(u -> {
                            u.setProvider(User.AuthProvider.GOOGLE);
                            u.setProviderId(providerId);
                            u.setUsername(name);
                            return userRepository.save(u);
                        })
                        .orElseGet(() -> {
                            User newUser = User.builder()
                                    .email(email)
                                    .username(name)
                                    .provider(User.AuthProvider.GOOGLE)
                                    .providerId(providerId)
                                    .role("USER")
                                    .build();
                                    if (email.equals("trandangkhai66@gmail.com")) {
                                         newUser.setRole("SUPER_ADMIN"); //
                                    } else if (email.equals("bilongdaica12@gmail.com")) {
                                        newUser.setRole("ADMIN"); //
            }                            else {
                                        newUser.setRole("USER");
            }
                            return userRepository.save(newUser);
                        }));
    }
}
