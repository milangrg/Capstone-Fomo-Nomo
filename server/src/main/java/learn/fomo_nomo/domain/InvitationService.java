package learn.fomo_nomo.domain;

import learn.fomo_nomo.data.InvitationRepository;
import learn.fomo_nomo.models.Invitation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvitationService {

    private final InvitationRepository repository;

    public InvitationService(InvitationRepository repository) {
        this.repository = repository;
    }

    // returns all invitation objects (includes event, hostUser, location objects) where user is a guest
    public List<Invitation> findAll(int guestId) {

        return repository.findAll().stream()
                .filter(i -> i.getGuestId() == guestId)
                .collect(Collectors.toList());
    }

    public Invitation findById(int invitationId) {
        return  repository.findById(invitationId);
    }

    public Result<Invitation> add(Invitation invitation) {
        Result<Invitation> result = validate(invitation);
        if (!result.isSuccess()) {
            return result;
        }

        if (invitation.getInvitationId() != 0) {
            result.addMessage("invitationId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        // check for duplicate invitation
        Invitation finalInvitation = invitation;
        boolean duplicateInvitation = repository.findAll()
                .stream()
                .anyMatch(i -> i.getEvent().equals(finalInvitation.getEvent()) &&
                        i.getGuestId() == finalInvitation.getGuestId());
        if (duplicateInvitation) {
            result.addMessage("invitation cannot be duplicated", ResultType.INVALID);
            return result;
        }

        // Check conflicts?

        invitation = repository.add(invitation);
        result.setPayload(invitation);
        return result;
    }

    public Result<Invitation> update(Invitation invitation) {
        Result<Invitation> result = validate(invitation);
        if (!result.isSuccess()) {
            return result;
        }

        if (invitation.getInvitationId() <= 0) {
            result.addMessage("invitationId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        // Check duplicate?
        // Check conflicts?

        if (!repository.update(invitation)) {
            String msg = String.format("invitationId: %s, not found",
                    invitation.getInvitationId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int invitationId) {
        return repository.deleteById(invitationId);
    }

    private Result<Invitation> validate(Invitation invitation) {
        Result<Invitation> result = new Result<>();

        if (invitation == null) {
            result.addMessage("invitation cannot be null", ResultType.INVALID);
            return result;
        }

        if (invitation.getEvent() == null) {
            result.addMessage("event cannot be null", ResultType.INVALID);
            return result;
        }

        if (invitation.getGuestId() <= 0) {
            result.addMessage("guestId must be set", ResultType.INVALID);
            return result;
        }

        return result;
    }

}
