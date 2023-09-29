package taylor.project.venue;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class VenueController {
    private VenueService venueService;

    public VenueController(VenueService vs){
        this.venueService = vs;
    }

    /**
     * List all venues in the system
     * @return list of all venues
     */
    @GetMapping("/venues")
    public List<Venue> getVenues(){
        return venueService.listVenues();
    }

    /**
     * Search for venue with the given id
     * If there is no venue with the given "id", throw a venueNotFoundException
     * @param id
     * @return venue with the given id
     */
    @GetMapping("/venues/byId/{id}")
    public Venue getVenueById(@PathVariable Long id){
        Venue venue = venueService.getVenueById(id);

        // Need to handle "venue not found" error using proper HTTP status code
        // In this case it should be HTTP 404
        if(venue == null) throw new VenueNotFoundException(id);
        return venueService.getVenueById(id);

    }


    /**
     * Search for venues containing the given name
     * @param venueName
     * @return list of venues containing the given name
     */
    @GetMapping("/venues/byName/{venueName}")
    public List<Venue> getVenuesByNameContaining(@PathVariable String venueName) {
        List<Venue> venues = venueService.getVenuesByName(venueName);

        if(venues.size() == 0) throw new VenueNotFoundException(venueName);
        // Handles an empty list by throwing a HTTP 404 exception
        return venues;
    }


    /**
     * Add a new venue with POST request to "/venues"
     * Note the use of @RequestBody
     * @param venue
     * @return list of all venues
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/venues")
    public Venue addVenue(@Valid @RequestBody Venue venue) {
        return venueService.addVenue(venue);
    }

    /**
     * If there is no venue with the given "id", throw a venueNotFoundException
     * @param id
     * @param newvenueInfo
     * @return the updated, or newly added venue
     */
    @PutMapping("/venues/{id}")
    public Venue updateVenue(@PathVariable Long id, @Valid @RequestBody Venue newvenueInfo){
        Venue venue = venueService.updateVenue(id, newvenueInfo);
        if(venue == null) throw new VenueNotFoundException(id);
        
        return venue;
    }

    /**
     * Remove a venue with the DELETE request to "/venues/{id}"
     * If there is no venue with the given "id", throw a venueNotFoundException
     * @param id
     */
    @DeleteMapping("/venues/{id}")
    public void deleteVenue(@PathVariable Long id){
        try{
            venueService.deleteVenue(id);
         }catch(EmptyResultDataAccessException e) {
            throw new VenueNotFoundException(id);
         }
    }
}