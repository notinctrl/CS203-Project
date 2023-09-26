package taylor.project;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import taylor.project.user.User;
import taylor.project.user.UserService;
import taylor.project.user.UserServiceImpl;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

@Controller

public class RegistrationController {

    @Autowired
    private UserServiceImpl userServiceImpl;
    private UserService userService;


    private BCryptPasswordEncoder encoder;

    public RegistrationController(UserService users, BCryptPasswordEncoder encoder){
        this.userService = users;
        this.encoder = encoder;
    }

    @GetMapping("/register")
    public String addNewUserPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }
 
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/save")
    public User saveUser(@Valid @RequestBody User user) {
        

        return userServiceImpl.addUser(user);
    }

    // @ResponseStatus(HttpStatus.CREATED)
    // @PostMapping("/addUser")
    // public User addUser(@Valid @RequestBody User user){
    //     user.setPassword(encoder.encode(user.getPassword()));
    //     return userService.addUser(user);
    // }

}
