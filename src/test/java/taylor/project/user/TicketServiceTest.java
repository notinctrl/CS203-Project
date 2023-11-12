package taylor.project.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.prepost.PreAuthorize;

import taylor.project.concert.Concert;
import taylor.project.ticket.Ticket;
import taylor.project.ticket.TicketRepository;
import taylor.project.ticket.TicketServiceImpl;
import taylor.project.venue.Venue;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository tickets;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    void addTicket_available_Success(){
        Venue venue = new Venue("s", 123, "sdfdsfsdfsd");
        Concert concert = new Concert("monstax", 23, "s", "12:12:00", venue, "sd");
        Ticket ticket = new Ticket(concert,"AA", "A1", 12, 123.0);
        List<Ticket> ticketList = new ArrayList<Ticket>();
        ticketList.add(ticket);
        //when(tickets.findTicketBySeatNo(ticket.getSeatNo())).thenReturn(ticketList);
        // when(ticket.getTicketStatus()).thenReturn();
        // assertEquals('A', ticket.getTicketStatus());

        when(tickets.save(any(Ticket.class))).thenReturn(ticket);

        Ticket savedTicket = ticketService.addTicket(ticket);
        
        assertNotNull(savedTicket);

        // verify(users).findByUsername(user.getUsername());
        verify(tickets).save(ticket);
        
        //verify(tickets).findTicketBySeatNo(ticket.getSeatNo());

    }

    @Test
    void addTicket_Pending_Success(){
        
    }

}
