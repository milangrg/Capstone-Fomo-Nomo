package learn.fomo_nomo.data;

import learn.fomo_nomo.models.Location;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LocationRepository {
    List<Location> fillAll();
    Location findById(int locationId);
    Location add(Location location);
    boolean update(Location location);
    boolean deleteById(int locationId);
}
