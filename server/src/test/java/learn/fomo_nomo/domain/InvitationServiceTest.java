package learn.fomo_nomo.domain;

import learn.fomo_nomo.data.InvitationRepository;
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
class InvitationServiceTest {

    @Autowired
    InvitationService service;

    @MockBean
    InvitationRepository repository;

    @Test
    void shouldFindAllInvitationByUserIsInvited() {
        Invitation invitation = makeInvitation();
        invitation.setInvitationId(1);

        List<Invitation> expected = new ArrayList<>();
        expected.add(invitation);

        when(service.findAll(invitation.getGuestId())).thenReturn(expected);
        List<Invitation> actual = service.findAll(1);
        assertEquals(expected, actual);
    }

    @Test
    void shouldFindById() {
        Invitation expected = makeInvitation();
        expected.setInvitationId(1);
        when(repository.findById(1)).thenReturn(expected);
        Invitation actual = service.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWhenInvalid() {
        // Should not add null
        Result<Invitation> result = service.add(null);
        assertEquals(ResultType.INVALID, result.getType());

        // should not add if guest id is null
        Invitation invitation = makeInvitation();
        invitation.setGuestId(0);
        result = service.add(invitation);
        assertEquals(ResultType.INVALID, result.getType());

        // should not add if event is null
        invitation.setEvent(null);
        result = service.add(invitation);
        assertEquals(ResultType.INVALID, result.getType());

        // should not add if invitationId is already set
        invitation = makeInvitation();
        invitation.setInvitationId(1);
        result = service.add(invitation);
        assertEquals(ResultType.INVALID, result.getType());
        assertTrue(result.getMessages().get(0).contains("invitationId cannot be set for `add` operation"));
    }

    @Test
    void shouldNotAddDuplicate() {
        List<Invitation> expected = new ArrayList<>();
        Invitation existingInvitation = makeInvitation();
        expected.add(existingInvitation);

        Invitation duplicate = makeInvitation();

        when(repository.findAll()).thenReturn(expected);
        Result<Invitation> actual = service.add(duplicate);
        assertNull(actual.getPayload());
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).contains("invitation cannot be duplicated"));
    }

    @Test
    void shouldAddWhenValid() {
        Invitation mockOut = makeInvitation();
        Invitation mockIn = makeInvitation();
        mockIn.setInvitationId(0);

        when(repository.add(mockIn)).thenReturn(mockOut);
        Result<Invitation> actual = service.add(mockIn);
        assertEquals(ResultType.SUCCESS, actual.getType());

        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotUpdateWhenInvalid() {
        // Should not update null
        Result<Invitation> result = service.update(null);
        assertEquals(ResultType.INVALID, result.getType());

        // should not update if guest id is null
        Invitation invitation = makeInvitation();
        invitation.setGuestId(0);
        result = service.update(invitation);
        assertEquals(ResultType.INVALID, result.getType());

        // should not update if event is null
        invitation.setEvent(null);
        result = service.update(invitation);
        assertEquals(ResultType.INVALID, result.getType());

        // should not update if invitationId is not set
        invitation = makeInvitation();
        invitation.setInvitationId(0);
        result = service.update(invitation);
        assertEquals(ResultType.INVALID, result.getType());
        assertTrue(result.getMessages().get(0).contains("invitationId must be set for `update` operation"));
    }

    @Test
    void shouldUpdate() {
        Invitation invitation = makeInvitation();
        invitation.setInvitationId(1);
        invitation.setStatus(Status.DECLINED);

        when(repository.update(invitation)).thenReturn(true);

        Result<Invitation> actual = service.update(invitation);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotDeleteNonExisting() {
        when(repository.deleteById(99999)).thenReturn(false);
        assertFalse(service.deleteById(99999));
    }

    @Test
    void shouldDelete() {
        when(repository.deleteById(1)).thenReturn(true);
        assertTrue(service.deleteById(1));
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

        Invitation invitation = new Invitation();
        invitation.setEvent(event);
        invitation.setStatus(Status.ACCEPTED);
        invitation.setGuestId(1);
        return invitation;
    }
}