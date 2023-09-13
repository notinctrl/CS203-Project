package project.concert;

import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class ConcertServiceImpl implements ConcertService {
   
    private ConcertRepository books;
    

    public ConcertServiceImpl(ConcertRepository books){
        this.books = books;
    }

    @Override
    public List<Concert> listBooks() {
        return books.findAll();
    }

    
    @Override
    public Concert getBook(Long id){
        return books.findById(id).orElse(null);
    }
    
    @Override
    public Concert addBook(Concert book) {
        return books.save(book);
    }
    
    @Override
    public Concert updateBook(Long id, Concert newBookInfo){
        return books.findById(id).map(book -> {book.setTitle(newBookInfo.getTitle());
            return books.save(book);
        }).orElse(null);

    }

    /**
     * Remove a book with the given id
     * Spring Data JPA does not return a value for delete operation
     * Cascading: removing a book will also remove all its associated reviews
     */
    @Override
    public void deleteBook(Long id){
        books.deleteById(id);
    }
}