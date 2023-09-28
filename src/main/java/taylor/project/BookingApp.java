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

@SpringBootApplication
@ComponentScan({"taylor.project","taylor.project.fileupload"})
public class BookingApp {

	public static void main(String[] args) throws IOException{

		ApplicationContext ctx = SpringApplication.run(BookingApp.class, args);

        // JPA concert repository init. default settings
        ConcertRepository concerts = ctx.getBean(ConcertRepository.class);
        // refer to below psvm for initialised concerts.
        List<Venue> vList = iniVenues();
        List<Concert> cList = iniConcerts(vList);
        System.out.println("[Add concert]: " + concerts.save(cList.get(0)).getConcertName());
        System.out.println("[Add concert]: " + concerts.save(cList.get(1)).getConcertName());
        System.out.println("[Add concert]: " + concerts.save(cList.get(2)).getConcertName());
       
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
                            "9 - 11 April, 2025", "19:00", vList.get(0),
                             null));
        result.add(new Concert("BTS Singapore 2024", 20000,
                            "21 - 22 September, 2024", "20:00", vList.get(0), 
                            null));
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

    public static List<Venue> iniVenues(){
        List<Venue> result = new ArrayList<>();
        Sector newSect = new Sector("634", 348.0, new String[]{"A", "B", "C"}, new int[]{20,20,20}, "src/main/resources/static/seating_plan/sector_seating.png");
        ArrayList<Sector> sects = new ArrayList<>();
        sects.add(newSect);
        result.add(new Venue("Singapore National Stadium", 10000, sects, "src/main/resources/static/seating_plan/Taylor_Swift_Seating_Plan.jpg"));
        result.add(new Venue("Singapore Indoor Stadium", 10000, sects, "src/main/resources/static/seating_plan/Charlie_Puth_Seating_Plan.jpg"));
        return result;
    }
}
