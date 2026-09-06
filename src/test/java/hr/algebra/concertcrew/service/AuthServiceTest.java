package hr.algebra.concertcrew.service;

import hr.algebra.concertcrew.dto.Dto;
import hr.algebra.concertcrew.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    AuthService authService;

    @Test
    void registerSavesNewUserWithHashedPassword() {
        Dto.RegisterRequest req = new Dto.RegisterRequest("newuser", "new@example.com", "secret1");
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secret1")).thenReturn("hashed-value");

        authService.register(req);

        verify(userRepository).save(argThat(u ->
            u.getUsername().equals("newuser")
                && u.getEmail().equals("new@example.com")
                && u.getPassword().equals("hashed-value")));
    }

    @Test
    void registerRejectsDuplicateUsername() {
        Dto.RegisterRequest req = new Dto.RegisterRequest("taken", "a@example.com", "secret1");
        when(userRepository.existsByUsername("taken")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(req));
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerRejectsDuplicateEmail() {
        Dto.RegisterRequest req = new Dto.RegisterRequest("fresh", "dupe@example.com", "secret1");
        when(userRepository.existsByUsername("fresh")).thenReturn(false);
        when(userRepository.existsByEmail("dupe@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(req));
        verify(userRepository, never()).save(any());
    }
}
