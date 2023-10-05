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
import taylor.project.sector.exceptions.SectorExistsException;

@SpringBootApplication
@ComponentScan({"taylor.project","taylor.project.fileupload"})
public class BookingApp {

	public static void main(String[] args) throws IOException{

		ApplicationContext ctx = SpringApplication.run(BookingApp.class, args);

        // JPA concert repository init. default settings
        ConcertRepository concerts = ctx.getBean(ConcertRepository.class);
        VenueRepository venues = ctx.getBean(VenueRepository.class);
        // refer to below psvm for initialised concerts.
        List<Venue> vList = iniVenues(ctx, venues);
        List<Concert> cList = iniConcerts(vList);
        System.out.println("[Add concert]: " + concerts.save(cList.get(0)).getConcertName() 
                + " and Sector " +  cList.get(0).getConcertVenue().getSectors().get(0).getSectorName()
                + ", row " + cList.get(0).getConcertVenue().getSectors().get(0).getRowNames().get(0) 
                + " has the following seats available = " + cList.get(0).getConcertVenue().getSectors().get(0).getSeats().get(0));
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
        ShoppingCart testShoppingCart1 = new ShoppingCart(1, 5);
        ShoppingCart testShoppingCart2 = new ShoppingCart(324, 21);

        System.out.println("[Add shopping cart]: User ID = " + shoppingCarts.save(testShoppingCart1).getUserID() + 
                                                ", Cart ID = " + shoppingCarts.save(testShoppingCart1).getCartID());
        System.out.println("[Add shopping cart]: User ID = " + shoppingCarts.save(testShoppingCart2).getUserID() +
                                                ", Cart ID = " + shoppingCarts.save(testShoppingCart2).getCartID());

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
        vList.get(0).setConcert(result.get(0));
        vList.get(1).setConcert(result.get(1));
        vList.get(2).setConcert(result.get(2));
        return result;
    }

    public static List<User> iniUsers(ApplicationContext ctx){
        BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);
        List<User> result = new ArrayList<>();
        result.add(new User("admin", encoder.encode("goodpassword"),"19-03-2003", 
            "dsasdgsdf@sfs.com", "dsdfsdsd", "ROLE_ADMIN"));
        result.add(new User("normaluser", encoder.encode("goodpassword"),"23-10-2001", 
            "dsasdgsdf@sfs.com", "dsdfsdsd", "ROLE_USER"));
        result.add(new User("admin", encoder.encode("goodpassword"), "19-03-2003" , "hello123@gmail.com" ,"1234", "ROLE_ADMIN"));
        return result;
    }

    public static List<Venue> iniVenues(ApplicationContext ctx, VenueRepository venues){
        List<Venue> result = new ArrayList<>();
        Venue v1 = new Venue("Singapore National Stadium", 10000,"src/main/resources/static/seating_plan/Taylor_Swift_Seating_Plan.jpg");
        Venue v2 = new Venue("Singapore Indoor Stadium", 10000, "src/main/resources/static/seating_plan/Charlie_Puth_Seating_Plan.jpg");
        Venue v3 = new Venue("Esplanade Theatre A", 10000, "src/main/resources/static/seating_plan/Charlie_Puth_Seating_Plan.jpg");
        Sector newSect1 = new Sector(v1, "634", 348.0, new String[]{"A","B","C"}, new Integer[]{20,30,40}, "src/main/resources/static/seating_plan/sector_seating.png");
        Sector newSect1a = new Sector(v1, "635", 348.0, new String[]{"D","E","F"}, new Integer[]{50,60,70}, "src/main/resources/static/seating_plan/sector_seating.png");
        Sector newSect2 = new Sector(v2, "634", 348.0, new String[]{"A"}, new Integer[]{20}, "src/main/resources/static/seating_plan/sector_seating.png");
        Sector newSect3 = new Sector(v3, "634", 348.0, new String[]{"A"}, new Integer[]{20}, "src/main/resources/static/seating_plan/sector_seating.png");
        ArrayList<Sector> sects1 = new ArrayList<>();
        ArrayList<Sector> sects2 = new ArrayList<>();
        ArrayList<Sector> sects3 = new ArrayList<>();
        sects1.add(newSect1);
        sects1.add(newSect1a);
        sects2.add(newSect2);
        sects3.add(newSect3);
        v1.setSectors(sects1);
        v2.setSectors(sects2);
        v3.setSectors(sects3);
        result.add(v1);
        result.add(v2);
        result.add(v3);
        return result;
    }
}


        // System.out.println("[Add concert]: " + concerts.save(new Concert("Coldplay Music of the Spheres 2024", 20000,
        //                     "23 - 27 January, 2024", "20:00", "Singapore National Stadium", 
        //                     "src/main/resources/static/concert_posters/Coldplay_Concert_Poster.jpg")).getConcertName());
