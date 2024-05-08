package learn.fomo_nomo.domain;

import learn.fomo_nomo.data.EventRepository;
import learn.fomo_nomo.data.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)

class UserServiceTest {
    @Autowired
    UserService service;

    @MockBean
    UserRepository userRepository;

    @Test
    void shouldAdd(){

    }

    @Test
    void shouldUpdate(){

    }

    @Test
    void shouldDelete(){

    }

    @Test
    void shouldNotAdd(){

    }
    @Test
    void shouldNotUpdate(){

    }
    @Test
    void shouldNotDelete(){

    }

}