package project.concert;

import java.util.List;

public interface ConcertService {
    List<Concert> listConcerts();
    Concert getConcert(Long id);
    Concert addConcert(Concert concert);
    Concert updateConcert(Long id, Concert concert);

    /**
     * Change method's signature: do not return a value for delete operation
     * @param id
     */
    void deleteConcert(Long id);
}