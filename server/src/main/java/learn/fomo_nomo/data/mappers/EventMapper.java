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
        event.setEventId(resultSet.getInt("event_id"));

        UserMapper hostMapper = new UserMapper();
        event.setHost(hostMapper.mapRow(resultSet,i));

        event.setTitle(resultSet.getString("title"));
        event.setDescription(resultSet.getString("description"));

        LocationMapper locationMapper = new LocationMapper();
        event.setLocation(locationMapper.mapRow(resultSet, i));

        event.setEventType(EventType.findByName(resultSet.getString("event_type")));
        event.setStart(resultSet.getTimestamp("start").toLocalDateTime());
        event.setEnd(resultSet.getTimestamp("end").toLocalDateTime());

        return event;
    }
}
