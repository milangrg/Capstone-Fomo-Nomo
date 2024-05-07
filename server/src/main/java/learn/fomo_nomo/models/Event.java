package learn.fomo_nomo.models;

import java.time.LocalDateTime;

public class Event {
    private int eventId;
    private User host;
    private String title;
    private String description;
    private Location location;
    private EventType eventType;
    private LocalDateTime start;
    private LocalDateTime end;


    public Event() {
    }

    public Event(int eventId,
                 User host,
                 String description,
                 Location location,
                 EventType eventType,
                 LocalDateTime start,
                 LocalDateTime end,
                 String title) {
        this.eventId = eventId;
        this.host = host;
        this.description = description;
        this.location = location;
        this.eventType = eventType;
        this.start = start;
        this.end = end;
        this.title = title;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
