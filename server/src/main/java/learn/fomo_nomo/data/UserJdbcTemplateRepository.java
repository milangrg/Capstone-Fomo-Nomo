package learn.fomo_nomo.data;

import learn.fomo_nomo.data.mappers.EventMapper;
import learn.fomo_nomo.data.mappers.UserMapper;
import learn.fomo_nomo.models.Event;
import learn.fomo_nomo.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserJdbcTemplateRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<User> findAllExceptHost(int userId) {

        final String sql = "select user_id, first_name, last_name, email, phone, dob "
                + "from `user` "
                + "where user_id != ?;";
        return jdbcTemplate.query(sql, new UserMapper(),userId);
    }


    @Override
    public List<User> findAll() {

        final String sql = "select user_id, first_name, last_name, email, phone, dob "
                + "from `user`;";
        return jdbcTemplate.query(sql, new UserMapper());
    }

    @Override
    public User findById(int userId) {

        final String sql = "select user_id, first_name, last_name, email, phone, dob "
                + "from `user` "
                + "where user_id = ?;";

       return jdbcTemplate.query(sql, new UserMapper(), userId).stream()
                .findFirst().orElse(null);
    }

    @Override
    public User add(User user) {

        final String sql = "insert into `user` (first_name, last_name, email, phone, dob) "
                + "values (?, ?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            if (user.getDob() != null) {
                ps.setDate(5, Date.valueOf(user.getDob()));
            } else {
                ps.setDate(5, null);
            }
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        user.setUserId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public boolean update(User user) {

        final String sql = "update `user` set "
                + "first_name = ?, "
                + "last_name = ?, "
                + "email = ?, "
                + "phone = ?, "
                + "dob = ? "
                + "where user_id = ?;";

        return jdbcTemplate.update(sql,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getDob(),
                user.getUserId()) > 0;
    }

    @Override
    @Transactional
    public boolean deleteById(int userId) {
        // events where user is guest
        jdbcTemplate.update("delete from invitation where user_id = ?;", userId);

        // events where user is the host
        List<Integer> eventList = findEventsByUserId(userId);
        if (!eventList.isEmpty()) {
            for (int eventId: eventList) {
                jdbcTemplate.update("delete from invitation where event_id = ?;", eventId);
                jdbcTemplate.update("delete from event where event_id = ?;", eventId);
            }
        }

        return jdbcTemplate.update("delete from user where user_id = ?;", userId) > 0;
    }

    private List<Integer> findEventsByUserId(int userId) {

        final String sql = "select event_id, user_id, title, description, location_id, event_type, start, end " +
                "from `event` " +
                "where user_id = ?;";

        return jdbcTemplate.query(sql, new EventMapper(), userId).stream()
                .map(Event::getEventId)
                .collect(Collectors.toList());
    }

}
