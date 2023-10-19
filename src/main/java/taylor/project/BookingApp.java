package taylor.project;

import java.util.*;
import java.time.LocalDateTime;

import java.time.LocalDate;
import java.io.*;
import java.nio.file.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import taylor.project.concert.*;
import taylor.project.user.*;
import taylor.project.client.RestTemplateClient;
import taylor.project.shoppingCart.*;
import taylor.project.venue.*;
import taylor.project.sector.*;
import taylor.project.ticket.*;
import taylor.project.sector.exceptions.SectorExistsException;

@SpringBootApplication
@ComponentScan({"taylor.project","taylor.project.fileupload"})
public class BookingApp {

	public static void main(String[] args) throws IOException{

		ApplicationContext ctx = SpringApplication.run(BookingApp.class, args);

        // JPA concert repository init. default settings
        ConcertRepository concerts = ctx.getBean(ConcertRepository.class);
        VenueRepository venues = ctx.getBean(VenueRepository.class);
        TicketRepository tickets = ctx.getBean(TicketRepository.class);
        // refer to below psvm for initialised concerts.
        List<Venue> vList = iniVenues(ctx, venues, tickets);
        List<Concert> cList = iniConcerts(vList);
        System.out.println("[Add concert]: " + concerts.save(cList.get(0)).getConcertName());

                // tester for sector seats and information is being added to concert successfully.
                // + " and Sector " +  cList.get(0).getConcertVenue().getSectors().get(0).getSectorName()
                // + ", row " + cList.get(0).getConcertVenue().getSectors().get(0).getRowNames().get(0) 
                // + " has the following seats available = " + cList.get(0).getConcertVenue().getSectors().get(0).getSeats().get(0));
        System.out.println("[Add concert]: " + concerts.save(cList.get(1)).getConcertName());
        System.out.println("[Add concert]: " + concerts.save(cList.get(2)).getConcertName());

        for (Venue v : vList){
            venues.save(v);
        }

        // JPA user repository init
        UserRepository users = ctx.getBean(UserRepository.class);
        // refer to below psvm for initialised users.
        List<User> uList = iniUsers(ctx);
        System.out.println("[Add user]: " + users.save(uList.get(0)).getUsername());
        System.out.println("[Add user]: " + users.save(uList.get(1)).getUsername() 
                                        + " with email " + uList.get(1).getEmailAddress());
        // System.out.println("[Add user]: " + users.save(uList.get(2)).getUsername());    
        
        // JPA shopping cart repository init. default settings
        ShoppingCartRepository shoppingCarts = ctx.getBean(ShoppingCartRepository.class);
        ShoppingCart testShoppingCart1 = new ShoppingCart((long) 1);
        ShoppingCart testShoppingCart2 = new ShoppingCart((long) 324);

        System.out.println("[Add shopping cart]: User ID = " + shoppingCarts.save(testShoppingCart1).getUserID());
        System.out.println("[Add shopping cart]: User ID = " + shoppingCarts.save(testShoppingCart2).getUserID());

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

        result.add(new Concert("Red Hot Chili Peppers 2024", 10000,
                            "9 - 11 April, 2025", "19:00", vList.get(1),
                             null));
        result.add(new Concert("BTS Singapore 2024", 20000,
                            "21 - 22 September, 2024", "20:00", vList.get(2), 
                            null));

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
        result.add(new User("admin", encoder.encode("goodpassword"), "19-03-2003" ,
            "hello123@gmail.com" ,"1234", "ROLE_ADMIN"));
        return result;
    }

    public static List<Venue> iniVenues(ApplicationContext ctx, VenueRepository venues, TicketRepository tickets){
        
        Venue v1 = new Venue("Singapore National Stadium", 10000,"src/main/resources/static/seating_plan/Taylor_Swift_Seating_Plan.jpg");
        Venue v2 = new Venue("Singapore Indoor Stadium", 10000, "src/main/resources/static/seating_plan/Charlie_Puth_Seating_Plan.jpg");
        Venue v3 = new Venue("Esplanade Theatre A", 10000, "src/main/resources/static/seating_plan/Charlie_Puth_Seating_Plan.jpg");
        List<Venue> result = new ArrayList<>(List.of(v1, v2, v3));

        Sector newSect1 = new Sector(v1, "634", 348.0, new String[]{"A","B","C","D"}, new Integer[]{18,18,18,18}, "src/main/resources/static/seating_plan/sector_seating.png");
        for(int i = 0; i < newSect1.getRowNames().size(); i++) {
            String rowName = newSect1.getRowNames().get(i);
            String seats = newSect1.getSeats().get(i);

            for(int seatNo = 1; seatNo <= seats.length(); seatNo++) {
                System.out.println("Added ticket:" + tickets.save(new Ticket(newSect1.getSectorName(), rowName, seatNo, newSect1.getTicketPrice())));
            }
        }
        Sector newSect1a = new Sector(v1, "635", 348.0, new String[]{"D", "E", "F"}, new Integer[]{50,50,50}, "src/main/resources/static/seating_plan/sector_seating.png");
        Sector newSect2 = new Sector(v2, "634", 348.0, new String[]{"A"}, new Integer[]{20}, "src/main/resources/static/seating_plan/sector_seating.png");
        Sector newSect3 = new Sector(v3, "634", 348.0, new String[]{"A"}, new Integer[]{20}, "src/main/resources/static/seating_plan/sector_seating.png");
        List<Sector> newSects = new ArrayList<>(List.of(newSect1, newSect1a, newSect2, newSect3));

        for (Venue v : result){
            ArrayList<Sector> vSectors = new ArrayList<>();
            for (Sector s : newSects){
                if (s.getVenue().equals(v)) vSectors.add(s);
            }
            v.setSectors(vSectors);
        }
        return result;
    }
}


        // System.out.println("[Add concert]: " + concerts.save(new Concert("Coldplay Music of the Spheres 2024", 20000,
        //                     "23 - 27 January, 2024", "20:00", "Singapore National Stadium", 
        //                     "src/main/resources/static/concert_posters/Coldplay_Concert_Poster.jpg")).getConcertName());
