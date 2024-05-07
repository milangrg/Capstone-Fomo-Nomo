package learn.fomo_nomo.data;

import learn.fomo_nomo.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserJdbcTemplateRepositoryTest {

    final static int NEXT_ID = 11;

    @Autowired
    UserJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<User> users = repository.findAll();
        assertNotNull(users);

        // can't predict order
        // if delete is first, we're down to 9
        // if add is first, we may go as high as 11
        assertTrue(users.size() >= 9 && users.size() <= 11);
    }

    @Test
    void shouldFindById() {
        User expected = makeUser();

        User actual = repository.findById(1);
        assertEquals(expected, actual);

        expected.setUserId(2);
        expected.setFirstName("Marjorie");
        expected.setLastName("Pierce");
        expected.setEmail("margiep54@emailspot.com");
        expected.setPhone("555-555-2216");
        expected.setDob(LocalDate.of(1954, 2, 19));

        actual = repository.findById(2);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotFindByNonExistingId() {
        User actual = repository.findById(99999);
        assertNull(actual);
    }

    @Test
    void shouldAdd() {
        User user = new User();
        user.setFirstName("Mike");
        user.setLastName("Test");
        user.setEmail("123@noemail.com");

        User actual = repository.add(user);
        assertNotNull(actual);
        assertEquals(NEXT_ID, actual.getUserId());
    }

    @Test
    void shouldUpdate() {
        User user = makeUser();
        user.setEmail("udpated@email.com");
        assertTrue(repository.update(user));

        user.setUserId(99999);
        assertFalse(repository.update(user));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.deleteById(10));
        assertFalse(repository.deleteById(10));
    }

    private User makeUser() {
        return new User(
                1,
                "Alex",
                "Carter",
                "acarter1234@emailspot.com",
                "555-555-1234",
                LocalDate.of(1995,8,2)
        );
    }
}