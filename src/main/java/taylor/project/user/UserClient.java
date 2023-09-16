package taylor.project.user;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserClient {
  
    private RestTemplate template;
    
    UserClient(RestTemplateBuilder builder) {
        this.template = builder.build();
    }
    

    public User getUsers(final String URI) {
        final User users = template.getForObject(URI + "/" , User.class);
        return users;
    }

    /**
     * Get a user with given id
     * 
     * @param URI
     * @param id
     * @return
     */
     public User getUser(final String URI, final String username) {
        final User user = template.getForObject(URI + "/" + username, User.class);
        return user;
    }

    /**
     * Add a new book
     * 
     * @param URI
     * @param newBook
     * @return
     */
    public User addUser(final String URI, final User newUser) {
        final User returned = template.postForObject(URI, newUser, User.class);
        return returned;
    }
    
}

