package hr.algebra.concertcrew.repository;

import hr.algebra.concertcrew.entity.Concert;
import hr.algebra.concertcrew.enums.Genre;
import hr.algebra.concertcrew.enums.ShowType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

    @Query("""
        SELECT c FROM Concert c
        WHERE (:query IS NULL OR LOWER(c.mainArtist)   LIKE LOWER(CONCAT('%', :query, '%'))
                              OR LOWER(c.tourName)     LIKE LOWER(CONCAT('%', :query, '%'))
                              OR LOWER(c.venue)        LIKE LOWER(CONCAT('%', :query, '%'))
                              OR LOWER(c.city)         LIKE LOWER(CONCAT('%', :query, '%'))
                              OR LOWER(c.openingActs)  LIKE LOWER(CONCAT('%', :query, '%')))
          AND (:genre IS NULL OR c.genre = :genre OR c.secondaryGenre = :genre)
          AND (:showType IS NULL OR c.showType = :showType)
          AND (:tearJerkerOnly = false OR c.tearJerker = true)
        ORDER BY c.dateAttended DESC, c.rating DESC NULLS LAST, c.mainArtist ASC
        """)
    List<Concert> search(
        @Param("query") String query,
        @Param("genre") Genre genre,
        @Param("showType") ShowType showType,
        @Param("tearJerkerOnly") boolean tearJerkerOnly
    );

    List<Concert> findAllByOrderByDateAttendedDescRatingDescMainArtistAsc();
}
