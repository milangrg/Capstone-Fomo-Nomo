package learn.fomo_nomo.data;

import learn.fomo_nomo.data.mappers.EventMapper;
import learn.fomo_nomo.models.Event;
import learn.fomo_nomo.models.EventType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

public class EventJdbcTemplateRepository implements EventRepository{

    private final JdbcTemplate jdbcTemplate;

    public EventJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Event> findAll() {
        final String sql = "select event_id, host_id, title, description, location_id, event_type, start, end "
                + "from event;";
        return jdbcTemplate.query(sql,new EventMapper());
    }

    @Override
    @Transactional
    public Event findById(int eventId) {
        final String sql = "select event_id, host_id, title, description, location_id, event_type, start, end "
                + "from event "
                + "where event_id = ?;";

        Event result = jdbcTemplate.query(sql,new EventMapper(),eventId).stream()
                .findAny().orElse(null);

        return result;
    }

    @Override
    public Event add(Event event) {
        final String sql = "insert into event (host_id, title, description, location_id, event_type, start, end)"
                + "values (?,?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, event.getHost().getUserId());
            ps.setString(2, event.getTitle());
            ps.setString(3, event.getDescription());
            ps.setInt(4, event.getLocation().getLocationId());
            ps.setString(5, event.getEventType().getName());
            ps.setTimestamp(6, Timestamp.valueOf(event.getStart()));
            ps.setTimestamp(7, Timestamp.valueOf(event.getEnd()));
            return ps;
        },keyHolder);

        if (rowsAffected <= 0){
            return null;
        }

        event.setEventId(keyHolder.getKey().intValue());
        return event;


    }

    @Override
    public boolean update(int eventId) {
        final String sql = "update event set "
                + "title = ?, "
                + "description = ? "
                + "location_id = ? "
                + "event_type = ? "
                + "start = ? "
                + "end = ? "
                + "where event_id = ?";

        Event event = findById(eventId);
        return jdbcTemplate.update(sql,
                event.getTitle(),
                event.getDescription(),
                event.getLocation().getLocationId(),
                event.getEventType(),
                event.getStart(),
                event.getEnd()) > 0;
    }

    @Override
    public boolean delete(int eventId) {
        return jdbcTemplate.update("delete from event where event_id = ?",eventId) > 0;
    }
}
