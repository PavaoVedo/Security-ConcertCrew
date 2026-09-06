package hr.algebra.concertcrew.dto;

import hr.algebra.concertcrew.entity.Concert;
import hr.algebra.concertcrew.enums.Genre;
import hr.algebra.concertcrew.enums.ShowType;
import hr.algebra.concertcrew.enums.VenueSize;
import hr.algebra.concertcrew.enums.ViewingArea;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Live concert data transfer object")
public record ConcertDto(

    // 1
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    Long id,

    // 2
    @NotBlank @Size(max = 200)
    @Schema(description = "Main artist / headliner", example = "Taylor Swift")
    String mainArtist,

    // 3
    @Size(max = 200)
    @Schema(description = "Tour name", example = "The Eras Tour")
    String tourName,

    // 4
    @Size(max = 300)
    @Schema(description = "Opening acts", example = "Sabrina Carpenter, Gracie Abrams")
    String openingActs,

    // 5
    @NotBlank @Size(max = 200)
    @Schema(description = "Venue name", example = "Wembley Stadium")
    String venue,

    // 6
    @NotBlank @Size(max = 100)
    @Schema(description = "City", example = "London")
    String city,

    // 7
    @Size(max = 100)
    @Schema(description = "Country", example = "UK")
    String country,

    // 8
    @NotNull
    @Schema(description = "Primary genre")
    Genre genre,

    // 9
    @Schema(description = "Secondary genre")
    Genre secondaryGenre,

    // 10
    @NotNull
    @Schema(description = "Type of show")
    ShowType showType,

    // 11
    @Schema(description = "Where you stood / sat")
    ViewingArea viewingArea,

    // 12
    @Schema(description = "Venue size class")
    VenueSize venueSize,

    // 13
    @NotNull
    @Schema(description = "Date attended", example = "2024-06-21")
    LocalDate dateAttended,

    // 14
    @Digits(integer = 8, fraction = 2) @DecimalMin("0.0")
    @Schema(description = "Ticket price in EUR", example = "120.00")
    BigDecimal ticketPriceEur,

    // 15
    @Digits(integer = 8, fraction = 2) @DecimalMin("0.0")
    @Schema(description = "Merch spent in EUR", example = "60.00")
    BigDecimal merchSpentEur,

    // 16
    @Digits(integer = 8, fraction = 2) @DecimalMin("0.0")
    @Schema(description = "Travel cost in EUR", example = "180.00")
    BigDecimal travelCostEur,

    // 17
    @Min(1) @Max(5)
    @Schema(description = "Overall rating 1-5", example = "5")
    Integer rating,

    // 18
    @Min(1) @Max(10)
    @Schema(description = "Vibe score 1-10", example = "10")
    Integer vibeScore,

    // 19
    @Min(1) @Max(10)
    @Schema(description = "Setlist score 1-10", example = "10")
    Integer setlistScore,

    // 20
    @Min(1) @Max(10)
    @Schema(description = "Production / stage / lights score 1-10", example = "10")
    Integer productionScore,

    // 21
    @Min(1) @Max(10)
    @Schema(description = "Crowd energy 1-10", example = "9")
    Integer crowdEnergyScore,

    // 22
    @Min(1) @Max(10)
    @Schema(description = "Vocal / performance form 1-10", example = "10")
    Integer vocalFormScore,

    // 23
    @Min(0) @Max(600)
    @Schema(description = "Set duration in minutes", example = "210")
    Integer durationMinutes,

    // 24
    @Min(0) @Max(100)
    @Schema(description = "Number of songs played", example = "44")
    Integer songsPlayed,

    // 25
    @Min(0) @Max(20)
    @Schema(description = "Number of encore songs", example = "3")
    Integer encoreCount,

    // 26
    @Schema(description = "Was a phone-locked / Yondr show?")
    boolean phoneFreeShow,

    // 27
    @Schema(description = "First time seeing this artist?")
    boolean firstTimeSeen,

    // 28
    @Schema(description = "Did you cry?")
    boolean tearJerker,

    // 29
    @Schema(description = "Danced the whole night?")
    boolean dancedAllNight,

    // 30
    @Schema(description = "Bought merch?")
    boolean boughtMerch,

    // 31
    @Size(max = 200)
    @Schema(description = "Who you went with", example = "Mia, Jake, Pol")
    String crewWith,

    // 32
    @Size(max = 200)
    @Schema(description = "Favorite song of the night", example = "All Too Well (10 Minute Version)")
    String favoriteSong,

    // 33
    @Size(max = 500)
    @Schema(description = "Best moment of the show")
    String bestMoment,

    // 34
    @Size(max = 200)
    @Schema(description = "Mood / vibe tags", example = "Cathartic, surreal, generational")
    String moodTags,

    // 35
    @Size(max = 2000)
    @Schema(description = "Setlist notes")
    String setlistNotes,

    // 36
    @Size(max = 2000)
    @Schema(description = "Private notes")
    String personalNotes,

    // 37
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    BigDecimal totalSpentEur,

    // 38
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    String addedBy,

    // 39
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    LocalDateTime createdAt,

    // 40
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    LocalDateTime updatedAt
) {
    public static ConcertDto from(Concert c) {
        return new ConcertDto(
            c.getId(),                  //  1
            c.getMainArtist(),          //  2
            c.getTourName(),            //  3
            c.getOpeningActs(),         //  4
            c.getVenue(),               //  5
            c.getCity(),                //  6
            c.getCountry(),             //  7
            c.getGenre(),               //  8
            c.getSecondaryGenre(),      //  9
            c.getShowType(),            // 10
            c.getViewingArea(),         // 11
            c.getVenueSize(),           // 12
            c.getDateAttended(),        // 13
            c.getTicketPriceEur(),      // 14
            c.getMerchSpentEur(),       // 15
            c.getTravelCostEur(),       // 16
            c.getRating(),              // 17
            c.getVibeScore(),           // 18
            c.getSetlistScore(),        // 19
            c.getProductionScore(),     // 20
            c.getCrowdEnergyScore(),    // 21
            c.getVocalFormScore(),      // 22
            c.getDurationMinutes(),     // 23
            c.getSongsPlayed(),         // 24
            c.getEncoreCount(),         // 25
            c.isPhoneFreeShow(),        // 26
            c.isFirstTimeSeen(),        // 27
            c.isTearJerker(),           // 28
            c.isDancedAllNight(),       // 29
            c.isBoughtMerch(),          // 30
            c.getCrewWith(),            // 31
            c.getFavoriteSong(),        // 32
            c.getBestMoment(),          // 33
            c.getMoodTags(),            // 34
            c.getSetlistNotes(),        // 35
            c.getPersonalNotes(),       // 36
            c.getTotalSpentEur(),       // 37
            c.getAddedBy() != null ? c.getAddedBy().getUsername() : null, // 38
            c.getCreatedAt(),           // 39
            c.getUpdatedAt()            // 40
        );
    }

    /**
     * Copies editable fields onto the entity. Read-only/derived components
     * (id, totalSpentEur, addedBy, createdAt, updatedAt) are intentionally skipped.
     */
    public void applyTo(Concert c) {
        c.setMainArtist(mainArtist);
        c.setTourName(tourName);
        c.setOpeningActs(openingActs);
        c.setVenue(venue);
        c.setCity(city);
        c.setCountry(country);
        c.setGenre(genre);
        c.setSecondaryGenre(secondaryGenre);
        c.setShowType(showType);
        c.setViewingArea(viewingArea);
        c.setVenueSize(venueSize);
        c.setDateAttended(dateAttended);
        c.setTicketPriceEur(ticketPriceEur);
        c.setMerchSpentEur(merchSpentEur);
        c.setTravelCostEur(travelCostEur);
        c.setRating(rating);
        c.setVibeScore(vibeScore);
        c.setSetlistScore(setlistScore);
        c.setProductionScore(productionScore);
        c.setCrowdEnergyScore(crowdEnergyScore);
        c.setVocalFormScore(vocalFormScore);
        c.setDurationMinutes(durationMinutes);
        c.setSongsPlayed(songsPlayed);
        c.setEncoreCount(encoreCount);
        c.setPhoneFreeShow(phoneFreeShow);
        c.setFirstTimeSeen(firstTimeSeen);
        c.setTearJerker(tearJerker);
        c.setDancedAllNight(dancedAllNight);
        c.setBoughtMerch(boughtMerch);
        c.setCrewWith(crewWith);
        c.setFavoriteSong(favoriteSong);
        c.setBestMoment(bestMoment);
        c.setMoodTags(moodTags);
        c.setSetlistNotes(setlistNotes);
        c.setPersonalNotes(personalNotes);
    }
}
