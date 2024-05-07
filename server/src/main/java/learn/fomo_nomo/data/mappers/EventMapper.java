package learn.fomo_nomo.data.mappers;

import learn.fomo_nomo.models.Event;
import learn.fomo_nomo.models.EventType;
import learn.fomo_nomo.models.Location;
import learn.fomo_nomo.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventMapper implements RowMapper<Event> {
    @Override
    public Event mapRow(ResultSet resultSet, int i) throws SQLException {
        Event event = new Event();
        UserMapper hostMapper = new UserMapper();
        LocationMapper locationMapper = new LocationMapper();

        event.setEventId(resultSet.getInt("event_id"));
        event.setHost(hostMapper.mapRow(resultSet,i));
        event.setTitle(resultSet.getString("title"));
        event.setDescription(resultSet.getString("description"));
        event.setLocation(locationMapper.mapRow(resultSet, i));
        event.setEventType(EventType.valueOf(resultSet.getString("event_type")));
        event.setStart(resultSet.getTimestamp("start").toLocalDateTime());
        event.setEnd(resultSet.getTimestamp("end").toLocalDateTime());

        return event;
    }
}
