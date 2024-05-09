package learn.fomo_nomo.controllers;

import learn.fomo_nomo.domain.InvitationService;
import learn.fomo_nomo.domain.Result;
import learn.fomo_nomo.models.Event;
import learn.fomo_nomo.models.Invitation;
import learn.fomo_nomo.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/invitation")
public class InvitationController {

    private final InvitationService service;

    public InvitationController(InvitationService service) {
        this.service = service;
    }

    @GetMapping("/invites/{userId}")
    public List<Invitation> findAll(@PathVariable int userId) {
        return service.findAll(userId);
    }

    // return list of events where user is hosting and invited as accepted;
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getEventsForUser(@PathVariable int userId) {

        List<Event> events = service.findAllAcceptedInvitationByGuestId(userId)
                .stream()
                .map(Invitation::getEvent)
                .collect(Collectors.toList());

        events.addAll(service.findAllEventByHosId(userId));

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    // return list of invited guests for the event for edit event view
    // from eventId
    @GetMapping("/guests/{eventId}")
    public ResponseEntity<Object> getInvitedGuests(@PathVariable int eventId) {

        List<User> guestList = service.findAllGuestsByEvent(eventId);
        return new ResponseEntity<>(guestList, HttpStatus.OK);
    }

    // request event object in body
    // return list of invitation
    @GetMapping("/invites/event/{eventId}")
    public ResponseEntity<Object> getAllInvites(@PathVariable int eventId) {

        List<Invitation> invitationList = service.findAllInvitationsByEvent(eventId);
        return new ResponseEntity<>(invitationList, HttpStatus.OK);
    }


    // Single Add
    @PostMapping("/invite/{userId}")
    public ResponseEntity<Object> add(@PathVariable int userId, @RequestBody Invitation invitation) {
        if (userId != invitation.getEvent().getHost().getUserId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Invitation> result = service.add(invitation);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return ErrorResponse.build(result);
    }

    // Bulk Add
    @PostMapping("/invites/{userId}")
    public ResponseEntity<Object> addAll(@PathVariable int userId, @RequestBody List<Invitation> invitationList) {
        if (userId != invitationList.get(0).getEvent().getHost().getUserId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        List<Invitation> resultList = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        for (Invitation invitation : invitationList) {
            Result<Invitation> result = service.add(invitation);
            if (result.isSuccess()) {
                resultList.add(result.getPayload());
            } else {
                errorMessages.addAll(result.getMessages());
            }
        }

        if (!resultList.isEmpty()) {
            return new ResponseEntity<>(resultList, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
    }

    // Check-Conflict for list of guest
    @PostMapping("/conflict/{userId}")
    public ResponseEntity<Object> validateForConflicts(@PathVariable int userId, @RequestBody List<Invitation> invitationList) {
        if (userId != invitationList.get(0).getEvent().getHost().getUserId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        // send invitationList to invitationService to validate
        List<User> conflictedGuests = service.validateConflictsForGuests(invitationList);
        return new ResponseEntity<>(conflictedGuests, HttpStatus.OK);
    }

    @PutMapping("/{invitationId}")
    public ResponseEntity<Object> update(@PathVariable int invitationId, @RequestBody Invitation invitation) {
        if (invitationId != invitation.getInvitationId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Invitation> result = service.update(invitation);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);

    }

    @DeleteMapping("/{invitationId}")
    public ResponseEntity<Void> deleteById(@PathVariable int invitationId) {
        if (service.deleteById(invitationId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
