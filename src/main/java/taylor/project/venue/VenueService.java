package taylor.project.venue;

import java.util.List;

public interface VenueService {
    List<Venue> listVenues();
    Venue getVenueById(Long id);
    List<Venue> getVenuesByName(String VenueName);
    Venue addVenue(Venue Venue);
    Venue updateVenue(Long id, Venue Venue);

    /**
     * Change method's signature: do not return a value for delete operation
     * @param id
     */
    void deleteVenue(Long id);
}