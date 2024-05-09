package learn.fomo_nomo.domain;

import learn.fomo_nomo.data.EventRepository;
import learn.fomo_nomo.data.InvitationRepository;
import learn.fomo_nomo.data.LocationRepository;
import learn.fomo_nomo.models.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final InvitationRepository invitationRepository;
    private final LocationRepository locationRepository;

    public EventService(EventRepository eventRepository, InvitationRepository invitationRepository, LocationRepository locationRepository) {
        this.eventRepository = eventRepository;
        this.invitationRepository = invitationRepository;
        this.locationRepository = locationRepository;
    }


    public List<Event> findAllEvents(){
        return eventRepository.findAll();
    }

    public Event findByEventId(int eventId){
        return eventRepository.findById(eventId);
    }

    public List<Location> findAllLocation(){return locationRepository.findAll();}

    public List<Invitation> findAllInvitations(){
        return invitationRepository.findAll();
    }

    public List<Event> findHostEventsByHostId(int hostID){
        return findAllEvents().stream()
                .filter(i -> i.getHost().getUserId() == hostID)
                .collect(Collectors.toList());
    }

    public Result<Event> add(Event event){
        Result<Event> result = validate(event);
        if(!result.isSuccess()){
            return result;
        }

        if (event.getEventId() != 0){
            result.addMessage("EventId cannot be set for add operation",ResultType.INVALID);
            return result;
        }

        event = eventRepository.add(event);
        result.setPayload(event);
        return result;
    }

    public Result<Event> update(Event event){
        Result<Event> result = validate(event);
        if(!result.isSuccess()){
            return result;
        }

        if (event.getEventId() <= 0){
            result.addMessage("EventId must be set for update operation",ResultType.INVALID);
            return result;
        }

        if(!eventRepository.update(event)){
            String msg = String.format("EventId: %s, not found",event.getEventId());
            result.addMessage(msg, ResultType.NOT_FOUND);
            return result;
        }

        //Update went through, all invites should be pending again
        List<Invitation> allInvitations = findAllInvitations().stream()
                .filter(i -> i.getEvent().getEventId() == event.getEventId())
                .collect(Collectors.toList());

        for (Invitation currInvitation : allInvitations){
            currInvitation.setStatus(Status.PENDING);
            invitationRepository.update(currInvitation);
        }

        return result;
    }

    public boolean deleteById(int eventId){
        return eventRepository.delete(eventId);
    }

    private Result<Event> validate(Event event){
        Result<Event> result = new Result<>();

        if(event == null){
            result.addMessage("Event cannot be null",ResultType.INVALID);
        }

        if(event.getHost() == null){
            result.addMessage("Event requires a host",ResultType.INVALID);
        }

        if(event.getTitle() == null || event.getTitle().isBlank()){
            result.addMessage("Event cannot be null",ResultType.INVALID);
        }

        if(event.getLocation() == null ){
            result.addMessage("Event Location cannot be null",ResultType.INVALID);
        }

        if (event.getStart().isAfter(event.getEnd()) || event.getStart().equals(event.getEnd())){
            result.addMessage("Start must be before End time",ResultType.INVALID);
        }

        if (event.getStart().isBefore(LocalDateTime.now())){
            result.addMessage("Start must take place in the future",ResultType.INVALID);
        }

        List<Event> allHostedEvents = findHostEventsByHostId(event.getHost().getUserId())
                .stream()
                .filter(e -> e.getEventId() != event.getEventId())
                .collect(Collectors.toList());

        for (Event currEvent : allHostedEvents){
            if(event.getEventId() == currEvent.getEventId()){
                continue;
            }

            if (event.getStart().isAfter(currEvent.getStart()) && event.getStart().isBefore(currEvent.getEnd())){
                result.addMessage("Start has an overlap with an existing hosted event",ResultType.INVALID);
            }

            if(event.getEnd().isAfter(currEvent.getStart()) && event.getEnd().isBefore(currEvent.getEnd())){
                result.addMessage("End has an overlap with an existing hosted event",ResultType.INVALID);
            }

            if (event.getStart().equals(currEvent.getStart()) || event.getStart().equals(currEvent.getEnd())){
                result.addMessage("Start has an overlap with an existing hosted event",ResultType.INVALID);
            }

            if(event.getEnd().equals(currEvent.getStart()) || event.getEnd().equals(currEvent.getEnd())){
                result.addMessage("End has an overlap with an existing hosted event",ResultType.INVALID);
            }
        }

        List<Invitation> allInvitedEvents = findAllInvitations().stream()
                .filter(i -> i.getGuestId() == event.getHost().getUserId() && i.getStatus().equals(Status.ACCEPTED))
                .collect(Collectors.toList());

        for (Invitation currInvitation : allInvitedEvents){
            if(event.getEventId() == currInvitation.getGuestId()){
                continue;
            }

            if (event.getStart().isAfter(currInvitation.getEvent().getStart()) && event.getStart().isBefore(currInvitation.getEvent().getEnd())){
                result.addMessage("Start has an overlap with an existing invited event",ResultType.INVALID);
            }

            if(event.getEnd().isAfter(currInvitation.getEvent().getStart()) && event.getEnd().isBefore(currInvitation.getEvent().getEnd())){
                result.addMessage("End has an overlap with an existing invited event",ResultType.INVALID);
            }

            if (event.getStart().equals(currInvitation.getEvent().getStart()) || event.getStart().equals(currInvitation.getEvent().getEnd())){
                result.addMessage("Start has an overlap with an existing invited event",ResultType.INVALID);
            }

            if(event.getEnd().equals(currInvitation.getEvent().getStart()) || event.getEnd().equals(currInvitation.getEvent().getEnd())){
                result.addMessage("End has an overlap with an existing invited event",ResultType.INVALID);
            }
        }

        return result;
    }
}
