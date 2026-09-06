package hr.algebra.concertcrew.ssrf;

import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SsrfGuardTest {

    @Test
    void allowsPublicAddress() {
        URI uri = SsrfGuard.validate("http://8.8.8.8/path");
        assertEquals("8.8.8.8", uri.getHost());
    }

    @Test
    void blocksLoopback() {
        assertThrows(SsrfValidationException.class,
            () -> SsrfGuard.validate("http://127.0.0.1/"));
    }

    @Test
    void blocksLinkLocalMetadataAddress() {
        assertThrows(SsrfValidationException.class,
            () -> SsrfGuard.validate("http://169.254.169.254/latest/meta-data/"));
    }

    @Test
    void blocksPrivateSiteLocalAddress() {
        assertThrows(SsrfValidationException.class,
            () -> SsrfGuard.validate("http://10.0.0.5/"));
    }

    @Test
    void rejectsNonHttpScheme() {
        assertThrows(SsrfValidationException.class,
            () -> SsrfGuard.validate("file:///etc/passwd"));
    }

    @Test
    void rejectsUrlWithoutHost() {
        assertThrows(SsrfValidationException.class,
            () -> SsrfGuard.validate("http://"));
    }
}
