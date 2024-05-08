package learn.fomo_nomo.domain;

import learn.fomo_nomo.data.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)

class EventServiceTest {
    @Autowired
    EventService service;

    @MockBean
    EventRepository eventRepository;

    @Test
    void shouldFindAll(){

    }
    @Test
    void shouldFindById(){

    }
    @Test
    void shouldFindByHostId(){

    }
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
    void shouldNotAcceptNullsOrBlanks(){

    }

    @Test
    void shouldNotAcceptDuplicates(){

    }


}