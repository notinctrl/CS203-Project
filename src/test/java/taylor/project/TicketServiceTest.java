package taylor.project;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import taylor.project.concert.Concert;
import taylor.project.concert.ConcertService;
import taylor.project.concert.ConcertServiceImpl;
import taylor.project.sector.Sector;
import taylor.project.ticket.Ticket;
import taylor.project.ticket.TicketRepository;
import taylor.project.ticket.TicketServiceImpl;
import taylor.project.user.UserRepository;
import taylor.project.venue.Venue;
import taylor.project.user.User;


@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {
    
    @Mock
    private ConcertService concertService;

    @Mock
    private TicketRepository tickets;

    @Mock
    private UserRepository users;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    void changeTicketStatusToPending_GeneralStandingSector_SeatsLeftException() {
        // Arrange
        Long concertId = 1L;
        Sector generalStandingSector = new Sector();
        generalStandingSector.setSeatsLeft(10.0);
        List<String> selectedSeats = Collections.singletonList("3");
        Long userId = 123L;

        when(concertService.getConcertById(concertId)).thenReturn(new Concert());
        when(tickets.findTicketsByConcertAndSectorName(any(), anyString())).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            ticketService.changeTicketStatusToPendingGeneral(concertId, generalStandingSector, selectedSeats, userId);
        });
    }

    @Test
    void updateTicketToPending_ValidTicketStatusChange() {
        // Arrange
        Ticket ticket = new Ticket();
        ticket.setBoughtUser(null);
        ticket.setCartedUser(null);
        ticket.setTicketStatus('A');
        Long userId = 123L;

        User user = new User();
        when(users.findById(userId)).thenReturn(Optional.of(user));

        // Act
        ticketService.updateTicketToPending(ticket, userId);

        // Assert
        // Verify that the save method is called once
        verify(tickets, times(1)).save(any(Ticket.class));

        // Verify the final state
        assertEquals('P', ticket.getTicketStatus());
        assertEquals(user, ticket.getCartedUser());
        assertTrue(user.getShoppingCart().contains(ticket));
    }

    @Test
    void updateTicketToPending_InvalidTicketStatusChange() {
        // Arrange
        Ticket ticket = new Ticket();
        ticket.setBoughtUser(new User()); // Set to a non-null value to make it invalid
        ticket.setCartedUser(null);
        ticket.setTicketStatus('U');
        Long userId = 123L;

        // Act
        assertAll(
            () -> assertThrows(RuntimeException.class, () -> ticketService.updateTicketToPending(ticket, userId)),
            () -> assertEquals('U', ticket.getTicketStatus()) // Additional assertion
        );
    }
    

}