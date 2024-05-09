package learn.fomo_nomo.domain;

import learn.fomo_nomo.data.LocationRepository;
import learn.fomo_nomo.models.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class LocationServiceTest {

    @Autowired
    LocationService service;

    @MockBean
    LocationRepository repository;

    @Test
    void shouldFindAll() {
        List<Location> expected = new ArrayList<>();
        expected.add(makeLocation());

        when(repository.findAll()).thenReturn(expected);
        List<Location> actual = service.findAll();
        assertEquals(expected, actual);
    }

    @Test
    void shouldFindById() {
        Location expected = makeLocation();
        when(repository.findById(1)).thenReturn(expected);
        Location actual = service.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWhenInvalid() {
        // Should not add null
        Result<Location> result = service.add(null);
        assertEquals(ResultType.INVALID, result.getType());

        // Should not add if state is null
        Location location = makeLocation();
        location.setState(null);
        result = service.add(location);
        assertEquals(ResultType.INVALID, result.getType());

        // Should not add if city is blank
        location.setCity("");
        result = service.add(location);
        assertEquals(ResultType.INVALID, result.getType());

        // Should not add if address is blank
        location.setAddress("");
        result = service.add(location);
        assertEquals(ResultType.INVALID, result.getType());

        // should not add if locationId is set
        result = service.add(makeLocation());
        assertEquals(ResultType.INVALID, result.getType());
        assertTrue(result.getMessages().get(0).contains("locationId cannot be set for `add` operation"));
    }

    @Test
    void shouldAddWhenValid() {
        Location mockIn = makeLocation();
        mockIn.setLocationId(0);
        Location mockOut = makeLocation();

        when(repository.add(mockIn)).thenReturn(mockOut);
        Result<Location> actual = service.add(mockIn);

        assertNotNull(actual.getPayload());
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotUpdateWhenInvalid() {
        // Should not update null
        Result<Location> result = service.update(null);
        assertEquals(ResultType.INVALID, result.getType());

        // Should not update if state is null
        Location location = makeLocation();
        location.setState(null);
        result = service.update(location);
        assertEquals(ResultType.INVALID, result.getType());

        // Should not update if city is blank
        location.setCity("");
        result = service.update(location);
        assertEquals(ResultType.INVALID, result.getType());

        // Should not update if address is blank
        location.setAddress("");
        result = service.update(location);
        assertEquals(ResultType.INVALID, result.getType());

        // should not update if locationId is not set
        location = makeLocation();
        location.setLocationId(0);
        result = service.update(location);
        assertEquals(ResultType.INVALID, result.getType());
        assertTrue(result.getMessages().get(0).contains("locationId must be set for `update` operation"));
    }

    @Test
    void shouldUpdate() {
        Location location = makeLocation();
        location.setAddress("123 Testing Address");

        when(repository.update(location)).thenReturn(true);

        Result<Location> actual = service.update(location);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    private Location makeLocation() {
        return new Location(
                1,
                "3300 Riverfront Walk",
                "NY",
                "Buffalo",
                "14202",
                "Riverside Restaurant"
        );
    }

}