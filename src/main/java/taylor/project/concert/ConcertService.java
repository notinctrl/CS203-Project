package taylor.project.concert;

import java.util.List;

public interface ConcertService {
    List<Concert> listConcerts();
    Concert getConcertById(Long id);
    List<Concert> getConcertsByName(String concertName);
    Concert addConcert(Concert concert);
    Concert updateConcert(Long id, Concert concert);

    /**
     * Change method's signature: do not return a value for delete operation
     * @param id
     */
    void deleteConcert(Long id);
}