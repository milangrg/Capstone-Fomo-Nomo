package learn.fomo_nomo.data;

import learn.fomo_nomo.models.Event;
import learn.fomo_nomo.models.EventType;
import learn.fomo_nomo.models.Location;
import learn.fomo_nomo.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Test
    void shouldFindById(){
        Event event = repository.findById(1);
        assertNotNull(event);

        assertEquals(event.getTitle(),"Going Away Party");
    }

    @Test
    void shouldAdd(){
        Event event = new Event();
        User user = new User(1,"Alex", "Carter", "acarter1234@emailspot.com", "555-555-1234", LocalDate.of(1995,8,2));
        Location location = new Location(1, "3300 Riverfront Walk", "NY", "Buffalo", "14202", "Riverside Restaurant");
        event.setHost(user);
        event.setTitle("Test");
        event.setDescription("This is a test for add");
        event.setLocation(location);
        event.setEventType(EventType.SOCIAL);
        event.setStart(LocalDateTime.of(2024,9,1,10,0,0));
        event.setEnd(LocalDateTime.of(2024,9,1,10,30,0));

        int eventsSize = repository.findAll().size();
        Event actual = repository.add(event);
        assertNotNull(actual);
        assertEquals(repository.findAll().size(),eventsSize+1);

        assertEquals(repository.findById(repository.findAll().size()).getTitle(),"Test");

    }

    @Test
    void shouldUpdate(){
        Event event = repository.findById(3);
        Location location = new Location(1, "3300 Riverfront Walk", "NY", "Buffalo", "14202", "Riverside Restaurant");
        event.setTitle("Test");
        event.setDescription("This is a test for update");
        event.setLocation(location);
        event.setEventType(EventType.SOCIAL);
        event.setStart(LocalDateTime.of(2024,9,1,10,0,0));
        event.setEnd(LocalDateTime.of(2024,9,1,10,30,0));

        boolean actual = repository.update(event);
        assertTrue(actual);
        Event updatedEvent = repository.findById(3);
        assertEquals(updatedEvent.getTitle(),"Test");

    }

    @Test
    void shouldDelete(){
        boolean actual = repository.delete(4);
        assertTrue(actual);
        assertNull(repository.findById(4));
    }


}