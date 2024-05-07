package learn.fomo_nomo.models;

import java.util.Objects;

public class Invitation {
    private int invitationId;
    private Event event;
    private int guestId;
    private Status status;

    public Invitation() {
    }

    public Invitation(int invitationId, Event event, int guestId, Status status) {
        this.invitationId = invitationId;
        this.event = event;
        this.guestId = guestId;
        this.status = status;
    }

    public int getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(int invitationId) {
        this.invitationId = invitationId;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invitation that = (Invitation) o;
        return invitationId == that.invitationId && guestId == that.guestId && Objects.equals(event, that.event) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(invitationId, event, guestId, status);
    }

    @Override
    public String toString() {
        return "Invitation{" +
                "invitationId=" + invitationId +
                ", event=" + event +
                ", guestId=" + guestId +
                ", status=" + status +
                '}';
    }
}
