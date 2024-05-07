package learn.fomo_nomo.data;

import learn.fomo_nomo.data.mappers.InvitationMapper;
import learn.fomo_nomo.models.Invitation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class InvitationJdbcTemplateRepository implements InvitationRepository {

    private final JdbcTemplate jdbcTemplate;

    public InvitationJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Invitation> findAll() {

//        final String sql = "select invitation_id, event_id, user_id, `status` "
//                + "from invitation limit 1000;";
        final String sql = "select i.invitation_id, "
                + "i.event_id, "
                + "e.user_id, h.first_name, h.last_name, h.email, h.phone, h.dob, "
                + "e.title, e.description, "
                + "e.location_id, l.address, l.state, l.city, l.postal, l.location_name, "
                + "e.event_type, e.`start`, e.`end`, "
                + "i.user_id as guest_id, "
                + "i.`status` "
                + "from invitation i "
                + "inner join `event` e on e.event_id = i.event_id "
                + "inner join `user` h on h.user_id = e.user_id "
                + "inner join location l on l.location_id = e.location_id ";

        return jdbcTemplate.query(sql, new InvitationMapper());
    }

    @Override
    public Invitation findById(int invitationId) {

//        final String sql = "select invitation_id, event_id, user_id, `status` "
//                + "from invitation "
//                + "where invitation_id = ?;";
        final String sql = "select i.invitation_id, "
                + "i.event_id, "
                + "e.user_id, h.first_name, h.last_name, h.email, h.phone, h.dob, "
                + "e.title, e.description, "
                + "e.location_id, l.address, l.state, l.city, l.postal, l.location_name, "
                + "e.event_type, e.`start`, e.`end`, "
                + "i.user_id as guest_id, "
                + "i.`status` "
                + "from invitation i "
                + "inner join `event` e on e.event_id = i.event_id "
                + "inner join `user` h on h.user_id = e.user_id "
                + "inner join location l on l.location_id = e.location_id "
                + "where invitation_id = ?;";

        return jdbcTemplate.query(sql, new InvitationMapper(), invitationId).stream()
                .findFirst().orElse(null);

        // first query imcomplete invite object, missing the guest object

    }

    @Override
    public Invitation add(Invitation invitation) {

        final String sql = "insert into invitation (event_id, user_id, `status`) "
                + "values (?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, invitation.getEvent().getEventId());
            ps.setInt(2, invitation.getGuestId());
            ps.setString(3, invitation.getStatus().getName());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        invitation.setInvitationId(keyHolder.getKey().intValue());
        return invitation;
    }

    @Override
    public boolean update(Invitation invitation) {

        final String sql = "update invitation set "
                + "event_id = ?, "
                + "user_id = ?, "
                + "`status` = ? "
                + "where invitation_id = ?;";

        return jdbcTemplate.update(sql,
                invitation.getEvent().getEventId(),
                invitation.getGuestId(),
                invitation.getStatus().getName(),
                invitation.getInvitationId()) > 0;
    }

    @Override
    public boolean deleteById(int invitationId) {
        return jdbcTemplate.update("delete from invitation where invitation_id = ?;", invitationId) > 0;
    }
}
