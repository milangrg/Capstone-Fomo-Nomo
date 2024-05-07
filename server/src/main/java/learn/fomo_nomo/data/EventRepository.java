package learn.fomo_nomo.data;

import learn.fomo_nomo.models.Event;

import java.util.List;

public interface EventRepository {
    List<Event> findAll();
    Event findById(int eventId);
    Event add(Event event);
    boolean update(int eventId);
    boolean delete(int eventId);



}
