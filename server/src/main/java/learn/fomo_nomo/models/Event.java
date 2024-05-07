package learn.fomo_nomo.models;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return eventId == event.eventId && Objects.equals(host, event.host) && Objects.equals(title, event.title) && Objects.equals(description, event.description) && Objects.equals(location, event.location) && eventType == event.eventType && Objects.equals(start, event.start) && Objects.equals(end, event.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, host, title, description, location, eventType, start, end);
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", host=" + host +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location=" + location +
                ", eventType=" + eventType +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
