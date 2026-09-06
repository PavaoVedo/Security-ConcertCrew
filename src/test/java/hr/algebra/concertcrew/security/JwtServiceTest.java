package hr.algebra.concertcrew.security;

import hr.algebra.concertcrew.entity.User;
import hr.algebra.concertcrew.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;


class JwtServiceTest {

    private JwtService jwtService;
    private User user;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret",
                "test-secret-key-that-is-long-enough-for-hmac256-algorithm");
        ReflectionTestUtils.setField(jwtService, "accessExpiryMs", 900_000L);

        user = new User();
        user.setUsername("alice");
        user.setRole(Role.USER);
        user.setEnabled(true);
    }

    @Test
    void generatesTokenWithCorrectSubject() {
        String token = jwtService.generateAccessToken(user);
        assertEquals("alice", jwtService.extractUsername(token));
    }

    @Test
    void validTokenPassesValidation() {
        String token = jwtService.generateAccessToken(user);
        assertTrue(jwtService.isValid(token, user));
    }

    @Test
    void expiredTokenFailsValidation() {
        ReflectionTestUtils.setField(jwtService, "accessExpiryMs", -1_000L);
        String token = jwtService.generateAccessToken(user);
        assertFalse(jwtService.isValid(token, user));
    }

    @Test
    void tamperedTokenIsRejected() {
        String token = jwtService.generateAccessToken(user);
        String tampered = token.substring(0, token.length() - 2) + "xx";
        assertNull(jwtService.extractUsername(tampered));
    }
}