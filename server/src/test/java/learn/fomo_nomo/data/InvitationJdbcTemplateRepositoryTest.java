package learn.fomo_nomo.data;

import learn.fomo_nomo.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class InvitationJdbcTemplateRepositoryTest {

    @Autowired
    InvitationJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<Invitation> invitations = repository.findAll();
        assertNotNull(invitations);

        // can't predict order
        // if delete is first, we're down to 11
        // if add is first, we may go as high as 13
        assertTrue(invitations.size() >= 11 && invitations.size() <= 13);
    }

    @Test
    void shouldFindById() {
        Invitation expected = makeInvitation();

        Invitation actual = repository.findById(7);

        System.out.println(expected);
        System.out.println(actual);



        assertEquals(expected, actual);
    }

    //@Test
    void shouldAdd() {}

    @Test
    void shouldUpdate() {
        Invitation invitation = makeInvitation();
        invitation.setStatus(Status.DECLINED);
        assertTrue(repository.update(invitation));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.deleteById(12));
        assertFalse(repository.deleteById(12));
    }

    private Invitation makeInvitation() {
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

        return new Invitation(
                1,
                event,
                1,
                Status.ACCEPTED
        );
    }

}