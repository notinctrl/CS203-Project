package taylor.project;

import java.time.LocalDateTime;

import org.apache.tomcat.jni.Local;
import java.time.LocalDate;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import taylor.project.concert.Concert;
import taylor.project.concert.ConcertRepository;
import taylor.project.concertimage.FileUploadController;
import taylor.project.user.User;
import taylor.project.user.UserRepository;
import taylor.project.client.RestTemplateClient;
import taylor.project.concert.*;
import taylor.project.user.*;
import taylor.project.shoppingCart.*;

@SpringBootApplication
@ComponentScan({"taylor.project","taylor.project.fileupload"})
public class BookingApp {

	public static void main(String[] args) throws IOException{
		
        // new File(FileUploadController.uploadDirectory).mkdir();
		ApplicationContext ctx = SpringApplication.run(BookingApp.class, args);

        // JPA concert repository init. default settings
        ConcertRepository concerts = ctx.getBean(ConcertRepository.class);
        System.out.println("[Add concert]: " + concerts.save(new Concert("Taylor Swift Singapore 2023", 10000,
                            "2024-03-02T19:00", "2024-03-02T22:00",
                            "Singapore National Stadium", "src/main/resources/static/concert_posters/Taylor_Swift_Concert_Poster.jpg")).getConcertName());
        // System.out.println("[Add concert]: " + concerts.save(new Concert("Taylor Swift Singapore 2023", 10000, "5 - 6 December 2023", "src/main/resources/static/concert_posters/Taylor_Swift_Concert_Poster.jpg")).getConcertName());
        // System.out.println("[Add concert]: " + concerts.save(new Concert("BTS Singapore 2024", 20000, "TBC", null)).getConcertName());

        System.out.println("[Add concert]: " + concerts.save(new Concert("Taylor Swift Singapore 2024", 10000,
                            "2024-03-02T19:00", "2024-03-02T22:00",
                            "Singapore National Stadium", null)).getConcertName());
                            
        System.out.println("[Add concert]: " + concerts.save(new Concert("BTS Singapore 2024", 20000,
                            "2024-01-29T20:00", "2024-01-20T23:00",
                            "Singapore Indoor Stadium", null)).getConcertName());
       
        // JPA user repository init
        UserRepository users = ctx.getBean(UserRepository.class);
        BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);
        System.out.println("[Add user]: " + users.save(
            new User("admin", encoder.encode("goodpassword"),"19-03-2003", 
            "dsasdgsdf@sfs.com", "dsdfsdsd", "ROLE_ADMIN")).getUsername());
            System.out.println("[Add user]: " + users.save(
            new User("normaluser", encoder.encode("goodpassword"),"23-10-2001", 
            "dsasdgsdf@sfs.com", "dsdfsdsd", "ROLE_USER")).getEmailAddress());
            new User("admin", encoder.encode("goodpassword"), "19-03-2003" , "hello123@gmail.com" ,"1234", "ROLE_ADMIN").getUsername();
        
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
    
}
