package project.client;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import project.concert.*;

@Component
public class RestTemplateClient {
    
    private final RestTemplate template;

    /**
     * Add authentication information for the RestTemplate
     * 
     */
    public RestTemplateClient(RestTemplateBuilder restTemplateBuilder) {
        this.template = restTemplateBuilder
                .basicAuthentication("admin", "goodpassword")
                .build();
    }
    /**
     * Get a Concert with given id
     * 
     * @param URI
     * @param id
     * @return
     */
    public Concert getConcert(final String URI, final Long id) {
        final Concert Concert = template.getForObject(URI + "/" + id, Concert.class);
        return Concert;
    }

    /**
     * Add a new Concert
     * 
     * @param URI
     * @param newConcert
     * @return
     */
    public Concert addConcert(final String URI, final Concert newConcert) {
        final Concert returned = template.postForObject(URI, newConcert, Concert.class);
        
        return returned;
    }

    /**
     * Get a Concert, but return a HTTP response entity.
     * @param URI
     * @param id
     * @return
     */
    public ResponseEntity<Concert> getConcertEntity(final String URI, final Long id){
        return template.getForEntity(URI + "/{id}", Concert.class, Long.toString(id));
    }
    
}