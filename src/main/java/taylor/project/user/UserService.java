package taylor.project.user;

import java.util.List;


public interface UserService {
    List<User> listUsers();
    User getUser(String username);
    User addUser(User user);
}
