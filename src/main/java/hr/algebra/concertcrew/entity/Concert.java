package hr.algebra.concertcrew.entity;

import hr.algebra.concertcrew.enums.Genre;
import hr.algebra.concertcrew.enums.ShowType;
import hr.algebra.concertcrew.enums.VenueSize;
import hr.algebra.concertcrew.enums.ViewingArea;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "concerts")
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String mainArtist;

    @Size(max = 200)
    private String tourName;

    @Size(max = 300)
    private String openingActs;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String venue;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String city;

    @Size(max = 100)
    private String country;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genre genre;

    @Enumerated(EnumType.STRING)
    private Genre secondaryGenre;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShowType showType;

    @Enumerated(EnumType.STRING)
    private ViewingArea viewingArea;

    @Enumerated(EnumType.STRING)
    private VenueSize venueSize;

    @NotNull
    @Column(nullable = false)
    private LocalDate dateAttended;

    @Digits(integer = 8, fraction = 2) @DecimalMin("0.0")
    private BigDecimal ticketPriceEur;

    @Digits(integer = 8, fraction = 2) @DecimalMin("0.0")
    private BigDecimal merchSpentEur;

    @Digits(integer = 8, fraction = 2) @DecimalMin("0.0")
    private BigDecimal travelCostEur;

    @Min(1) @Max(5)
    private Integer rating;

    @Min(1) @Max(10)
    private Integer vibeScore;

    @Min(1) @Max(10)
    private Integer setlistScore;

    @Min(1) @Max(10)
    private Integer productionScore;

    @Min(1) @Max(10)
    private Integer crowdEnergyScore;

    @Min(1) @Max(10)
    private Integer vocalFormScore;

    @Min(0) @Max(600)
    private Integer durationMinutes;

    @Min(0) @Max(100)
    private Integer songsPlayed;

    @Min(0) @Max(20)
    private Integer encoreCount;

    @Column(nullable = false)
    private boolean phoneFreeShow = false;

    @Column(nullable = false)
    private boolean firstTimeSeen = false;

    @Column(nullable = false)
    private boolean tearJerker = false;

    @Column(nullable = false)
    private boolean dancedAllNight = false;

    @Column(nullable = false)
    private boolean boughtMerch = false;

    @Size(max = 200)
    private String crewWith;

    @Size(max = 200)
    private String favoriteSong;

    @Size(max = 500)
    @Column(length = 500)
    private String bestMoment;

    @Size(max = 200)
    private String moodTags;

    @Size(max = 2000)
    @Column(length = 2000)
    private String setlistNotes;

    @Size(max = 2000)
    @Column(length = 2000)
    private String personalNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by_id")
    private User addedBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Total cost of attending = ticket + merch + travel. Returns zero if all three are null.
     */
    @Transient
    public BigDecimal getTotalSpentEur() {
        BigDecimal total = BigDecimal.ZERO;
        if (ticketPriceEur != null) total = total.add(ticketPriceEur);
        if (merchSpentEur != null)  total = total.add(merchSpentEur);
        if (travelCostEur != null)  total = total.add(travelCostEur);
        return total;
    }

    public Long getId()                                  { return id; }
    public void setId(Long id)                           { this.id = id; }
    public String getMainArtist()                        { return mainArtist; }
    public void setMainArtist(String mainArtist)         { this.mainArtist = mainArtist; }
    public String getTourName()                          { return tourName; }
    public void setTourName(String tourName)             { this.tourName = tourName; }
    public String getOpeningActs()                       { return openingActs; }
    public void setOpeningActs(String openingActs)       { this.openingActs = openingActs; }
    public String getVenue()                             { return venue; }
    public void setVenue(String venue)                   { this.venue = venue; }
    public String getCity()                              { return city; }
    public void setCity(String city)                     { this.city = city; }
    public String getCountry()                           { return country; }
    public void setCountry(String country)               { this.country = country; }
    public Genre getGenre()                              { return genre; }
    public void setGenre(Genre genre)                    { this.genre = genre; }
    public Genre getSecondaryGenre()                     { return secondaryGenre; }
    public void setSecondaryGenre(Genre g)               { this.secondaryGenre = g; }
    public ShowType getShowType()                        { return showType; }
    public void setShowType(ShowType showType)           { this.showType = showType; }
    public ViewingArea getViewingArea()                  { return viewingArea; }
    public void setViewingArea(ViewingArea v)            { this.viewingArea = v; }
    public VenueSize getVenueSize()                      { return venueSize; }
    public void setVenueSize(VenueSize venueSize)        { this.venueSize = venueSize; }
    public LocalDate getDateAttended()                   { return dateAttended; }
    public void setDateAttended(LocalDate d)             { this.dateAttended = d; }
    public BigDecimal getTicketPriceEur()                { return ticketPriceEur; }
    public void setTicketPriceEur(BigDecimal p)          { this.ticketPriceEur = p; }
    public BigDecimal getMerchSpentEur()                 { return merchSpentEur; }
    public void setMerchSpentEur(BigDecimal p)           { this.merchSpentEur = p; }
    public BigDecimal getTravelCostEur()                 { return travelCostEur; }
    public void setTravelCostEur(BigDecimal p)           { this.travelCostEur = p; }
    public Integer getRating()                           { return rating; }
    public void setRating(Integer rating)                { this.rating = rating; }
    public Integer getVibeScore()                        { return vibeScore; }
    public void setVibeScore(Integer vibeScore)          { this.vibeScore = vibeScore; }
    public Integer getSetlistScore()                     { return setlistScore; }
    public void setSetlistScore(Integer setlistScore)    { this.setlistScore = setlistScore; }
    public Integer getProductionScore()                  { return productionScore; }
    public void setProductionScore(Integer p)            { this.productionScore = p; }
    public Integer getCrowdEnergyScore()                 { return crowdEnergyScore; }
    public void setCrowdEnergyScore(Integer c)           { this.crowdEnergyScore = c; }
    public Integer getVocalFormScore()                   { return vocalFormScore; }
    public void setVocalFormScore(Integer v)             { this.vocalFormScore = v; }
    public Integer getDurationMinutes()                  { return durationMinutes; }
    public void setDurationMinutes(Integer d)            { this.durationMinutes = d; }
    public Integer getSongsPlayed()                      { return songsPlayed; }
    public void setSongsPlayed(Integer songsPlayed)      { this.songsPlayed = songsPlayed; }
    public Integer getEncoreCount()                      { return encoreCount; }
    public void setEncoreCount(Integer encoreCount)      { this.encoreCount = encoreCount; }
    public boolean isPhoneFreeShow()                     { return phoneFreeShow; }
    public void setPhoneFreeShow(boolean phoneFreeShow)  { this.phoneFreeShow = phoneFreeShow; }
    public boolean isFirstTimeSeen()                     { return firstTimeSeen; }
    public void setFirstTimeSeen(boolean firstTimeSeen)  { this.firstTimeSeen = firstTimeSeen; }
    public boolean isTearJerker()                        { return tearJerker; }
    public void setTearJerker(boolean tearJerker)        { this.tearJerker = tearJerker; }
    public boolean isDancedAllNight()                    { return dancedAllNight; }
    public void setDancedAllNight(boolean d)             { this.dancedAllNight = d; }
    public boolean isBoughtMerch()                       { return boughtMerch; }
    public void setBoughtMerch(boolean boughtMerch)      { this.boughtMerch = boughtMerch; }
    public String getCrewWith()                          { return crewWith; }
    public void setCrewWith(String crewWith)             { this.crewWith = crewWith; }
    public String getFavoriteSong()                      { return favoriteSong; }
    public void setFavoriteSong(String favoriteSong)     { this.favoriteSong = favoriteSong; }
    public String getBestMoment()                        { return bestMoment; }
    public void setBestMoment(String bestMoment)         { this.bestMoment = bestMoment; }
    public String getMoodTags()                          { return moodTags; }
    public void setMoodTags(String moodTags)             { this.moodTags = moodTags; }
    public String getSetlistNotes()                      { return setlistNotes; }
    public void setSetlistNotes(String setlistNotes)     { this.setlistNotes = setlistNotes; }
    public String getPersonalNotes()                     { return personalNotes; }
    public void setPersonalNotes(String p)               { this.personalNotes = p; }
    public User getAddedBy()                             { return addedBy; }
    public void setAddedBy(User addedBy)                 { this.addedBy = addedBy; }
    public LocalDateTime getCreatedAt()                  { return createdAt; }
    public void setCreatedAt(LocalDateTime t)            { this.createdAt = t; }
    public LocalDateTime getUpdatedAt()                  { return updatedAt; }
    public void setUpdatedAt(LocalDateTime t)            { this.updatedAt = t; }
}
