package learn.fomo_nomo.controllers;

import learn.fomo_nomo.domain.EventService;
import learn.fomo_nomo.domain.LocationService;
import learn.fomo_nomo.domain.Result;
import learn.fomo_nomo.models.Event;
import learn.fomo_nomo.models.Location;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/event")
public class EventController {
    private final EventService eventService;
    private final LocationService locationService;

    public EventController(EventService eventService, LocationService locationService) {
        this.eventService = eventService;
        this.locationService = locationService;
    }

    @GetMapping
    public List<Event> findAll(){
        return eventService.findAllEvents();
    }

//    @GetMapping("/{eventId}")
//    public Event findByEventId(int eventId){
//        return service.findByEventId(eventId);
//    }

    @GetMapping("/user/{userId}")
    public List<Event> findHostEventsByHostId(@PathVariable int userId){
        return eventService.findHostEventsByHostId(userId);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Object> add(@PathVariable int userId, @RequestBody Event event){
        if (userId != event.getHost().getUserId()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        //Check if location already exists, otherwise make a new one
        if(event.getLocation().getLocationId() == 0){
            Location checkedLocation = locationService.checkIfExist(event.getLocation());
            if (checkedLocation != null){
                event.setLocation(checkedLocation);
            }
            else {
                Result<Location> newLocation = locationService.add(event.getLocation());
                if(!newLocation.isSuccess()){
                    return ErrorResponse.build(newLocation);
                }
                event.setLocation(newLocation.getPayload());
            }
        }


        Result<Event> result = eventService.add(event);
        if(result.isSuccess()){
            return new ResponseEntity<>(result.getPayload(),HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Object> update(@PathVariable int eventId,@RequestBody Event event){
        if (eventId != event.getEventId()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Event> result = eventService.update(event);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteById(@PathVariable int eventId){
        if(eventService.deleteById(eventId)){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
