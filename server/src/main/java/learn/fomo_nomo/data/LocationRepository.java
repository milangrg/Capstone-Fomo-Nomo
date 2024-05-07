package learn.fomo_nomo.data;

import learn.fomo_nomo.models.Location;

public interface LocationRepository {
    Location findById(int locationId);
    Location add(Location location);
    boolean update(Location location);
    boolean delete(int locationId);
}
