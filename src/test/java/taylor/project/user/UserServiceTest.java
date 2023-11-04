package taylor.project.user;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.prepost.PreAuthorize;




@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository users;

    @InjectMocks
    private UserServiceImpl userService;
    @Test
    void addUser_NewInfo_ReturnSavedUser(){
        User user = new User("dahee", "12345678", "20011023", "dahee@smu.com", "singpapore and korea","ROLE_ADMIN");
        // Optional<User> optionalUser = Optional.of(user);
        // when(users.findByUsername("dahee")).thenReturn(optionalUser);
        when(users.save(any(User.class))).thenReturn(user);
        // act ***
        User savedUser = userService.addUser(user);
        
        // assert ***
        assertNotNull(savedUser);

        //verify(users).findByUsername(user.getUsername());
        verify(users).save(user);
    }

}


// @Test
// void addBook_SameTitle_ReturnNull(){ 
    // Book book = new Book("The Same Title Exists");
    // List<Book> sameTitles = new ArrayList<Book>(); 
    // sameTitles.add(new Book("The Same Title Exists")); 
    // when(books.findByTitle(book.getTitle())).thenReturn(sameTitles); 
    // Book savedBook = bookService.addBook(book); 
    // assertNull(savedBook); 
    // verify(books).findByTitle(book.getTitle());
// }