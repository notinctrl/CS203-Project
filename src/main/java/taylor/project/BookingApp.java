package taylor.project;

import java.util.*;

import java.io.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import taylor.project.concert.*;
import taylor.project.user.*;
import taylor.project.client.RestTemplateClient;
// import taylor.project.shoppingCart.*;
import taylor.project.venue.*;
import taylor.project.sector.*;
import taylor.project.ticket.*;

@SpringBootApplication
public class BookingApp {

	public static void main(String[] args) throws IOException{

		ApplicationContext ctx = SpringApplication.run(BookingApp.class, args);

        // JPA concert repository init. default settings
        ConcertRepository concerts = ctx.getBean(ConcertRepository.class);
        VenueRepository venues = ctx.getBean(VenueRepository.class);
        TicketRepository tickets = ctx.getBean(TicketRepository.class);
        // refer to below psvm for initialised concerts.
        List<Venue> vList = iniVenues();
        List<Concert> cList = iniConcerts(vList);
        List<Ticket> tList = iniTickets(vList);
        System.out.println("[Add concert]: " + concerts.save(cList.get(0)).getConcertName());

                
        System.out.println("[Add concert]: " + concerts.save(cList.get(1)).getConcertName());
        System.out.println("[Add concert]: " + concerts.save(cList.get(2)).getConcertName());

        // venues cannot be added into repository in iniVenues.
        // as a result, i have to save them from the list.
        // same can be said for the rest of the entities
        for (Venue v : vList) venues.save(v);
        for (Ticket t : tList) tickets.save(t);

        // JPA user repository init
        UserRepository users = ctx.getBean(UserRepository.class);
        // refer to below psvm for initialised users.
        List<User> uList = iniUsers(ctx);
        System.out.println("[Add user]: " + users.save(uList.get(0)).getUsername()
                                        + " and role " + uList.get(0).getAuthorities());
        System.out.println("[Add user]: " + users.save(uList.get(1)).getUsername() 
                                        + " with email " + uList.get(1).getEmailAddress()
                                        + " and role " + uList.get(1).getAuthorities());
        System.out.println("[Add user]: " + users.save(uList.get(2)).getUsername() 
                                        + " with email " + uList.get(2).getEmailAddress()
                                        + " and role " + uList.get(2).getAuthorities());

        // Test the RestTemplate client with authentication
        /**
         * Use Rest client to test the service
         * Authentication info has been added into the RestTemplateClient.java in client subfolder
         */
        
        // RestTemplateClient client = ctx.getBean(RestTemplateClient.class);
        // System.out.println("[Add concert]: " + client.addConcert("http://localhost:8080/concerts", new Concert("Kessoku Band", 100)).getConcertName());

        // // Get the 1st concert, obtain a HTTP response and print out the name of the concert
        // System.out.println("[Get concert]: " + client.getConcertEntity("http://localhost:8080/concerts", 1L).getBody().getConcertName());
    }
    
    public static List<Concert> iniConcerts(List<Venue> vList){
        List<Concert> result = new ArrayList<>();
        result.add(new Concert("Taylor Swift Singapore 2023", 10000,
                            "2 - 4 March, 2024", "19:00", vList.get(0), 
                            "src/main/resources/static/concert_posters/Taylor_Swift_Concert_Poster.jpg"));

        result.add(new Concert("Coldplay Music of the Spheres 2024", 10000,
                            "23 - 27 January, 2024", "20:00", vList.get(1),
                             "src/main/resources/static/concert_posters/Coldplay_Concert_Poster.jpg"));
                             
        result.add(new Concert("BTS Singapore 2024", 20000,
                            "21 - 22 September, 2024", "20:00", vList.get(2), 
                            "src/main/resources/static/concert_posters/BTS_Concert_Poster.jpg"));

        for (int i = 0; i < vList.size(); i++){
             vList.get(i).setConcert(result.get(i));
        }
        return result;
    }

    public static List<User> iniUsers(ApplicationContext ctx){      
        BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);
        List<User> result = new ArrayList<>();
        result.add(new User("admin", encoder.encode("goodpassword"),"19-03-2003", 
            "dsasdgsdf@sfs.com", "dsdfsdsd", "ROLE_ADMIN"));
        result.add(new User("normaluser", encoder.encode("goodpassword"),"23-10-2001", 
            "dsasdgsdf@sfs.com", "dsdfsdsd", "ROLE_USER"));
        result.add(new User("normalbuyeruser", encoder.encode("goodpassword"),"23-10-1001", 
            "coder@gwhale.com", "dsdfsdsd", "ROLE_USER"));
        return result;
    }
    
    /**
     * Initialise all the venues and relevant sectors for use in each concert.
     * 
     * @return List<Venue> for saving onto the venue repository.
     */
    public static List<Venue> iniVenues(){
        
        Venue v1 = new Venue("Singapore National Stadium", 10000,"src/main/resources/static/seating_plan/Taylor_Swift_Seating_Plan.jpg");
        Venue v2 = new Venue("Singapore National Stadium", 10000, "src/main/resources/static/seating_plan/Coldplay_Seat_Map.jpg");
        Venue v3 = new Venue("Singapore National Stadium", 10000, "src/main/resources/static/seating_plan/BTS_Seating_Plan.png");
        List<Venue> result = new ArrayList<>(List.of(v1, v2, v3));

        Sector swiftSector1 = new Sector(v1, "634", 348.0, new String[]{"A","B","C","D"}, new Integer[]{18,18,18,18});
        Sector swiftSector2 = new Sector(v1, "635", 348.0, new String[]{"D", "E", "F"}, new Integer[]{20,20,20});
        Sector coldSect1 = new Sector(v2, "635", 128.0, new String[]{"A", "B","C","D"}, new Integer[]{20,20,20,20});
        Sector coldSect2 = new Sector(v2, "General_Standing", 168.0, new String[]{"GEN-"}, new Integer[]{300});
        Sector btsSect1 = new Sector(v3, "Purple_1", 348.0, new String[]{"PURP1-"}, new Integer[]{200});
        Sector btsSect2 = new Sector(v3, "112", 268.0, new String[]{"F", "G", "H", "I"}, new Integer[]{15,15,15,15});
        List<Sector> newSects = new ArrayList<>(List.of(swiftSector1, swiftSector2, coldSect1, coldSect2, btsSect1, btsSect2));
        
        // set the venues' sectors to 
        for (Venue v : result){
            ArrayList<Sector> vSectors = new ArrayList<>();
            for (Sector s : newSects){
                if (s.getVenue().equals(v)) vSectors.add(s);
            }
            v.setSectors(vSectors);
        }
        return result;
    }

    /**
     * Initialise tickets according to the sectors' seats. Mark them as all available, with
     * a null userid tagged to them
     * 
     */
    public static List<Ticket> iniTickets(List<Venue> venues){
        List<Ticket> result = new ArrayList<>();

        // get all sectors present in Venues 
        List<Sector> sectors = new ArrayList<>();

        // iterate through venue list to get all sectors
        for (Venue v : venues){
            sectors.addAll(v.getSectors());
        }
        // initialise the ticketRepository and fill it with tickets
        for (Sector sect : sectors){
            // ini all variables required.
            String sectName = sect.getSectorName();
            List<String> rowNames = sect.getRowNames();
            List<String> allSeats = sect.getSeats();
            Concert concert = sect.getVenue().getConcert();

            // if it is general standing sector, initialise accordingly
            if (sect.isGeneralStanding()){
                for (int i = 1; i <= sect.getSeatsLeft(); i++){
                    result.add(new Ticket(concert, sectName, sect.getRowNames().get(0), i, sect.getTicketPrice()));
                }
            } else {
                // go thru every sector's rows
                for (int i = 0; i < rowNames.size(); i++) {
                    String rowName = rowNames.get(i);
                    String seatsInRow = allSeats.get(i);

                    // go thru this specific row and create a ticket for every seat 
                    for(int seatNo = 1; seatNo <= seatsInRow.length(); seatNo++) {
                        // System.out.println("Added ticket:" + tickets.save(new Ticket(sectName, (rowName + seatNo), newSect1.getTicketPrice())));
                        result.add(new Ticket(concert, sectName, rowName,  seatNo, sect.getTicketPrice()));
                    }
                }
            }            
        }
        return result;
    }
}

