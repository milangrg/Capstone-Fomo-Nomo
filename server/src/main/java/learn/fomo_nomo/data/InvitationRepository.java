package learn.fomo_nomo.data;

import learn.fomo_nomo.models.Invitation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InvitationRepository {

    List<Invitation> findAll();

    Invitation findById(int invitationId);

    Invitation add(Invitation invitation);

    boolean update(Invitation invitation);

    boolean deleteById(int invitationId);
}
