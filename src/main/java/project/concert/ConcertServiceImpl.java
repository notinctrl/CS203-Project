package project.concert;

import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class ConcertServiceImpl implements ConcertService {
   
    private ConcertRepository concert;
    

    public ConcertServiceImpl(ConcertRepository concert){
        this.concert = concert;
    }

    @Override
    public List<Concert> listConcerts() {
        return concert.findAll();
    }

    
    @Override
    public Concert getConcert(Long id){
        return concert.findById(id).orElse(null);
    }
    
    @Override
    public Concert addConcert(Concert concert) {
        return concert.save(concert);
    }
    
    @Override
    public Concert updateConcert(Long id, Concert newConcertInfo){
        return concert.findById(id).map(concert -> new Concert(newConcertInfo.getConcertName(), newConcertInfo.getTicketQuantity());
            return concert.save(concert);
        ).orElse(null);

    }

    /**
     * Remove a concert with the given id
     * Spring Data JPA does not return a value for delete operation
     * Cascading: removing a concert will also remove all its associated reviews
     */
    @Override
    public void deleteConcert(Long id){
        concert.deleteById(id);
    }
}