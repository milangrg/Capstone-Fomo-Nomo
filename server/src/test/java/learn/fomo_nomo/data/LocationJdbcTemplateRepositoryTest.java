package learn.fomo_nomo.data;

import learn.fomo_nomo.models.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class LocationJdbcTemplateRepositoryTest {

    final static int NEXT_ID = 13;

    @Autowired
    LocationJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<Location> locations = repository.fillAll();
        assertNotNull(locations);

        // can't predict order
        // if add is first, we may go as high as 13
        assertTrue(locations.size() >= 12 && locations.size() <= 13);
    }

    @Test
    void shouldFindById() {
        Location expected = new Location(
                1,
                "3300 Riverfront Walk",
                "NY",
                "Buffalo",
                "14202",
                "Riverside Restaurant"
        );

        Location actual = repository.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void shouldAdd() {
        Location location = new Location();
        location.setAddress("123");
        location.setCity("Seattle");
        location.setState("WA");
        location.setPostal("45678");

        Location actual = repository.add(location);
        assertNotNull(actual);
        assertEquals(NEXT_ID, actual.getLocationId());
    }

    @Test
    void shouldUpdate() {
        Location location = makeLocation();
        location.setLocationName("TESTING location name...");

        assertTrue(repository.update(location));
        Location actual = repository.findById(2);
        assertEquals(location.getLocationName(), actual.getLocationName());
    }

    private Location makeLocation() {
        return new Location(
                2,
                "1020 Sunshine Blvd",
                "NY",
                "Rochester",
                "14602",
                null
        );
    }

}