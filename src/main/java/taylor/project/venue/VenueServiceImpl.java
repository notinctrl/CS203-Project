package taylor.project.venue;

import java.util.List;

import org.springframework.stereotype.Service;


@Service
public class VenueServiceImpl implements VenueService {
   
    private VenueRepository venues;
    

    public VenueServiceImpl(VenueRepository venues){
        this.venues = venues;
    }

    @Override
    public List<Venue> listVenues() {
        return venues.findAll();
    }

    
    @Override
    public Venue getVenueById(Long id){
        return venues.findById(id).orElse(null);
    }
    
    @Override
    public List<Venue> getVenuesByName(String VenueName) {
        return venues.findByVenueNameContainingIgnoreCase(VenueName);
    }

    @Override
    public Venue addVenue(Venue Venue) {
        return venues.save(Venue);
    }
    
    @Override
    public Venue updateVenue(Long id, Venue newVenueInfo){
        return venues.findById(id).map(venue -> {venue.setVenueName(newVenueInfo.getVenueName());
                                                    venue.setVenueSize(newVenueInfo.getVenueSize());
                                                    venue.setVenueOverview(newVenueInfo.getVenueOverview());
                                                    venue.setSectors(newVenueInfo.getSectors());
            return venues.save(venue);
    }).orElse(null);

    }

    /**
     * Remove a Venue with the given id
     * Spring Data JPA does not return a value for delete operation
     * Cascading: removing a Venue will also remove all its associated reviews
     */
    @Override
    public void deleteVenue(Long id){
        venues.deleteById(id);
    }
}