package taylor.project.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    
    @Autowired
    private UserRepository users;

    public UserServiceImpl(UserRepository users){
        this.users = users;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> listUsers() {
        return users.findAll();
    }
    
    @Override
    public User getUser(String username){

        return users.findByUsername(username).map(user -> {
            return user;
        }).orElse(null);
    }
        
    @Override
    public User addUser(User user) {
        return users.save(user);
    }
}
