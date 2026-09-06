package hr.algebra.concertcrew.controller.rest;

import hr.algebra.concertcrew.ssrf.SsrfGuard;
import hr.algebra.concertcrew.ssrf.SsrfValidationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/tools")
@Tag(name = "Tools", description = "Server-side URL fetch (SSRF-protected)")
@SecurityRequirement(name = "bearerAuth")
public class UrlFetchController {

    private final HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(3))
        .followRedirects(HttpClient.Redirect.NEVER)
        .build();

    @GetMapping("/fetch-metadata")
    @Operation(summary = "Fetch a remote URL server-side after SSRF validation")
    public ResponseEntity<?> fetchMetadata(@RequestParam String url) {
        URI safeUri;
        try {
            safeUri = SsrfGuard.validate(url);
        } catch (SsrfValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Blocked: " + e.getMessage());
        }

        try {
            HttpRequest request = HttpRequest.newBuilder(safeUri)
                .timeout(Duration.ofSeconds(3))
                .GET()
                .build();
            HttpResponse<Void> response = httpClient.send(
                request, HttpResponse.BodyHandlers.discarding());
            return ResponseEntity.ok(Map.of(
                "url", safeUri.toString(),
                "status", response.statusCode(),
                "contentType", response.headers().firstValue("content-type").orElse("unknown")));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Fetch failed");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Fetch failed");
        }
    }
}
