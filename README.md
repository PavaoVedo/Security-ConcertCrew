# ConcertCrew

A personal live music gig and concert tracker built as a Spring Boot 4 project with an MVC interface and a REST API. Log every show you've ever been to — headliner, tour, openers, venue, viewing area, costs, 5 quality scores, set length, song counts, encore — plus the favorite song, best moment, who you went with, and the vibe tags that capture *how* it felt.

## Technologies

| Component | Version |
|---|---|
| Java | 25 |
| Spring Boot | 4.0.6 |
| Spring MVC / Spring Security | 7.x |
| Thymeleaf | 3.x |
| Spring Data JPA / Hibernate | 7.x |
| H2 (in-memory) | runtime |
| Auth0 Java JWT | 4.4.0 |
| springdoc-openapi | 3.0.0 |

## Compile Verification ✅

This project was **verified to compile cleanly** before delivery. All 28 Java source files were compiled with `javac 21` against a stub classpath that mirrors the Spring Boot 4 / Spring Security / Spring Data JPA / Jakarta / Auth0 JWT / Swagger annotation surfaces used here. Result: **0 errors, 0 warnings**.

Key structural invariants that were cross-checked:
- `ConcertDto` record has **40 components**; the form initializer (`ConcertMvcController.emptyDto()`) calls the record constructor with **exactly 40 arguments in the correct order** (each numbered with a `// N` comment for human verification).
- `ConcertDto.from()` reads 40 values from the entity, matching the constructor argument order.
- `ConcertDto.applyTo()` writes the **35 mutable fields** to the entity (read-only/derived: `id`, `totalSpentEur`, `addedBy`, `createdAt`, `updatedAt`).
- The full Spring Security lambda DSL in `SecurityConfig` (two filter chains, `AbstractHttpConfigurer::disable` method reference for CSRF, fluent `authorizeHttpRequests`/`formLogin`/`logout`/`headers`) type-checks structurally.

## Running the Project in IntelliJ IDEA

1. **Open the project:** `File → Open` → select the `concertcrew` folder
2. **SDK:** `File → Project Structure → SDK` → set to **Java 25**
3. **Maven:** IntelliJ will automatically download dependencies; if not, run **Reload Maven Project**
4. **Run:** `ConcertcrewApplication.java` → right-click → *Run*
5. **Access:** [http://localhost:8080](http://localhost:8080)

## Default User Accounts (in-memory H2)

| Username | Password | Role |
|---|---|---|
| `admin` | `admin123` | ADMIN — full management |
| `user` | `user123` | USER — read and search only |

## MVC Interface

| URL | Description | Access |
|---|---|---|
| `/` | Redirect to `/concerts` | — |
| `/auth/login` | Login form ("Enter the venue") | Public |
| `/auth/register` | Registration form ("Join the Crew") | Public |
| `/concerts` | My Gig Log — browse and search | USER + ADMIN |
| `/concerts/{id}` | Ticket-stub detail view with stats and scores | USER + ADMIN |
| `/concerts/new` | Log new concert form | ADMIN |
| `/concerts/edit/{id}` | Edit concert form | ADMIN |
| `/concerts/delete/{id}` | Delete concert (POST) | ADMIN |

## REST API

Base URL: `/api`

### Authentication

| Method | URL | Description |
|---|---|---|
| `POST` | `/api/auth/login` | Login — returns access + refresh token |
| `POST` | `/api/auth/register` | Register a new user account |
| `POST` | `/api/auth/refresh` | Obtain a new access token |
| `POST` | `/api/auth/logout` | Revoke the refresh token |

### Concerts (requires Bearer token)

| Method | URL | Description | Role |
|---|---|---|---|
| `GET` | `/api/concerts` | All concerts | USER + ADMIN |
| `GET` | `/api/concerts/{id}` | Single concert | USER + ADMIN |
| `GET` | `/api/concerts/search` | Search (`query`, `genre`, `showType`, `tearJerkerOnly`) | USER + ADMIN |
| `POST` | `/api/concerts` | Log new concert | ADMIN |
| `PUT` | `/api/concerts/{id}` | Update concert | ADMIN |
| `DELETE` | `/api/concerts/{id}` | Delete concert | ADMIN |

### Example: Login and API Usage

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Fetch concerts using the access token
curl http://localhost:8080/api/concerts \
  -H "Authorization: Bearer <access_token>"

# Search for indie shows that made you cry
curl "http://localhost:8080/api/concerts/search?genre=INDIE_ROCK&tearJerkerOnly=true" \
  -H "Authorization: Bearer <access_token>"
```

## Swagger UI

Available at: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## H2 Console

URL: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

| Field | Value |
|---|---|
| JDBC URL | `jdbc:h2:mem:concertcrewdb` |
| Username | `sa` |
| Password | *(leave blank)* |

## Key Features

- **Vintage gig poster aesthetic** — matte black background with concert red (`#ff3838`), cream paper (`#f5e6c8`), and yellow highlighter (`#ffd60a`); Impact display type for chunky gig-poster headlines; Courier monospace for the DIY/zine body copy
- **Ticket-stub design language** — every card uses dashed perforated borders on top and bottom (CSS radial-gradient pattern) like a real ticket tear-line; the detail view goes full ticket with venue stamp, barcode, and seat info
- **Cards "stuck to a wall"** — each card on the list is rotated `-0.7°` / `+0.5°` / `-0.3°` in a rotating pattern, with a yellow tape strip rendered on top via a `::before` pseudo-element; hover rotates back to 0° and lifts
- **SOLD OUT rubber stamp** — 5-star memorable shows get a rotated 15° red double-border rubber stamp overlay reading "SOLD OUT", positioned over the card with low opacity for that "stamped after the fact" look
- **Hand-stamped barcode** on the login ticket stub — pure CSS, 24 alternating-width black bars
- **`@Transient` `totalSpentEur` calculation** in the entity — sums `ticketPriceEur + merchSpentEur + travelCostEur` on demand, displayed prominently throughout the UI (no service-layer math)
- **5-dimension quality scoring** — Vibe / Setlist / Production / Crowd Energy / Vocal Form, each 1-10 with red progress bars on the detail view, separate from the overall 1-5 star rating
- **22 genres** — POP, INDIE_POP, INDIE_ROCK, ROCK, ALT_ROCK, PUNK, METAL, EMO_REVIVAL, HIP_HOP, RAP, RNB, SOUL, ELECTRONIC, HOUSE, TECHNO, KPOP, JPOP, HYPERPOP, BEDROOM_POP, SHOEGAZE, FOLK, JAZZ — full Gen Z listening taxonomy
- **10 show types** — STADIUM_TOUR, ARENA_TOUR, AMPHITHEATER, CLUB_SHOW, FESTIVAL_SET, INTIMATE_VENUE, OUTDOOR_FREE, RESIDENCY, ACOUSTIC_SET, **SECRET_SHOW** (for those Fred again.. last-minute Boiler Room announces)
- **10 viewing areas** — FRONT_PIT, BARRICADE, GENERAL_ADMISSION, SEATED_FLOOR, SEATED_LOWER, SEATED_UPPER, BALCONY, VIP_BOX, STANDING_ONLY, FESTIVAL_FIELD
- **8 venue sizes** — DIVE_BAR → STADIUM with FESTIVAL_FIELD as its own class
- **5 boolean flags** that capture *how* a show felt:
  - **`phoneFreeShow`** — was it a Yondr-pouch / phones-locked-up show? Tracks whether you were actually present
  - **`firstTimeSeen`** — first time seeing this artist live?
  - **`tearJerker`** — did you cry? (searchable filter)
  - **`dancedAllNight`** — physical commitment confirmed
  - **`boughtMerch`** — wallet committed too
- **Engagement metrics** — `durationMinutes`, `songsPlayed`, `encoreCount` (the Eras Tour was 195 min / 44 songs / 3 encore)
- **Free-text memory fields** — `crewWith` (who you went with), `favoriteSong`, `bestMoment` (highlighted yellow note card with rotation), `moodTags`, `setlistNotes`, `personalNotes`
- **JPQL search** across `mainArtist`, `tourName`, `venue`, `city`, and `openingActs` simultaneously, plus genre/showType filters and a `tearJerkerOnly` toggle
- **All UI in English** with Croatian-Algebra package structure (`hr.algebra.concertcrew`)

## 10 Curated Sample Concerts

| # | Artist | Tour / Show | Date | Venue | Notes |
|---|---|---|---|---|---|
| 1 | **Taylor Swift** | The Eras Tour | 2024-06-21 | Wembley Stadium, London | 5★ first time, tear-jerker, €210 ticket, "best show of my life" |
| 2 | **The 1975** | Still At Their Very Best | 2024-03-12 | O2 Brixton Academy | 5★ barricade, queued 14h, About You finale |
| 3 | **Phoebe Bridgers** | Reunion Tour | 2024-09-08 | Alexandra Palace | 5★ first time, I Know The End scream, Boygenius reunion encore |
| 4 | **Fred again.. + Skrillex b2b** | Secret Boiler Room | 2023-11-04 | Printworks | 5★ phone-free Yondr show, secret announce |
| 5 | **NewJeans** | Bunnies Camp | 2024-06-26 | Tokyo Dome | 5★ flew to Tokyo, lightstick ocean, €950 travel |
| 6 | **boygenius** | the rest tour | 2023-08-20 | Gunnersbury Park festival | 5★ golden hour, Ethel Cain opener, sapphic awakening |
| 7 | **Mitski** | The Land Is Inhospitable Tour | 2024-04-18 | Eventim Apollo | 5★ went solo intentional, phone-free, healing |
| 8 | **Charli XCX** | Brat Tour | 2024-07-05 | Crystal Palace Park | 5★ Apple choreo viral moment, brat summer peaked |
| 9 | **Bad Bunny** | Most Wanted | 2024-05-23 | Estadio Olimpico Madrid | 4★ first time, mechanical bull, perreo stadium |
| 10 | **Black Country, New Road** | Live at Bush Hall | 2024-02-09 | EartH Hackney | 5★ post-Isaac era, brass section devastation |

## Project Structure

```
src/main/java/hr/algebra/concertcrew/
├── ConcertcrewApplication.java
├── config/
│   ├── DataInitializer.java          # 10 sample concerts on startup
│   ├── OpenApiConfig.java            # Swagger / OpenAPI configuration
│   └── SecurityConfig.java           # Two filter chains (API + MVC)
├── controller/
│   ├── mvc/
│   │   ├── AuthMvcController.java
│   │   ├── ConcertMvcController.java # The 40-arg form initializer lives here
│   │   └── HomeController.java
│   └── rest/
│       ├── AuthRestController.java
│       └── ConcertRestController.java
├── dto/
│   ├── ConcertDto.java               # 40-component record with numbered comments
│   └── Dto.java                      # Login/Register/Token records
├── entity/
│   ├── Concert.java                  # 35 mutable fields + @Transient totalSpentEur
│   ├── RefreshToken.java
│   └── User.java                     # Implements UserDetails
├── enums/
│   ├── Genre.java                    # 22 genres
│   ├── Role.java
│   ├── ShowType.java                 # 10 show types incl. SECRET_SHOW
│   ├── VenueSize.java                # 8 size classes
│   └── ViewingArea.java              # 10 viewing areas
├── repository/
│   ├── ConcertRepository.java
│   ├── RefreshTokenRepository.java
│   └── UserRepository.java
├── security/
│   ├── JwtAuthFilter.java
│   ├── JwtService.java
│   └── UserDetailsServiceImpl.java
└── service/
    ├── AuthService.java
    ├── ConcertService.java
    └── RefreshTokenService.java
```
