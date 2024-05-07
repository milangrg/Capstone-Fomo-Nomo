package learn.fomo_nomo.data;

import learn.fomo_nomo.data.mappers.InvitationMapper;
import learn.fomo_nomo.models.Invitation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class InvitationJdbcTemplateRepository implements InvitationRepository {

    private final JdbcTemplate jdbcTemplate;

    public InvitationJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Invitation> findAll() {

        final String sql = "select invitation_id, event_id, guest_id, `status` "
                + "from `invitation` limit 1000;";

        return jdbcTemplate.query(sql, new InvitationMapper());
    }

    @Override
    public Invitation findById(int invitationId) {

        final String sql = "select invitation_id, event_id, guest_id, `status` "
                + "from invitation "
                + "where invitation_id = ?;";

        return jdbcTemplate.query(sql, new InvitationMapper(), invitationId).stream()
                .findFirst().orElse(null);
    }

    @Override
    public Invitation add(Invitation invitation) {

        final String sql = "insert into invitation (event_id, guest_id, `status`) "
                + "values (?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, invitation.getEvent().getEventId());
            ps.setInt(2, invitation.getGuest().getUserId());
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
                + "guest_id = ?, "
                + "`status` = ? "
                + "where invitation_id = ?;";

        return jdbcTemplate.update(sql,
                invitation.getEvent().getEventId(),
                invitation.getGuest().getUserId(),
                invitation.getStatus().getName(),
                invitation.getInvitationId()) > 0;
    }

    @Override
    public boolean deleteById(int invitationId) {
        return jdbcTemplate.update("delete from invitation where invitation_id = ?;", invitationId) > 0;
    }
}
