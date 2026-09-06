package hr.algebra.concertcrew.service;

import hr.algebra.concertcrew.dto.ConcertDto;
import hr.algebra.concertcrew.entity.Concert;
import hr.algebra.concertcrew.entity.User;
import hr.algebra.concertcrew.enums.Genre;
import hr.algebra.concertcrew.enums.ShowType;
import hr.algebra.concertcrew.repository.ConcertRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTest {

    @Mock
    ConcertRepository concertRepository;
    @InjectMocks
    ConcertService concertService;

    private Concert sampleConcert() {
        Concert c = new Concert();
        c.setId(1L);
        c.setMainArtist("Test Band");
        c.setVenue("Test Arena");
        c.setCity("Zagreb");
        c.setGenre(Genre.POP);
        c.setShowType(ShowType.ARENA_TOUR);
        c.setDateAttended(LocalDate.of(2024, 1, 1));
        return c;
    }

    @Test
    void findAllReturnsMappedDtos() {
        when(concertRepository.findAllByOrderByDateAttendedDescRatingDescMainArtistAsc())
            .thenReturn(List.of(sampleConcert()));

        List<ConcertDto> result = concertService.findAll();

        assertEquals(1, result.size());
        assertEquals("Test Band", result.get(0).mainArtist());
    }

    @Test
    void findByIdReturnsDto() {
        when(concertRepository.findById(1L)).thenReturn(Optional.of(sampleConcert()));
        assertEquals("Test Band", concertService.findById(1L).mainArtist());
    }

    @Test
    void findByIdThrowsWhenMissing() {
        when(concertRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> concertService.findById(99L));
    }

    @Test
    void searchBlankQueryIsNormalisedToNull() {
        when(concertRepository.search(null, null, null, false))
            .thenReturn(List.of(sampleConcert()));
        assertEquals(1, concertService.search("   ", null, null, false).size());
    }

    @Test
    void createSavesAndReturnsDto() {
        Concert saved = sampleConcert();
        when(concertRepository.save(any(Concert.class))).thenReturn(saved);

        ConcertDto result = concertService.create(ConcertDto.from(saved), new User());

        assertEquals("Test Band", result.mainArtist());
        verify(concertRepository).save(any(Concert.class));
    }

    @Test
    void updateModifiesExistingConcert() {
        Concert existing = sampleConcert();
        when(concertRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(concertRepository.save(any(Concert.class))).thenReturn(existing);

        ConcertDto result = concertService.update(1L, ConcertDto.from(existing));

        assertEquals("Test Band", result.mainArtist());
    }

    @Test
    void updateThrowsWhenMissing() {
        when(concertRepository.findById(99L)).thenReturn(Optional.empty());
        ConcertDto dto = ConcertDto.from(sampleConcert());
        assertThrows(NoSuchElementException.class, () -> concertService.update(99L, dto));
    }

    @Test
    void deleteRemovesWhenPresent() {
        when(concertRepository.existsById(1L)).thenReturn(true);
        concertService.delete(1L);
        verify(concertRepository).deleteById(1L);
    }

    @Test
    void deleteThrowsWhenMissing() {
        when(concertRepository.existsById(99L)).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> concertService.delete(99L));
    }
}
