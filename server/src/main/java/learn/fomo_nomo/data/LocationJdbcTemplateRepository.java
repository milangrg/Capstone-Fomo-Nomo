package learn.fomo_nomo.data;

import learn.fomo_nomo.data.mappers.LocationMapper;
import learn.fomo_nomo.models.Event;
import learn.fomo_nomo.models.Location;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class LocationJdbcTemplateRepository implements LocationRepository{

    private final JdbcTemplate jdbcTemplate;
    public LocationJdbcTemplateRepository(JdbcTemplate jdbcTemplate){this.jdbcTemplate = jdbcTemplate;}

    @Override
    public List<Location> findAll() {
        final String sql = "select location_id, address, city, state, postal, location_name "
                + "from location; ";

        return jdbcTemplate.query(sql, new LocationMapper());
    }

    @Override
    public Location findById(int locationId) {
        final String sql = "select location_id, address, city, state, postal, location_name "
                + "from location "
                + "where location_id = ?;";

        return jdbcTemplate.query(sql, new LocationMapper(), locationId).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Location add(Location location) {
        final String sql = " insert into location (address, city, state, postal, location_name) "
                + "values (?,?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,location.getAddress());
            ps.setString(2,location.getCity());
            ps.setString(3,location.getState());
            ps.setString(4,location.getPostal());
            ps.setString(5,location.getLocationName());
            return ps;
        },keyHolder);

        if (rowsAffected <= 0){
            return null;
        }

        location.setLocationId(keyHolder.getKey().intValue());
        return location;
    }

    @Override
    public boolean update(Location location) {
        final String sql = "Update location set "
                + "address = ?, "
                + "city = ?, "
                + "state = ?, "
                + "postal = ?, "
                + "location_name = ? "
                + "where location_id = ?;";

        return jdbcTemplate.update(sql,
                location.getAddress(),
                location.getCity(),
                location.getState(),
                location.getPostal(),
                location.getLocationName(),
                location.getLocationId()) > 0;
    }

    @Override
    public boolean deleteById(int locationId) {
        // events where location is referenced
        // we might not be using this delete function at all

        return jdbcTemplate.update("delete from location where location_id = ?;" , locationId) > 0;
    }

}
