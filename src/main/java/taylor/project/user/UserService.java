package taylor.project.user;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;


public interface UserService {
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<User> listUsers();
    
    User getUser(String username);
    User addUser(User user);
}
