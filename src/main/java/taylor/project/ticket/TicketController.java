package taylor.project.ticket;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import taylor.project.concert.*;

@RestController
public class TicketController {
    private TicketService ticketService;
    private ConcertService concertService;

    public TicketController(TicketService ts, ConcertService cs){
        this.ticketService = ts;
        concertService = cs;
    }

    /**
     * List all tickets in the system that belongs to the user
     * @return list of all tickets
     */
    @GetMapping("/tickets")
    public List<Ticket> getTickets(){
        return ticketService.listTickets();
    }

    @GetMapping("/tickets/{Id}")
    public Ticket getTicketById(@PathVariable Long ticketId){
        return ticketService.getTicketById(ticketId);
    }

    // @GetMapping("/tickets/{seatNumber}")
    // public Ticket getTicketbySeatNumber(@PathVariable int seatNumber){
    //     Ticket ticket = ticketService.getTicketbySeatNumber(seatNumber);

    //     // Need to handle "ticket not found" error using proper HTTP status code
    //     // In this case it should be HTTP 404
    //     if(ticket == null) throw new TicketNotFoundException(seatNumber);
    //     return ticketService.getTicketbySeatNumber(seatNumber);

    // }

    @GetMapping("/tickets/{seatNumber}")
    public List<Ticket> getTicketbySeatNumber(@PathVariable int seatNumber){
        return ticketService.getTicketListbySeatNo(seatNumber);

    }

    

    /**
     * Search for ticket with the given id
     * If there is no ticket with the given "id", throw a ticketNotFoundException
     * @param id
     * @return ticket with the given id
     */
    // @GetMapping("/tickets/{id}")
    // public Ticket getTicket(@PathVariable Long id){
    //     Ticket ticket = ticketService.getTicket(id);

    //     // Need to handle "ticket not found" error using proper HTTP status code
    //     // In this case it should be HTTP 404
    //     if(ticket == null) throw new TicketNotFoundException(id);
    //     return ticketService.getTicket(id);

    // }
    
    @GetMapping("/tickets/{cId}/{sectName}/{rowName}/{seatNo}")
    public Optional<Ticket> findSpecificTicket(@PathVariable("cId") Long concertId, @PathVariable("sectName") String sectName,
                                                 @PathVariable("rowName") String rowName, @PathVariable("seatNo") Integer seatNo){
        Concert c = concertService.getConcertById(concertId);
        return ticketService.findSpecificTicket(c, sectName, rowName, seatNo);
    }
     
    

    /*
     * @param id
     */
    @GetMapping("tickets/{id}/status")
    public Character getTicketStatus(@PathVariable Long id) {
        Character ticket = ticketService.getTicketStatus(id);
        if(ticket == null) throw new TicketNotFoundException(id);
        return ticketService.getTicketStatus(id);
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
    @PutMapping("/tickets/{id}/{newticketInfo}")
    public Ticket updateticket(@PathVariable Long id, @Valid @RequestBody Ticket newticketInfo){
        Ticket ticket = ticketService.updateTicket(id, newticketInfo);
        if(ticket == null) throw new TicketNotFoundException(id);
        
        return ticket;
    }

    @PutMapping("/tickets/{id}")
    public Ticket pendingStatus(@PathVariable Long id, @Valid @RequestBody Character newStatus){
        Ticket ticket = ticketService.pendingStatus(id, newStatus);
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