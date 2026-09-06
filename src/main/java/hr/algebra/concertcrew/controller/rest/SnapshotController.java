package hr.algebra.concertcrew.controller.rest;

import hr.algebra.concertcrew.serialization.ConcertSnapshot;
import hr.algebra.concertcrew.serialization.ConcertSnapshotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InvalidClassException;
import java.io.StreamCorruptedException;

@RestController
@RequestMapping("/api/snapshots")
@Tag(name = "Snapshots", description = "Export / import concerts as secure Java serialized snapshots")
@SecurityRequirement(name = "bearerAuth")
public class SnapshotController {

    private final ConcertSnapshotService snapshotService;

    public SnapshotController(ConcertSnapshotService snapshotService) {
        this.snapshotService = snapshotService;
    }

    @GetMapping("/{concertId}/export")
    @Operation(summary = "Export a concert as a .ser snapshot file")
    public ResponseEntity<Resource> export(@PathVariable Long concertId) throws Exception {
        byte[] data = snapshotService.serialize(concertId);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=concert-" + concertId + ".ser")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(new ByteArrayResource(data));
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Import a .ser snapshot (validated: magic bytes + class whitelist)")
    public ResponseEntity<?> importSnapshot(@RequestParam("file") MultipartFile file) {
        try {
            ConcertSnapshot snapshot = snapshotService.importSnapshot(file.getBytes());
            return ResponseEntity.ok(snapshot);
        } catch (StreamCorruptedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Rejected: invalid binary structure (" + e.getMessage() + ")");
        } catch (InvalidClassException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Rejected: class not allowed (" + e.getMessage() + ")");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Rejected: " + e.getMessage());
        }
    }
}
