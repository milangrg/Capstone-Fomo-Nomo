package learn.fomo_nomo.models;

public class Invitation {
    private int invitationId;
    private Event event;
    private User guest;
    private Status status;

    public Invitation() {
    }

    public Invitation(int invitationId, Event event, User guest, Status status) {
        this.invitationId = invitationId;
        this.event = event;
        this.guest = guest;
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

    public User getGuest() {
        return guest;
    }

    public void setGuest(User guest) {
        this.guest = guest;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
