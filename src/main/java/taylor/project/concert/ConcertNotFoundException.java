package taylor.project.concert;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ConcertNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public ConcertNotFoundException(Long id) {
        super("Could not find concert with id " + id);
    }
    
    public ConcertNotFoundException(String concertName) {
        super("Could not find concerts with " + concertName);
    }
}
