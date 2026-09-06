package hr.algebra.concertcrew.service;

import hr.algebra.concertcrew.dto.ConcertDto;
import hr.algebra.concertcrew.entity.Concert;
import hr.algebra.concertcrew.entity.User;
import hr.algebra.concertcrew.enums.Genre;
import hr.algebra.concertcrew.enums.ShowType;
import hr.algebra.concertcrew.repository.ConcertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ConcertService {

    private final ConcertRepository concertRepository;

    public ConcertService(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    public List<ConcertDto> findAll() {
        return concertRepository.findAllByOrderByDateAttendedDescRatingDescMainArtistAsc()
            .stream()
            .map(ConcertDto::from)
            .toList();
    }

    public ConcertDto findById(Long id) {
        return concertRepository.findById(id)
            .map(ConcertDto::from)
            .orElseThrow(() -> new NoSuchElementException("Concert not found: " + id));
    }

    public List<ConcertDto> search(String query, Genre genre, ShowType showType, boolean tearJerkerOnly) {
        String nq = (query != null && query.isBlank()) ? null : query;
        return concertRepository.search(nq, genre, showType, tearJerkerOnly)
            .stream()
            .map(ConcertDto::from)
            .toList();
    }

    @Transactional
    public ConcertDto create(ConcertDto dto, User creator) {
        Concert concert = new Concert();
        dto.applyTo(concert);
        concert.setAddedBy(creator);
        return ConcertDto.from(concertRepository.save(concert));
    }

    @Transactional
    public ConcertDto update(Long id, ConcertDto dto) {
        Concert concert = concertRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Concert not found: " + id));
        dto.applyTo(concert);
        return ConcertDto.from(concertRepository.save(concert));
    }

    @Transactional
    public void delete(Long id) {
        if (!concertRepository.existsById(id)) {
            throw new NoSuchElementException("Concert not found: " + id);
        }
        concertRepository.deleteById(id);
    }
}
