package hr.algebra.concertcrew.dto;

import hr.algebra.concertcrew.entity.Concert;
import hr.algebra.concertcrew.entity.User;
import hr.algebra.concertcrew.enums.Genre;
import hr.algebra.concertcrew.enums.ShowType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ConcertDtoTest {

    private Concert fullConcert() {
        Concert c = new Concert();
        c.setId(7L);
        c.setMainArtist("Mitski");
        c.setTourName("The Land Is Inhospitable Tour");
        c.setVenue("Eventim Apollo");
        c.setCity("London");
        c.setCountry("UK");
        c.setGenre(Genre.INDIE_ROCK);
        c.setShowType(ShowType.ARENA_TOUR);
        c.setDateAttended(LocalDate.of(2024, 4, 18));
        c.setTicketPriceEur(new BigDecimal("65.00"));
        c.setRating(5);
        c.setSongsPlayed(20);
        c.setPhoneFreeShow(true);
        c.setTearJerker(true);
        return c;
    }

    @Test
    void fromCopiesFieldsFromEntity() {
        ConcertDto dto = ConcertDto.from(fullConcert());

        assertEquals("Mitski", dto.mainArtist());
        assertEquals("Eventim Apollo", dto.venue());
        assertEquals(Genre.INDIE_ROCK, dto.genre());
        assertEquals(5, dto.rating());
        assertTrue(dto.phoneFreeShow());
    }

    @Test
    void fromMapsAddedByToUsername() {
        Concert c = fullConcert();
        User u = new User();
        u.setUsername("admin");
        c.setAddedBy(u);

        assertEquals("admin", ConcertDto.from(c).addedBy());
    }

    @Test
    void fromToleratesNullAddedBy() {
        assertNull(ConcertDto.from(fullConcert()).addedBy());
    }

    @Test
    void applyToWritesEditableFieldsOntoEntity() {
        ConcertDto dto = ConcertDto.from(fullConcert());
        Concert target = new Concert();

        dto.applyTo(target);

        assertEquals("Mitski", target.getMainArtist());
        assertEquals("London", target.getCity());
        assertEquals(Genre.INDIE_ROCK, target.getGenre());
        assertEquals(20, target.getSongsPlayed());
        assertTrue(target.isTearJerker());
    }
}
