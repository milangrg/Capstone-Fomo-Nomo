package learn.fomo_nomo.domain;

import learn.fomo_nomo.data.EventRepository;
import learn.fomo_nomo.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
        Event mockOut = makeEvent();
        Event mockIn = makeEvent();
        mockIn.setEventId(0);

        when(eventRepository.add(mockIn)).thenReturn(mockOut);
        Result<Event> actual = service.add(mockIn);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut,actual.getPayload());
    }

    @Test
    void shouldUpdate(){
        Event event = makeEvent();
        event.setEventId(2);
        event.setTitle("Testing Update");

        when(eventRepository.update(event)).thenReturn(true);
        Result<Event> actual = service.update(event);
        assertEquals(ResultType.SUCCESS, actual.getType());

    }

    @Test
    void shouldDelete(){

    }

    @Test
    void shouldNotAcceptNullsOrBlanks(){

    }

    @Test
    void shouldNotAcceptDuplicates(){
        List<Event> expected = new ArrayList<>();
        Event existingEvent = makeEvent();
        expected.add(existingEvent);

        Event duplicate = makeEvent();

        when(eventRepository.findAll()).thenReturn(expected);
        Result<Event> actual = service.add(duplicate);
        assertNull(actual.getPayload());
        assertEquals(ResultType.INVALID,actual.getType());
        System.out.println(actual.getMessages().get(0));

    }

    private Event makeEvent() {
        User host = new User(
                4,
                "Jimmy",
                "Fisher",
                "gonefishin889@yahoo2.com",
                "555-555-7895",
                LocalDate.of(1989, 5, 10)
        );
        Location location = new Location(
                3,
                "720 Party Lane",
                "NY",
                "Syracuse",
                "13201",
                "Party Central"
        );
        Event event = new Event(
                1,
                host,
                "I am moving somewhere else, so come see me off!",
                location,
                EventType.SOCIAL,
                LocalDateTime.of(2024,5,17,20,0,0),
                LocalDateTime.of(2024,5,17,23,59,0),
                "Going Away Party"
        );

        return event;
    }


}