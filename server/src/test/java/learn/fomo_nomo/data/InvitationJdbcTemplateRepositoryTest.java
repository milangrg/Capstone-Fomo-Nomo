package learn.fomo_nomo.data;

import learn.fomo_nomo.models.Event;
import learn.fomo_nomo.models.Invitation;
import learn.fomo_nomo.models.Status;
import learn.fomo_nomo.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
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

    //@Test
    void shouldFindById() {}

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
        User user = new User();
        user.setUserId(1);
        Event event = new Event();
        event.setEventId(1);

        return new Invitation(
                1,
                event,
                user,
                Status.ACCEPTED
        );
    }

}