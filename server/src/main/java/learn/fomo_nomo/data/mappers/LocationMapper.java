package learn.fomo_nomo.data.mappers;
import org.springframework.jdbc.core.RowMapper;
import learn.fomo_nomo.models.Location;

import javax.swing.tree.TreePath;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationMapper implements RowMapper<Location> {
    @Override
    public Location mapRow(ResultSet resultSet,int i) throws SQLException {
        Location location = new Location();
        location.setLocationId(resultSet.getInt("location_id"));
        location.setAddress(resultSet.getString("address"));
        location.setState(resultSet.getString("state"));
        location.setCity(resultSet.getString("city"));
        location.setPostal(resultSet.getString("postal"));
        location.setLocationName(resultSet.getString("location_name"));
        return location;

    }
}
