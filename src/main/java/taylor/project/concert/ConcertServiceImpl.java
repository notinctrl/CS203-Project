package taylor.project.concert;

import java.util.*;

import org.springframework.stereotype.Service;

import taylor.project.sector.Sector;


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

    @Override
    public List<String> getSectorRowAvailability(Long concertId, String sectorToFind){
        Optional<Concert> concert = concerts.findById(concertId);
        List<String> result = new ArrayList<>();

        // if no concert by that id was found, return null. 
        if (concert == null){
            return null;
        } else {
            // get all the sectors inside the concert's venue. if there are no sectors
            // in this concert venue, return an empty arraylist
            List<Sector> sectors = concert.get().getConcertVenue().getSectors();
            if (sectors.size() == 0) return new ArrayList<String>();

            // iterate thru the list of sectors to find the sector required
            for (Sector s : sectors){
                String currentSectorName = s.getSectorName();
                // once specified sector found, get its rowNames and
                // its seatAvailability to store in result.
                if (currentSectorName.equals(sectorToFind)){
                    List<String> seats = s.getSeats();
                    List<String> rowNames = s.getRowNames();
                    int i = 0;
                    for (String rowName : rowNames) {
                        result.add(rowName + ":" + seats.get(i++));
                    }
                }
            }
        }
        return result;
    }
}