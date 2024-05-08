package learn.fomo_nomo.domain;

import learn.fomo_nomo.data.LocationRepository;
import learn.fomo_nomo.models.Location;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository repository;

    public LocationService(LocationRepository repository) {
        this.repository = repository;
    }

    public List<Location> findAll() {
        return repository.fillAll();
    }

    public Location findById(int locationId) {
        return repository.findById(locationId);
    }

    public Result<Location> add(Location location) {
        Result<Location> result = validate(location);
        if (!result.isSuccess()) {
            return result;
        }

        if (location.getLocationId() != 0) {
            result.addMessage("locationId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        // check for duplicate locations?

        location = repository.add(location);
        result.setPayload(location);
        return result;
    }

    public Result<Location> update(Location location) {
        Result<Location> result = validate(location);
        if (!result.isSuccess()) {
            return result;
        }

        if (location.getLocationId() <= 0) {
            result.addMessage("locationId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        // check for duplicate locations?

        if (!repository.update(location)) {
            String msg = String.format("locationId: %s, not found",
                    location.getLocationId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    private Result<Location> validate(Location location) {
        Result<Location> result = new Result<>();

        if (location == null) {
            result.addMessage("location cannot be null", ResultType.INVALID);
            return result;
        }

        if (location.getAddress() == null || location.getAddress().isBlank()) {
            result.addMessage("address is required", ResultType.INVALID);
        }

        if (location.getCity() == null || location.getCity().isBlank()) {
            result.addMessage("city is required", ResultType.INVALID);
        }

        if (location.getState() == null || location.getState().isBlank()) {
            result.addMessage("state is required", ResultType.INVALID);
        }

        // null postal is fine in schema
        return result;
    }

}
