package learn.fomo_nomo.data.mappers;

import learn.fomo_nomo.models.Invitation;
import learn.fomo_nomo.models.Status;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InvitationMapper implements RowMapper<Invitation> {

    @Override
    public Invitation mapRow(ResultSet resultSet, int i) throws SQLException {
        Invitation invitation = new Invitation();
        invitation.setInvitationId(resultSet.getInt("invitation_id"));

        EventMapper eventMapper = new EventMapper();
        invitation.setEvent(eventMapper.mapRow(resultSet, i));

        UserMapper guestMapper = new UserMapper();
        invitation.setGuest(guestMapper.mapRow(resultSet, i));

        invitation.setStatus(Status.valueOf(resultSet.getString("status")));

        return invitation;
    }
}
