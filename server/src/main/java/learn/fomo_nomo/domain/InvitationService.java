package learn.fomo_nomo.domain;

import learn.fomo_nomo.data.EventRepository;
import learn.fomo_nomo.data.InvitationRepository;
import learn.fomo_nomo.data.UserRepository;
import learn.fomo_nomo.models.Event;
import learn.fomo_nomo.models.Invitation;
import learn.fomo_nomo.models.Status;
import learn.fomo_nomo.models.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public InvitationService(InvitationRepository invitationRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.invitationRepository = invitationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    // returns all invitation objects (includes event, hostUser, location objects) where user is a guest
    public List<Invitation> findAll(int userId) {

        return invitationRepository.findAll().stream()
                .filter(i -> i.getGuestId() == userId)
                .collect(Collectors.toList());
    }

    // return all invitation objects where guest is invited and status is accepted
    public List<Invitation> findAllAcceptedInvitationByGuestId(int guestId) {
        return invitationRepository.findAll().stream()
                .filter(i -> i.getGuestId() == guestId && i.getStatus() == Status.ACCEPTED)
                .collect(Collectors.toList());
    }

    // returns all event objects where user is hosting
    public List<Event> findAllEventByHosId(int hostId) {
        return eventRepository.findAll().stream()
                .filter(i -> i.getHost().getUserId() == hostId)
                .collect(Collectors.toList());
    }

    // returns all guests for the event
    public List<User> findAllGuestsByEvent(int eventId) {

        List<Invitation> invitationList = invitationRepository.findAll()
                .stream()
                .filter(i -> i.getEvent().getEventId() == eventId)
                .collect(Collectors.toList());

        List<User> guestList = new ArrayList<>();
        for (Invitation invitation : invitationList) {
            guestList.add(userRepository.findById(invitation.getGuestId()));
        }

        return guestList;
    }

    public List<Invitation> findAllInvitationsByEvent(int eventId) {

        return invitationRepository.findAll()
                .stream()
                .filter(i -> i.getEvent().getEventId() == eventId)
                .collect(Collectors.toList());
    }

    public Invitation findById(int invitationId) {
        return invitationRepository.findById(invitationId);
    }

    public Result<Invitation> add(Invitation invitation) {
        Result<Invitation> result = validate(invitation);
        if (!result.isSuccess()) {
            return result;
        }

        if (invitation.getInvitationId() != 0) {
            result.addMessage("invitationId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        // check for duplicate invitation
        Invitation finalInvitation = invitation;
        boolean duplicateInvitation = invitationRepository.findAll()
                .stream()
                .anyMatch(i -> i.getEvent().equals(finalInvitation.getEvent()) &&
                        i.getGuestId() == finalInvitation.getGuestId());
        if (duplicateInvitation) {
            result.addMessage("invitation cannot be duplicated", ResultType.INVALID);
            return result;
        }

        invitation = invitationRepository.add(invitation);
        result.setPayload(invitation);
        return result;
    }

    public Result<Invitation> update(Invitation invitation) {
        Result<Invitation> result = validate(invitation);
        if (!result.isSuccess()) {
            return result;
        }

        if (invitation.getInvitationId() <= 0) {
            result.addMessage("invitationId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        // Check conflicts?
        if (invitation.getStatus() == Status.ACCEPTED) {
            // check all the invitations where user invited and accepted status
            List<Event> eventList = findAllAcceptedInvitationByGuestId(invitation.getGuestId())
                    .stream()
                    .map(Invitation::getEvent)
                    .collect(Collectors.toList());
            // check all events user is hosting
            eventList.addAll(findAllEventByHosId(invitation.getGuestId()));

            Event event = invitation.getEvent();
            for (Event currEvent : eventList) {
                if (currEvent.getEventId() != event.getEventId() && event.getStart().isAfter(currEvent.getStart())
                        && event.getStart().isBefore(currEvent.getEnd())) {
                    result.addMessage("Unable to accept, this event conflicts with another event in your calender.", ResultType.INVALID);
                    return result;
                }
                if (currEvent.getEventId() != event.getEventId() && event.getEnd().isAfter(currEvent.getStart())
                        && event.getEnd().isBefore(currEvent.getEnd())) {
                    result.addMessage("Unable to accept, this event conflicts with another event in your calender.", ResultType.INVALID);
                    return result;
                }
            }
        }

        if (!invitationRepository.update(invitation)) {
            String msg = String.format("invitationId: %s, not found",
                    invitation.getInvitationId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int invitationId) {
        return invitationRepository.deleteById(invitationId);
    }

    private Result<Invitation> validate(Invitation invitation) {
        Result<Invitation> result = new Result<>();

        if (invitation == null) {
            result.addMessage("invitation cannot be null", ResultType.INVALID);
            return result;
        }

        if (invitation.getEvent() == null) {
            result.addMessage("event cannot be null", ResultType.INVALID);
            return result;
        }

        if (invitation.getGuestId() <= 0) {
            result.addMessage("guestId must be set", ResultType.INVALID);
            return result;
        }

        return result;
    }

    public List<User> validateConflictsForGuests(List<Invitation> invitationList) {
        List<User> resultList = new ArrayList<>(); // will store guests with conflict/validation issues
        if (invitationList.isEmpty()) {
            return resultList;
        }

        Event event = invitationList.get(0).getEvent();

        for (Invitation invitation : invitationList) {
            Result<Invitation> result = validate(invitation);
            if (!result.isSuccess()) {
                // Guest validate failed, add guest to list
                resultList.add(userRepository.findById(invitation.getGuestId()));
                continue;
            }

            // Guest validate conflict failed, add guest to list
            if (!validateDates(invitation, event)) {
                resultList.add(userRepository.findById(invitation.getGuestId()));
            }
            // if all validates do nothing
        }

        return resultList;
    }

    private boolean validateDates(Invitation invitation, Event event) {
        int guestId = invitation.getGuestId();

        // check all events guest is hosting
        List<Event> hostedEvents = findAllEventByHosId(guestId);
        for (Event currEvent : hostedEvents) {
            if (event.getStart().isAfter(currEvent.getStart()) && event.getStart().isBefore(currEvent.getEnd())) {
                return false;
            }

            if (event.getEnd().isAfter(currEvent.getStart()) && event.getEnd().isBefore(currEvent.getEnd())) {
                return false;
            }
        }

        // check all events where guest is invited and status is accepted
        List<Event> invitedEvents = findAllAcceptedInvitationByGuestId(guestId)
                .stream()
                .map(Invitation::getEvent)
                .collect(Collectors.toList());

        for (Event currEvent : invitedEvents) {
            if (event.getStart().isAfter(currEvent.getStart()) && event.getStart().isBefore(currEvent.getEnd())) {
                return false;
            }

            if (event.getEnd().isAfter(currEvent.getStart()) && event.getEnd().isBefore(currEvent.getEnd())) {
                return false;
            }
        }

        return true;
    }

}
