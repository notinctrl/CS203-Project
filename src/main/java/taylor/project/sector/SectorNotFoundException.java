package taylor.project.sector;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SectorNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public SectorNotFoundException(String id) {
        super("Could not find sector with id " + id);
    }
}
