package learn.fomo_nomo.data;

import learn.fomo_nomo.models.Event;

import java.util.List;

public interface EventRepository {
    List<Event> findAll();
    Event findById(int eventId);
    Event add(Event event);
    boolean update(Event event);
    boolean delete(int eventId);



}
