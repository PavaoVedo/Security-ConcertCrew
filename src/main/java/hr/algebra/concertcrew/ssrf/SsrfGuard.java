package hr.algebra.concertcrew.ssrf;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Set;

public final class SsrfGuard {

    private static final Set<String> ALLOWED_SCHEMES = Set.of("http", "https");

    private SsrfGuard() {
    }

    public static URI validate(String rawUrl) {
        URI uri;
        try {
            uri = URI.create(rawUrl.trim());
        } catch (IllegalArgumentException e) {
            throw new SsrfValidationException("Malformed URL");
        }

        String scheme = uri.getScheme();
        if (scheme == null || !ALLOWED_SCHEMES.contains(scheme.toLowerCase())) {
            throw new SsrfValidationException("Only http and https URLs are allowed");
        }

        String host = uri.getHost();
        if (host == null || host.isBlank()) {
            throw new SsrfValidationException("URL host is missing");
        }

        InetAddress[] addresses;
        try {
            addresses = InetAddress.getAllByName(host);
        } catch (UnknownHostException e) {
            throw new SsrfValidationException("Host could not be resolved");
        }

        for (InetAddress address : addresses) {
            if (isBlocked(address)) {
                throw new SsrfValidationException(
                    "Access to internal/private addresses is blocked: " + address.getHostAddress());
            }
        }
        return uri;
    }

    private static boolean isBlocked(InetAddress address) {
        return address.isAnyLocalAddress()
            || address.isLoopbackAddress()
            || address.isLinkLocalAddress()
            || address.isSiteLocalAddress()
            || address.isMulticastAddress();
    }
}
