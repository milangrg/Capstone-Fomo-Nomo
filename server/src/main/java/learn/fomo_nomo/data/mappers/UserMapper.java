package learn.fomo_nomo.data.mappers;

import learn.fomo_nomo.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setUserId(resultSet.getInt("user_id"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setEmail(resultSet.getString("email"));
        if (resultSet.getString("phone") != null) {
            user.setPhone(resultSet.getString("phone"));
        }
        if (resultSet.getDate("dob") != null) {
            user.setDob(resultSet.getDate("dob").toLocalDate());
        }
        return user;
    }
}
