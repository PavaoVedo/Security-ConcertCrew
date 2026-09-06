package hr.algebra.concertcrew.config;

import hr.algebra.concertcrew.entity.Concert;
import hr.algebra.concertcrew.entity.User;
import hr.algebra.concertcrew.enums.Genre;
import hr.algebra.concertcrew.enums.Role;
import hr.algebra.concertcrew.enums.ShowType;
import hr.algebra.concertcrew.enums.VenueSize;
import hr.algebra.concertcrew.enums.ViewingArea;
import hr.algebra.concertcrew.repository.ConcertRepository;
import hr.algebra.concertcrew.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final ConcertRepository concertRepository;
    private final PasswordEncoder passwordEncoder;

    @org.springframework.beans.factory.annotation.Value("${app.seed.admin-password}")
    private String adminPassword;

    @org.springframework.beans.factory.annotation.Value("${app.seed.user-password}")
    private String userPassword;

    public DataInitializer(
            UserRepository userRepository,
            ConcertRepository concertRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.concertRepository = concertRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) return;

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@concertcrew.hr");
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(Role.ADMIN);
        admin = userRepository.save(admin);

        User user = new User();
        user.setUsername("user");
        user.setEmail("user@concertcrew.hr");
        user.setPassword(passwordEncoder.encode(userPassword));
        user.setRole(Role.USER);
        userRepository.save(user);

        Concert c1 = base("Taylor Swift", "The Eras Tour", "Wembley Stadium", "London", "UK",
            Genre.POP, ShowType.STADIUM_TOUR, LocalDate.of(2024, 6, 21), admin);
        c1.setOpeningActs("Paramore");
        c1.setSecondaryGenre(Genre.INDIE_POP);
        c1.setViewingArea(ViewingArea.SEATED_FLOOR);
        c1.setVenueSize(VenueSize.STADIUM);
        c1.setTicketPriceEur(new BigDecimal("210.00"));
        c1.setMerchSpentEur(new BigDecimal("85.00"));
        c1.setTravelCostEur(new BigDecimal("320.00"));
        c1.setRating(5); c1.setVibeScore(10); c1.setSetlistScore(10);
        c1.setProductionScore(10); c1.setCrowdEnergyScore(10); c1.setVocalFormScore(9);
        c1.setDurationMinutes(195); c1.setSongsPlayed(44); c1.setEncoreCount(3);
        c1.setFirstTimeSeen(true); c1.setTearJerker(true); c1.setDancedAllNight(true); c1.setBoughtMerch(true);
        c1.setCrewWith("Mia, Hanna, Ema (all swifties since debut)");
        c1.setFavoriteSong("All Too Well (10 Minute Version)");
        c1.setBestMoment("The Folklore set under the moss-covered cabin. The whole stadium silent then erupting.");
        c1.setMoodTags("Cathartic, generational, surreal, friendship bracelets traded");
        c1.setSetlistNotes("44 songs across 10 eras, surprise songs: Maroon (acoustic) + The Manuscript. The 1989 set was peak.");
        c1.setPersonalNotes("Worth every cent of the €210 + travel. Wore the bejeweled outfit. Cried 4 times. Best show of my life.");
        concertRepository.save(c1);

        Concert c2 = base("The 1975", "Still... At Their Very Best", "O2 Academy Brixton", "London", "UK",
            Genre.INDIE_POP, ShowType.ARENA_TOUR, LocalDate.of(2024, 3, 12), admin);
        c2.setOpeningActs("Holly Humberstone");
        c2.setSecondaryGenre(Genre.ALT_ROCK);
        c2.setViewingArea(ViewingArea.BARRICADE);
        c2.setVenueSize(VenueSize.LARGE_VENUE);
        c2.setTicketPriceEur(new BigDecimal("75.00"));
        c2.setMerchSpentEur(new BigDecimal("40.00"));
        c2.setTravelCostEur(new BigDecimal("60.00"));
        c2.setRating(5); c2.setVibeScore(10); c2.setSetlistScore(9);
        c2.setProductionScore(10); c2.setCrowdEnergyScore(10); c2.setVocalFormScore(9);
        c2.setDurationMinutes(115); c2.setSongsPlayed(22); c2.setEncoreCount(2);
        c2.setTearJerker(true); c2.setDancedAllNight(true); c2.setBoughtMerch(true);
        c2.setCrewWith("Solo (queued 14 hours)");
        c2.setFavoriteSong("About You");
        c2.setBestMoment("About You finale with confetti drop. Matty went off-script for two minutes. The set room with the TV.");
        c2.setMoodTags("Theatrical, melancholy, intoxicating, fan-projection");
        c2.setSetlistNotes("The whole 'house' set design is genius. I'm In Love With You was unhinged in the best way.");
        c2.setPersonalNotes("Queued from 6am to get barricade. Worth it. Made friends in line.");
        concertRepository.save(c2);

        Concert c3 = base("Phoebe Bridgers", "Reunion Tour", "Alexandra Palace", "London", "UK",
            Genre.INDIE_ROCK, ShowType.ARENA_TOUR, LocalDate.of(2024, 9, 8), admin);
        c3.setOpeningActs("MUNA, Christian Lee Hutson");
        c3.setSecondaryGenre(Genre.INDIE_POP);
        c3.setViewingArea(ViewingArea.GENERAL_ADMISSION);
        c3.setVenueSize(VenueSize.LARGE_VENUE);
        c3.setTicketPriceEur(new BigDecimal("55.00"));
        c3.setMerchSpentEur(new BigDecimal("35.00"));
        c3.setTravelCostEur(new BigDecimal("40.00"));
        c3.setRating(5); c3.setVibeScore(9); c3.setSetlistScore(10);
        c3.setProductionScore(8); c3.setCrowdEnergyScore(9); c3.setVocalFormScore(10);
        c3.setDurationMinutes(95); c3.setSongsPlayed(18); c3.setEncoreCount(2);
        c3.setFirstTimeSeen(true); c3.setTearJerker(true);
        c3.setCrewWith("Pol (his first big indie show)");
        c3.setFavoriteSong("I Know The End");
        c3.setBestMoment("Everyone screaming the I Know The End outro. Cathartic doesn't begin to cover it.");
        c3.setMoodTags("Sad girl autumn, cathartic scream, communal grief");
        c3.setSetlistNotes("Full Punisher in order + Boygenius reunion encore with Lucy and Julien.");
        c3.setPersonalNotes("MUNA were unreal too. Bought the Skeleton tour tee.");
        concertRepository.save(c3);

        Concert c4 = base("Fred again..", "Boiler Room x ConcertCrew", "Printworks", "London", "UK",
            Genre.ELECTRONIC, ShowType.SECRET_SHOW, LocalDate.of(2023, 11, 4), admin);
        c4.setOpeningActs("Skrillex (b2b set)");
        c4.setSecondaryGenre(Genre.HOUSE);
        c4.setViewingArea(ViewingArea.STANDING_ONLY);
        c4.setVenueSize(VenueSize.LARGE_VENUE);
        c4.setTicketPriceEur(new BigDecimal("90.00"));
        c4.setMerchSpentEur(BigDecimal.ZERO);
        c4.setTravelCostEur(new BigDecimal("30.00"));
        c4.setRating(5); c4.setVibeScore(10); c4.setSetlistScore(9);
        c4.setProductionScore(10); c4.setCrowdEnergyScore(10); c4.setVocalFormScore(9);
        c4.setDurationMinutes(165); c4.setSongsPlayed(28); c4.setEncoreCount(0);
        c4.setPhoneFreeShow(true); c4.setFirstTimeSeen(true); c4.setDancedAllNight(true);
        c4.setCrewWith("Jake, Hanna, two strangers we met in the smoking area");
        c4.setFavoriteSong("Marea (we've lost dancing)");
        c4.setBestMoment("The Skrillex b2b. Fred dropped Rumble unannounced. The whole crowd lost it.");
        c4.setMoodTags("Euphoric, transcendent, phones-locked-up, full body sweat");
        c4.setSetlistNotes("Yondr pouches at door. Best decision. Nobody filmed, everyone DANCED.");
        c4.setPersonalNotes("Phone-free shows are how concerts should be. Found tickets day-of via the secret announce.");
        concertRepository.save(c4);

        Concert c5 = base("NewJeans", "Bunnies Camp Tour", "Tokyo Dome", "Tokyo", "Japan",
            Genre.KPOP, ShowType.ARENA_TOUR, LocalDate.of(2024, 6, 26), admin);
        c5.setOpeningActs(null);
        c5.setSecondaryGenre(Genre.POP);
        c5.setViewingArea(ViewingArea.SEATED_FLOOR);
        c5.setVenueSize(VenueSize.STADIUM);
        c5.setTicketPriceEur(new BigDecimal("180.00"));
        c5.setMerchSpentEur(new BigDecimal("120.00"));
        c5.setTravelCostEur(new BigDecimal("950.00"));
        c5.setRating(5); c5.setVibeScore(10); c5.setSetlistScore(9);
        c5.setProductionScore(10); c5.setCrowdEnergyScore(10); c5.setVocalFormScore(10);
        c5.setDurationMinutes(140); c5.setSongsPlayed(20); c5.setEncoreCount(2);
        c5.setFirstTimeSeen(true); c5.setBoughtMerch(true); c5.setDancedAllNight(true);
        c5.setCrewWith("Ema (we flew to Tokyo for this)");
        c5.setFavoriteSong("Super Shy");
        c5.setBestMoment("The ETA stage with the entire dome in sync with the lightsticks.");
        c5.setMoodTags("Y2K kawaii, lightstick ocean, bunnies forever, jet-lagged but powering through");
        c5.setSetlistNotes("Ditto + Hype Boy back-to-back was the killer combo. New choreography for Bubble Gum.");
        c5.setPersonalNotes("Worth the flight. The Tokyo Dome lightstick coordination is unmatched.");
        concertRepository.save(c5);

        Concert c6 = base("boygenius", "the rest tour", "Gunnersbury Park", "London", "UK",
            Genre.INDIE_ROCK, ShowType.FESTIVAL_SET, LocalDate.of(2023, 8, 20), admin);
        c6.setOpeningActs("Ethel Cain, Muna");
        c6.setSecondaryGenre(Genre.INDIE_POP);
        c6.setViewingArea(ViewingArea.FESTIVAL_FIELD);
        c6.setVenueSize(VenueSize.FESTIVAL_FIELD);
        c6.setTicketPriceEur(new BigDecimal("75.00"));
        c6.setMerchSpentEur(new BigDecimal("45.00"));
        c6.setTravelCostEur(new BigDecimal("25.00"));
        c6.setRating(5); c6.setVibeScore(10); c6.setSetlistScore(10);
        c6.setProductionScore(8); c6.setCrowdEnergyScore(10); c6.setVocalFormScore(10);
        c6.setDurationMinutes(75); c6.setSongsPlayed(14); c6.setEncoreCount(1);
        c6.setFirstTimeSeen(true); c6.setTearJerker(true); c6.setBoughtMerch(true);
        c6.setCrewWith("Mia, Hanna, three other queer girls we met in line");
        c6.setFavoriteSong("Not Strong Enough");
        c6.setBestMoment("The three of them harmonizing on Letter To An Old Poet at golden hour.");
        c6.setMoodTags("Sapphic awakening, golden hour cry, friendship trio worship, communal");
        c6.setSetlistNotes("Full the record album + selections from the rest EP. Ethel Cain opener stole hearts.");
        c6.setPersonalNotes("Best summer afternoon of my life. Ethel Cain Preacher live = transcendent.");
        concertRepository.save(c6);

        Concert c7 = base("Mitski", "The Land Is Inhospitable Tour", "Eventim Apollo", "London", "UK",
            Genre.INDIE_ROCK, ShowType.ARENA_TOUR, LocalDate.of(2024, 4, 18), admin);
        c7.setOpeningActs("Madison Cunningham");
        c7.setSecondaryGenre(Genre.INDIE_POP);
        c7.setViewingArea(ViewingArea.SEATED_LOWER);
        c7.setVenueSize(VenueSize.MID_VENUE);
        c7.setTicketPriceEur(new BigDecimal("65.00"));
        c7.setMerchSpentEur(new BigDecimal("28.00"));
        c7.setTravelCostEur(new BigDecimal("18.00"));
        c7.setRating(5); c7.setVibeScore(9); c7.setSetlistScore(10);
        c7.setProductionScore(10); c7.setCrowdEnergyScore(8); c7.setVocalFormScore(10);
        c7.setDurationMinutes(85); c7.setSongsPlayed(20); c7.setEncoreCount(2);
        c7.setPhoneFreeShow(true); c7.setTearJerker(true);
        c7.setCrewWith("Solo (intentionally)");
        c7.setFavoriteSong("Last Words of a Shooting Star");
        c7.setBestMoment("Her choreographed movements during Washing Machine Heart. Visual poetry.");
        c7.setMoodTags("Theatrical, devastating, intimate-despite-2000-people, intentional");
        c7.setSetlistNotes("Phone-free policy was perfect. Mitski's stage direction is unparalleled.");
        c7.setPersonalNotes("Went alone on purpose. Cried discreetly during 'I Bet on Losing Dogs'. Healing.");
        concertRepository.save(c7);

        Concert c8 = base("Charli XCX", "Brat Tour", "Crystal Palace Park", "London", "UK",
            Genre.HYPERPOP, ShowType.FESTIVAL_SET, LocalDate.of(2024, 7, 5), admin);
        c8.setOpeningActs("Shygirl, A. G. Cook DJ set");
        c8.setSecondaryGenre(Genre.ELECTRONIC);
        c8.setViewingArea(ViewingArea.FESTIVAL_FIELD);
        c8.setVenueSize(VenueSize.FESTIVAL_FIELD);
        c8.setTicketPriceEur(new BigDecimal("95.00"));
        c8.setMerchSpentEur(new BigDecimal("50.00"));
        c8.setTravelCostEur(new BigDecimal("25.00"));
        c8.setRating(5); c8.setVibeScore(10); c8.setSetlistScore(10);
        c8.setProductionScore(9); c8.setCrowdEnergyScore(10); c8.setVocalFormScore(9);
        c8.setDurationMinutes(80); c8.setSongsPlayed(16); c8.setEncoreCount(0);
        c8.setDancedAllNight(true); c8.setBoughtMerch(true);
        c8.setCrewWith("Mia, Jake, Pol (we all wore brat-green)");
        c8.setFavoriteSong("365");
        c8.setBestMoment("Apple turned the entire field into one choreographed flash mob. Brat summer peaked.");
        c8.setMoodTags("Brat summer, sweaty, lime-green tribalism, hyperpop euphoria");
        c8.setSetlistNotes("Full Brat in order + Track 10 + Vroom Vroom encore. A.G. Cook B2B was insane.");
        c8.setPersonalNotes("This show defined the summer of 2024. Worth it just for the Apple choreo viral moment.");
        concertRepository.save(c8);

        Concert c9 = base("Bad Bunny", "Most Wanted Tour", "Estadio Olimpico", "Madrid", "Spain",
            Genre.RAP, ShowType.STADIUM_TOUR, LocalDate.of(2024, 5, 23), admin);
        c9.setOpeningActs(null);
        c9.setSecondaryGenre(Genre.HIP_HOP);
        c9.setViewingArea(ViewingArea.SEATED_UPPER);
        c9.setVenueSize(VenueSize.STADIUM);
        c9.setTicketPriceEur(new BigDecimal("145.00"));
        c9.setMerchSpentEur(new BigDecimal("0.00"));
        c9.setTravelCostEur(new BigDecimal("280.00"));
        c9.setRating(4); c9.setVibeScore(9); c9.setSetlistScore(8);
        c9.setProductionScore(10); c9.setCrowdEnergyScore(10); c9.setVocalFormScore(8);
        c9.setDurationMinutes(130); c9.setSongsPlayed(32); c9.setEncoreCount(1);
        c9.setFirstTimeSeen(true); c9.setDancedAllNight(true);
        c9.setCrewWith("Hanna + her cousins from Madrid");
        c9.setFavoriteSong("Yo Perreo Sola");
        c9.setBestMoment("The mechanical bull rising during Monaco. Latin trap stadium chaos.");
        c9.setMoodTags("Perreo, latin trap takeover, summer in Madrid, sweaty euphoria");
        c9.setSetlistNotes("Heavy on nadie sabe + un verano sin ti cuts. Saturno trap deep cuts too.");
        c9.setPersonalNotes("Stadium upper section is sketchy but the production made up for it. Hanna's family fed us paella.");
        concertRepository.save(c9);

        Concert c10 = base("Black Country, New Road", "Live at Bush Hall Tour", "EartH Hackney", "London", "UK",
            Genre.INDIE_ROCK, ShowType.INTIMATE_VENUE, LocalDate.of(2024, 2, 9), admin);
        c10.setOpeningActs("Caroline");
        c10.setSecondaryGenre(Genre.FOLK);
        c10.setViewingArea(ViewingArea.SEATED_FLOOR);
        c10.setVenueSize(VenueSize.SMALL_CLUB);
        c10.setTicketPriceEur(new BigDecimal("32.00"));
        c10.setMerchSpentEur(new BigDecimal("25.00"));
        c10.setTravelCostEur(new BigDecimal("12.00"));
        c10.setRating(5); c10.setVibeScore(10); c10.setSetlistScore(9);
        c10.setProductionScore(7); c10.setCrowdEnergyScore(7); c10.setVocalFormScore(10);
        c10.setDurationMinutes(105); c10.setSongsPlayed(15); c10.setEncoreCount(1);
        c10.setFirstTimeSeen(true); c10.setTearJerker(true);
        c10.setCrewWith("Pol (we both wept)");
        c10.setFavoriteSong("Turbines/Pigs");
        c10.setBestMoment("The brass section during The Boy. Post-rock at its absolute peak.");
        c10.setMoodTags("Cathartic chamber-rock, post-Isaac era, communal silence, devastating");
        c10.setSetlistNotes("Almost entirely new material from Forever Howlong. Live At Bush Hall was incredible too.");
        c10.setPersonalNotes("BCNR live is a different band than the records. The new era is even better. Bought the vinyl.");
        concertRepository.save(c10);
    }

    private Concert base(
        String mainArtist, String tourName, String venue, String city, String country,
        Genre genre, ShowType showType, LocalDate dateAttended, User addedBy
    ) {
        Concert c = new Concert();
        c.setMainArtist(mainArtist);
        c.setTourName(tourName);
        c.setVenue(venue);
        c.setCity(city);
        c.setCountry(country);
        c.setGenre(genre);
        c.setShowType(showType);
        c.setDateAttended(dateAttended);
        c.setAddedBy(addedBy);
        return c;
    }
}
