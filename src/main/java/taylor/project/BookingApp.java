package taylor.project;

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
        System.out.println("[Add concert]: " + concerts.save(new Concert("Taylor Swift Singapore 2023", 10000)).getConcertName());
        System.out.println("[Add concert]: " + concerts.save(new Concert("BTS Singapore 2024", 20000)).getConcertName());

        // JPA user repository init
        UserRepository users = ctx.getBean(UserRepository.class);
        BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);
        System.out.println("[Add user]: " + users.save(
            new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN")).getUsername());
        
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
