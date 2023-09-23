package taylor.project.venue;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VenueNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public VenueNotFoundException(Long id) {
        super("Could not find venue with id " + id);
    }
    
    public VenueNotFoundException(String venueName) {
        super("Could not find venues with " + venueName);
    }
}
