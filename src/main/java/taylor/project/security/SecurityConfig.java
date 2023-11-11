package taylor.project.security;

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
     * 
     * Add roles to specify permissions for each endpoint
     * User role: can add concerts to cart.
     * Admin role: can add/delete/update concerts/reviews, and add/list users
     *  
     * Note: '*': matches zero or more characters, e.g., /concerts/* matches /concerts/20
             '**': matches zero or more 'directories' in a path, e.g., /concerts/** matches /concerts/1/details
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .and()
        .httpBasic()
            .and() //  "and()"" method allows us to continue configuring the parent
        .authorizeRequests()

            /** Concert access settings. All visitors can get concert infomation, but only Admins can alter concert info.
             *  permitAll: GET
             *  ADMIN ONLY: POST, PUT, DELETE
             */
            .antMatchers(HttpMethod.GET, "/concerts", "/concerts/*").permitAll() // Anyone can view concerts
            // .antMatchers(HttpMethod.POST, "/concerts", "/concerts/**").authenticated()
            .antMatchers(HttpMethod.POST, "/concerts", "/concerts/**").hasRole("ADMIN")
            // .antMatchers(HttpMethod.PUT, "/concerts", "/concerts/**").authenticated()
            .antMatchers(HttpMethod.PUT, "/concerts", "/concerts/**").hasRole("ADMIN")
            // .antMatchers(HttpMethod.DELETE, "/concerts", "/concerts/*").authenticated()
            .antMatchers(HttpMethod.DELETE, "/concerts", "/concerts/**").hasRole("ADMIN")            

            .antMatchers(HttpMethod.GET, "/tickets/**/status").authenticated()

            /** User access settings. Only Admins are allowed to view/alter user information.
             *  authenticated: GET (only if userId matches userId of logged in user)
             *  ADMIN ONLY: POST, PUT, DELETE
             */
            .antMatchers(HttpMethod.GET, "/users", "/users/**").hasAnyRole("ADMIN", "USER")
            .antMatchers(HttpMethod.POST, "/users", "/users/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.PUT, "/users", "/users/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.DELETE, "/users", "/users/**").hasRole("ADMIN")
            // .antMatchers(HttpMethod.POST, "/adminUsers").hasRole("ADMIN")

            .antMatchers(HttpMethod.POST, "/marketplace/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.PUT, "/marketplace/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.DELETE, "/marketplace/**").hasRole("ADMIN")


            /**ADMIN ONLY functions.
             * 
             */
            .antMatchers(HttpMethod.GET, "/sectorRowAvailability/**").hasAnyRole("USER", "ADMIN")
            .antMatchers(HttpMethod.POST, "/sectorRowAvailability/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.PUT, "/sectorRowAvailability/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.DELETE, "/sectorRowAvailability/**").hasRole("ADMIN")

            // .antMatchers(HttpMethod.GET, "/tickets/**").hasAnyRole("USER","ADMIN")
            .antMatchers(HttpMethod.POST, "/tickets/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.PUT, "/tickets/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.DELETE, "/tickets/**").hasRole("ADMIN")




            // .antMatchers(HttpMethod.GET, "/tickets/**/status").hasAnyRole("ADMIN", "USER")

            // .antMatchers(HttpMethod.GET, "/concerts/**/sectorLayout").authenticated() // New security rule for getSectorLayout
            
        .and()
        .authenticationProvider(authenticationProvider()) //specifies the authentication provider for HttpSecurity
        .csrf().disable()
        .formLogin()
        .loginPage("/login")
        // .usernameParameter("username").permitAll()
        .defaultSuccessUrl("/index", true)
        .and()
            // .logoutUrl("/custom-logout") // Specify a custom logout URL
            // .logoutSuccessUrl("/index") // Redirect to this URL after successful logout
        .logout().logoutSuccessUrl("/index").permitAll()
        .invalidateHttpSession(true) // Invalidate the HTTP session
        .deleteCookies("JSESSIONID") // Delete cookies on logout
        ; // Disable the security headers, as we do not return HTML in our service

        
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
 