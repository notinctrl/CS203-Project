package taylor.project.user;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
   
    private UserRepository users;

    public UserServiceImpl(UserRepository users){
        this.users = users;
     
    }

    @Override
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
