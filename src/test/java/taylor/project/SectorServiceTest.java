package taylor.project;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import taylor.project.concert.Concert;
import taylor.project.concert.ConcertService;
import taylor.project.sector.*;
import taylor.project.ticket.*;
import taylor.project.user.UserRepository;
import taylor.project.venue.Venue;


@ExtendWith(MockitoExtension.class)
public class SectorServiceTest {
    @Mock
    private ConcertService concertService;

    @Mock
    private TicketService ticketService;

    @Mock
    private SectorRepository sectors;

    @Mock
    private UserRepository users;

    @InjectMocks
    private SectorServiceImpl sectorService;

    @Test
    void updateSectorSeatsToPending_ValidScenario() {
        // Arrange
        Long concertId = 1L;
        String sectorName = "SomeSector";
        List<String> selectedSeats = Arrays.asList("A:1", "B:2");
        
        Concert concert = new Concert();
        Venue venue = new Venue();
        Sector sector = new Sector();

        sector.setRowNames(new ArrayList<>(List.of("A", "B")));
        sector.setGeneralStanding(false);
        sector.setSeats(new ArrayList<>(List.of("AA", "AA")));
        sector.setSectorName(sectorName);
        venue.setSectors(List.of(sector));
        venue.setConcert(concert);
        concert.setConcertVenue(venue);
        
        when(concertService.getConcertById(concertId)).thenReturn(concert);
        when(sectors.save(any())).thenReturn(sector); // Mock the save method
        
        // Act
        sectorService.updateSectorSeatsToPending(concertId, sectorName, selectedSeats);

        // Assert
        verify(sectors, times(1)).save(any(Sector.class));
    }

    @Test
    void updateSeatToPending_SeatIsAvailable_ValidScenario() {
        // Arrange
        String seatString = "AAAAAA";
        String seatNumStringed = "1";
        
        // Act
        String result = sectorService.updateSeatToPending(seatString, seatNumStringed);

        // Assert
        assertEquals("PAAAAA", result);
    }

    @Test
    void updateSeatToPending_SeatIsUnavailable_InvalidScenario() {
        // Arrange
        String seatString = "UAAAAA";
        String seatNumStringed = "1";
        
        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sectorService.updateSeatToPending(seatString, seatNumStringed);
        });
        // Assert
        assertEquals("Illegal seat status change: from U to P", exception.getMessage());
    }

    @Test
    void updateGeneralStandingToPending_ValidScenario() {
        // Arrange
        Sector sector = new Sector();
        sector.setSeatsLeft(10.0);
        List<String> selectedSeats = Collections.singletonList("3");
        
        // Act
        Sector result = sectorService.updateGeneralStandingToPending(sector, selectedSeats);

        // Assert
        assertEquals(7.0, result.getSeatsLeft());
    }

    @Test
    void updateGeneralStandingToPending_NotEnoughSeats_InvalidScenario() {
        // Arrange
        Sector sector = new Sector();
        sector.setSeatsLeft(2.0);
        List<String> selectedSeats = Collections.singletonList("3");

        
        // Act and Assert
        assertAll(
            () -> assertThrows(RuntimeException.class, () -> sectorService.updateGeneralStandingToPending(sector, selectedSeats))
        );
    }

    @Test
    void updateSectorSeatsToUnavail_ValidScenario() {
        // Arrange
        Long ticketId = 1L;
        Long userId = 123L;
    
        Concert concert = new Concert();
        Venue venue = new Venue();
        Sector sector = new Sector();
        sector.setSectorName("SomeSector");
        sector.setGeneralStanding(false);
        List<String> rowNames = Arrays.asList("A", "B");
        List<String> seats = Arrays.asList("PAAAA", "AAAAA");
        sector.setRowNames(rowNames);
        sector.setSeats(seats);
    
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setConcert(concert);
        ticket.setSectorName("SomeSector");
        ticket.setSeatRowName("A");
        ticket.setSeatNo(1);
        ticket.setTicketStatus('P');
    
        // Ensure that concert has a valid venue
        venue.setSectors(Collections.singletonList(sector));
        concert.setConcertVenue(venue);
    
        when(ticketService.getTicketById(ticketId)).thenReturn(ticket);
    
        // Act
        sectorService.updateSectorSeatsToUnavail(ticketId, userId);
    
        // Assert
        assertEquals("UAAAA", sector.getSeats().get(0)); // Assuming seat 1 in row A is changed to Unavailable
    }

    @Test
    void updateSectorSeatsToUnavail_SeatWasAvailable_InvalidScenario() {
        // Arrange
        Long ticketId = 1L;
        Long userId = 123L;
    
        Concert concert = new Concert();
        Venue venue = new Venue();
        Sector sector = new Sector();
        sector.setSectorName("SomeSector");
        sector.setGeneralStanding(false);
        List<String> rowNames = Arrays.asList("A", "B");
        List<String> seats = Arrays.asList("AAAAA", "AAAAA");
        sector.setRowNames(rowNames);
        sector.setSeats(seats);
    
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setConcert(concert);
        ticket.setSectorName("SomeSector");
        ticket.setSeatRowName("A");
        ticket.setSeatNo(1);
        ticket.setTicketStatus('P');
    
        // Ensure that concert has a valid venue
        venue.setSectors(Collections.singletonList(sector));
        concert.setConcertVenue(venue);
    
        when(ticketService.getTicketById(ticketId)).thenReturn(ticket);
    
        // Act and Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sectorService.updateSectorSeatsToUnavail(ticketId, userId);
        });

        // Assert
        assertEquals("Illegal status change: from A to U", exception.getMessage());
        assertEquals("AAAAA", sector.getSeats().get(0)); // Assuming seat 1 in row A remains as Pending
        }
    
}


