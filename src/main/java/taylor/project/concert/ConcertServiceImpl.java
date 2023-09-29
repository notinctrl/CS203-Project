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
    public Concert getConcertById(Long id){
        return concerts.findById(id).orElse(null);
    }
    
    @Override
    public List<Concert> getConcertsByName(String concertName) {
        return concerts.findByConcertNameContainingIgnoreCase(concertName);
    }

    @Override
    public Concert addConcert(Concert concert) {
        return concerts.save(concert);
    }
    
    @Override
    public Concert updateConcert(Long id, Concert newConcertInfo){
        return concerts.findById(id).map(concert -> {concert.setConcertName(newConcertInfo.getConcertName());
                                                    concert.setTicketQuantity(newConcertInfo.getTicketQuantity());
                                                    concert.setSoldOut(newConcertInfo.isSoldOut());
                                                    concert.setDateRange(newConcertInfo.getDateRange());
                                                    concert.setStartTime(newConcertInfo.getStartTime());
                                                    concert.setConcertVenue(newConcertInfo.getConcertVenue());
                                                    concert.setPhoto(newConcertInfo.getPhoto());
                                                    
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