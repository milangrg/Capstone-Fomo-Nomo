package learn.fomo_nomo.data;

import learn.fomo_nomo.models.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class EventJdbcTemplateRepositoryTest {

    @Autowired
    EventJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll(){
        List<Event> events = repository.findAll();
        assertNotNull(events);

        assertTrue(events.size() >= 5 && events.size() <= 7);
    }

    

}