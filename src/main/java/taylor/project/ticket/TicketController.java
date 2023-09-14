package taylor.project.ticket;

import java.util.List;

import javax.validation.Valid;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketController {
    private TicketService ticketService;

    public TicketController(TicketService ts){
        this.ticketService = ts;
    }

    /**
     * List all tickets in the system that belongs to the user
     * @return list of all tickets
     */
    @GetMapping("/tickets")
    public List<Ticket> getTickets(){
        return ticketService.listTickets();
    }

    /**
     * Search for ticket with the given id
     * If there is no ticket with the given "id", throw a ticketNotFoundException
     * @param id
     * @return ticket with the given id
     */
    @GetMapping("/tickets/{id}")
    public Ticket getTicket(@PathVariable Long id){
        Ticket ticket = ticketService.getTicket(id);

        // Need to handle "ticket not found" error using proper HTTP status code
        // In this case it should be HTTP 404
        if(ticket == null) throw new TicketNotFoundException(id);
        return ticketService.getTicket(id);

    }
    /**
     * Add a new ticket with POST request to "/tickets"
     * Note the use of @RequestBody
     * @param ticket
     * @return list of all tickets
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/tickets")
    public Ticket addticket(@Valid @RequestBody Ticket ticket) {
        return ticketService.addTicket(ticket);
    }

    /**
     * If there is no ticket with the given "id", throw a ticketNotFoundException
     * @param id
     * @param newticketInfo
     * @return the updated, or newly added ticket
     */
    @PutMapping("/tickets/{id}")
    public Ticket updateticket(@PathVariable Long id, @Valid @RequestBody Ticket newticketInfo){
        Ticket ticket = ticketService.updateTicket(id, newticketInfo);
        if(ticket == null) throw new TicketNotFoundException(id);
        
        return ticket;
    }

    /**
     * Remove a ticket with the DELETE request to "/tickets/{id}"
     * If there is no ticket with the given "id", throw a ticketNotFoundException
     * @param id
     */
    @DeleteMapping("/tickets/{id}")
    public void deleteTicket(@PathVariable Long id){
        try{
            ticketService.deleteTicket(id);
         }catch(EmptyResultDataAccessException e) {
            throw new TicketNotFoundException(id);
         }
    }
}