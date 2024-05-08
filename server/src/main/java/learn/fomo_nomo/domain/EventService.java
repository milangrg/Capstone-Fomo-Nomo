package learn.fomo_nomo.domain;

import learn.fomo_nomo.data.EventRepository;
import learn.fomo_nomo.models.Event;
import learn.fomo_nomo.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class EventService {

    private final EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public List<Event> findAll(){
        return repository.findAll();
    }

    public Event findById(int eventId){
        return repository.findById(eventId);
    }

    public List<Event> findHostEventsByHostId(int hostID){
        return repository.findAll().stream()
                .filter(i -> i.getHost().getUserId() == hostID)
                .collect(Collectors.toList());
    }

    public Result<Event> add(Event event){
        Result<Event> result = validate(event);

        if (event.getEventId() != 0){
            result.addMessage("EventId cannot be set for add operation",ResultType.INVALID);
            return result;
        }

        event = repository.add(event);
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

        if(!repository.update(event)){
            String msg = String.format("EventId: %s, not found",event.getEventId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }
        return result;
    }

    public boolean deleteById(int eventId){
        return repository.delete(eventId);
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

        if (event.getStart().isAfter(event.getEnd()) || event.getStart().equals(event.getEnd())){
            result.addMessage("Start must be before event",ResultType.INVALID);
        }

        if (event.getStart().isBefore(LocalDateTime.now())){
            result.addMessage("Start must take place in the future",ResultType.INVALID);
        }

        List<Event> allEvents = findHostEventsByHostId(event.getHost().getUserId());

        for (Event currEvent : allEvents){
            if (event.getStart().isAfter(currEvent.getStart()) && event.getStart().isBefore(currEvent.getEnd())){
                result.addMessage("Start has an overlap with an existing event",ResultType.INVALID);
            }

            if(event.getEnd().isAfter(currEvent.getStart()) && event.getEnd().isBefore(currEvent.getEnd())){
                result.addMessage("End has an overlap with an existing event",ResultType.INVALID);
            }
        }

        return result;
    }
}
