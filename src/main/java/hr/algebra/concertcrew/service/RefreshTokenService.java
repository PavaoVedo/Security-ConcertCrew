package hr.algebra.concertcrew.service;

import hr.algebra.concertcrew.entity.RefreshToken;
import hr.algebra.concertcrew.entity.User;
import hr.algebra.concertcrew.repository.RefreshTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Service
public class RefreshTokenService {

    private static final Logger log = LoggerFactory.getLogger(RefreshTokenService.class);
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Value("${app.jwt.refresh-expiry-ms}")
    private long refreshExpiryMs;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public RefreshToken createForLogin(User user) {
        refreshTokenRepository.deleteByUser(user);
        return persistNew(user);
    }

    @Transactional
    public Optional<RefreshToken> rotate(String presentedToken) {
        RefreshToken existing = refreshTokenRepository.findByToken(presentedToken).orElse(null);

        if (existing == null) {
            return Optional.empty();
        }

        if (existing.isRevoked()) {
            log.warn("Refresh token reuse detected for user '{}' — invalidating token family.",
                    existing.getUser().getUsername());
            refreshTokenRepository.deleteByUser(existing.getUser());
            return Optional.empty();
        }

        if (existing.isExpired()) {
            return Optional.empty();
        }

        existing.setRevoked(true);
        refreshTokenRepository.save(existing);
        return Optional.of(persistNew(existing.getUser()));
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public boolean isValid(RefreshToken token) {
        return !token.isRevoked() && !token.isExpired();
    }

    @Transactional
    public void revokeByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    private RefreshToken persistNew(User user) {
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(generateSecureToken());
        token.setExpiryDate(Instant.now().plusMillis(refreshExpiryMs));
        token.setRevoked(false);
        return refreshTokenRepository.save(token);
    }

    private String generateSecureToken() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}