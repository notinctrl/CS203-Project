package csd.week5.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    
    private UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userSvc){
        this.userDetailsService = userSvc;
    }
    
    /**
     * Exposes a bean of DaoAuthenticationProvider, a type of AuthenticationProvider
     * Attaches the user details and the password encoder   
     * @return
     */

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
     
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
 
        return authProvider;
    }

    /**
     * TODO: Activity 2a - Authentication
     * Add code to secure requests to Reviews
     * In particular, only authenticated users would be able to create/update/delete Reviews
     * Hint: Add antMatchers rules
     * 
     * 
     * 
     * 
     * TODO: Activity 2b - Authorization
     * Add roles to specify permissions for each enpoint
     * User role: can add review.
     * Admin role: can add/delete/update books/reviews, and add/list users
     *  
     * Note: '*'�matches zero or more characters, e.g., /books/* matches /books/20
             '**'�matches zero or more 'directories' in a path, e.g., /books/** matches /books/1/reviews 
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
        .httpBasic()
            .and() //  "and()"" method allows us to continue configuring the parent
        .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/books", "/books/**").permitAll() // Anyone can view books and reviews
            .antMatchers(HttpMethod.POST, "/books", "books/").authenticated()
            .antMatchers(HttpMethod.PUT, "/books/*").authenticated()
            .antMatchers(HttpMethod.DELETE, "/books/*").authenticated()
            
            // your code here
            .antMatchers(HttpMethod.POST, "/books/**/reviews/", "/books/**/reviews").authenticated()
            .antMatchers(HttpMethod.PUT, "/books/**/reviews/","/books/**/reviews/**").authenticated()
            .antMatchers(HttpMethod.DELETE, "/books/**/reviews/**").authenticated()

            .antMatchers(HttpMethod.POST, "/users", "k/users/").authenticated()
            .antMatchers(HttpMethod.DELETE, "/users", "/users/").authenticated()

            .antMatchers(HttpMethod.POST, "/books/**/reviews/", "/books/**/reviews").hasAnyRole("USER", "ADMIN")
            .antMatchers(HttpMethod.PUT, "/books/**/reviews/","/books/**/reviews/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.DELETE, "/books/**/reviews/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.POST, "/users", "/users/").hasRole("ADMIN")
            .antMatchers(HttpMethod.GET, "/users", "/users/").hasRole("ADMIN")
            .and()
        .authenticationProvider(authenticationProvider()) //specifies the authentication provider for HttpSecurity
        .csrf().disable()
        .formLogin().disable()
        .headers().disable(); // Disable the security headers, as we do not return HTML in our service
        return http.build();
    }

    /**
     * @Bean annotation is used to declare a PasswordEncoder bean in the Spring application context. 
     * Any calls to encoder() will then be intercepted to return the bean instance.
     */
    @Bean
    public BCryptPasswordEncoder encoder() {
        // auto-generate a random salt internally
        return new BCryptPasswordEncoder();
    }
}
 