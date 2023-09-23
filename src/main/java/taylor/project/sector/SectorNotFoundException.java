package taylor.project.sector;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SectorNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public SectorNotFoundException(Long id) {
        super("Could not find sector with id " + id);
    }
    
    public SectorNotFoundException(String sectorName) {
        super("Could not find sectors with " + sectorName);
    }
}
