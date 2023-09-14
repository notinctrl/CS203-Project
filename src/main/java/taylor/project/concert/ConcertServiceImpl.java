package taylor.project.concert;

import java.util.List;

import org.springframework.stereotype.Service;


@Service
public class ConcertServiceImpl implements ConcertService {
   
    private ConcertRepository concerts;
    

    public ConcertServiceImpl(ConcertRepository concerts){
        this.concerts = concerts;
    }

    @Override
    public List<Concert> listConcerts() {
        return concerts.findAll();
    }

    
    @Override
    public Concert getConcert(Long id){
        return concerts.findById(id).orElse(null);
    }
    
    @Override
    public Concert addConcert(Concert concert) {
        return concerts.save(concert);
    }
    
    @Override
    public Concert updateConcert(Long id, Concert newConcertInfo){
        return concerts.findById(id).map(concert -> {concert.setConcertName(newConcertInfo.getConcertName());
                                                    concert.setTicketQuantity(newConcertInfo.getTicketQuantity());
            return concerts.save(concert);
    }).orElse(null);

    }

    /**
     * Remove a concert with the given id
     * Spring Data JPA does not return a value for delete operation
     * Cascading: removing a concert will also remove all its associated reviews
     */
    @Override
    public void deleteConcert(Long id){
        concerts.deleteById(id);
    }
}