package learn.fomo_nomo.controllers;

import learn.fomo_nomo.domain.EventService;
import learn.fomo_nomo.domain.Result;
import learn.fomo_nomo.models.Event;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/event")
public class EventController {
    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @GetMapping
    public List<Event> findAll(){
        return service.findAllEvents();
    }

//    @GetMapping("/{eventId}")
//    public Event findByEventId(int eventId){
//        return service.findByEventId(eventId);
//    }

    @GetMapping("user/{userId}")
    public List<Event> findHostEventsByHostId(@PathVariable int userId){
        return service.findHostEventsByHostId(userId);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Object> add(@PathVariable int userId, @RequestBody Event event){
        if (userId != event.getHost().getUserId()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Event> result = service.add(event);
        if(result.isSuccess()){
            return new ResponseEntity<>(result.getPayload(),HttpStatus.CREATED);
        }
        return  ErrorResponse.build(result);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Object> update(@PathVariable int eventId,@RequestBody Event event){
        if (eventId != event.getEventId()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Event> result = service.update(event);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteById(@PathVariable int eventId){
        if(service.deleteById(eventId)){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
