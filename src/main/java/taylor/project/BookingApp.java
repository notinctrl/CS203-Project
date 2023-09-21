package taylor.project;

import java.time.LocalDateTime;

import org.apache.tomcat.jni.Local;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import taylor.project.client.RestTemplateClient;
import taylor.project.concert.*;
import taylor.project.user.*;

@SpringBootApplication
public class BookingApp {

	public static void main(String[] args) {
		
		ApplicationContext ctx = SpringApplication.run(BookingApp.class, args);

        // JPA concert repository init. default settings
        ConcertRepository concerts = ctx.getBean(ConcertRepository.class);
        System.out.println("[Add concert]: " + concerts.save(new Concert("Taylor Swift Singapore 2023", 10000,
                            "2024-03-02T19:00", "2024-03-02T22:00",
                            "Singapore National Stadium")).getConcertName());

        System.out.println("[Add concert]: " + concerts.save(new Concert("Taylor Swift Singapore 2024", 10000,
                            "2024-03-02T19:00", "2024-03-02T22:00",
                            "Singapore National Stadium")).getConcertName());
                            
        System.out.println("[Add concert]: " + concerts.save(new Concert("BTS Singapore 2024", 20000,
                            "2024-01-29T20:00", "2024-01-20T23:00",
                            "Singapore Indoor Stadium")).getConcertName());
       
        // JPA user repository init
        UserRepository users = ctx.getBean(UserRepository.class);
        BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);
        System.out.println("[Add user]: " + users.save(
            new User("admin", encoder.encode("goodpassword"),"19-03-2003", 
            "dsasdgsdf@sfs.com", "dsdfsdsd", "ROLE_ADMIN")).getUsername());
            System.out.println("[Add user]: " + users.save(
            new User("normaluser", encoder.encode("goodpassword"),"23-10-2001", 
            "dsasdgsdf@sfs.com", "dsdfsdsd", "ROLE_USER")).getEmailAddress());
        
        // Test the RestTemplate client with authentication
        /**
         * TODO: Activity 3 (after class)
         * Uncomment the following code and test the changes
         * Here we use our own Rest client to test the service
         * Authentication info has been added int the RestTemplateClient.java
         */
        /*
        RestTemplateClient client = ctx.getBean(RestTemplateClient.class);
        System.out.println("[Add book]: " + client.addBook("http://localhost:8080/concerts", new Book("Spring in Actions")).getTitle());

        // Get the 1st book, obtain a HTTP response and print out the title of the book
        System.out.println("[Get book]: " + client.getBookEntity("http://localhost:8080/concerts", 1L).getBody().getTitle());
        */
    }
    
}
