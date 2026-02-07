package com.vlu.capstone.user;

import com.vlu.capstone.common.exception.ResourceNotFoundException;
import com.vlu.capstone.user.dto.UserResponse;
import com.vlu.capstone.user.dto.UserUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserById_WhenExists_ReturnsUser() {
        User u = User.builder().id(1L).email("a@b.com").username("u").role("USER").provider(User.AuthProvider.LOCAL).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));

        UserResponse r = userService.getUserById(1L);

        assertThat(r.getEmail()).isEqualTo("a@b.com");
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_WhenNotExists_Throws() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateUser_WhenExists_Updates() {
        User u = User.builder().id(1L).email("a@b.com").username("old").provider(User.AuthProvider.LOCAL).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        UserUpdateRequest req = new UserUpdateRequest();
        req.setUsername("new");
        UserResponse r = userService.updateUser(1L, req);

        assertThat(r.getUsername()).isEqualTo("new");
        verify(userRepository).save(u);
    }
}
