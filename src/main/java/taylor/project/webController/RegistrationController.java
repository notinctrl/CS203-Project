package taylor.project.webController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import taylor.project.user.User;
import taylor.project.user.UserService;
import taylor.project.user.UserServiceImpl;
import taylor.project.user.UserController;
import taylor.project.user.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;


@Controller

public class RegistrationController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    private UserService userService;
    @Autowired
    private UserRepository users;
    


    private BCryptPasswordEncoder encoder;

    public RegistrationController(UserService users, BCryptPasswordEncoder encoder){
        this.userService = users;
        this.encoder = encoder;
    }

    @GetMapping("/register")
    public String registrationPage() {

        return "register";
    }
 
    @PostMapping("/save")
    public String userRegistration(@ModelAttribute User user, Model model) {

       Optional<User> existingUser = users.findByUsername(user.getUsername());
        if (existingUser != null) {
            model.addAttribute("error", "Username is already taken.");
            return "register";
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userService.addUser(user);
        return "login";
    }
    

}
