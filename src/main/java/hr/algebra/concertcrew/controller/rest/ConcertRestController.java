package hr.algebra.concertcrew.controller.rest;

import hr.algebra.concertcrew.dto.ConcertDto;
import hr.algebra.concertcrew.entity.User;
import hr.algebra.concertcrew.enums.Genre;
import hr.algebra.concertcrew.enums.ShowType;
import hr.algebra.concertcrew.service.ConcertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/concerts")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Concerts", description = "Live concert CRUD and search")
public class ConcertRestController {

    private final ConcertService concertService;

    public ConcertRestController(ConcertService concertService) {
        this.concertService = concertService;
    }

    @GetMapping
    @Operation(summary = "Get all concerts")
    public ResponseEntity<List<ConcertDto>> getAll() {
        return ResponseEntity.ok(concertService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single concert by ID")
    public ResponseEntity<ConcertDto> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(concertService.findById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search concerts by artist, tour, venue, city, openers, genre, show type, or tearJerkerOnly")
    public ResponseEntity<List<ConcertDto>> search(
        @RequestParam(required = false) String query,
        @RequestParam(required = false) Genre genre,
        @RequestParam(required = false) ShowType showType,
        @RequestParam(defaultValue = "false") boolean tearJerkerOnly
    ) {
        return ResponseEntity.ok(concertService.search(query, genre, showType, tearJerkerOnly));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add a new concert (admin only)")
    public ResponseEntity<ConcertDto> create(
        @Valid @RequestBody ConcertDto dto,
        @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(concertService.create(dto, currentUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a concert (admin only)")
    public ResponseEntity<ConcertDto> update(
        @PathVariable Long id,
        @Valid @RequestBody ConcertDto dto
    ) {
        try {
            return ResponseEntity.ok(concertService.update(id, dto));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a concert (admin only)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            concertService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
