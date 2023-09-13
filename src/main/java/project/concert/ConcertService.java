package project.concert;

import java.util.List;

public interface ConcertService {
    List<Concert> listBooks();
    Concert getBook(Long id);
    Concert addBook(Concert book);
    Concert updateBook(Long id, Concert book);

    /**
     * Change method's signature: do not return a value for delete operation
     * @param id
     */
    void deleteBook(Long id);
}