package taylor.project.ticket;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TicketNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public TicketNotFoundException(Long id) {
        super("Could not find ticket with id " + id);
    }
    
}
