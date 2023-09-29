package taylor.project.sector.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import taylor.project.sector.Sector;

@ResponseStatus(HttpStatus.NOT_FOUND)

public class SectorExistsException extends RuntimeException{

    private static final long serialVersionUID = 2L;

    public SectorExistsException(Sector s) {
        super("Sector with name " + s.getSectorName() + " in " + s.getVenue().getVenueName() + " already exists.");
    }
}