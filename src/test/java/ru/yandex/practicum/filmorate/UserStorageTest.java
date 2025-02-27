package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorageImpl;
import ru.yandex.practicum.filmorate.storage.impl.mapper.UserRowMapper;

import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorageImpl.class, UserRowMapper.class})
public class UserStorageTest {
    private final UserDbStorageImpl userStorage;

    @Test
    public void FindAllUsersTest() {
        User user1 = new User(null, "email1@email.com", "userLogin1",
                "user1", LocalDate.of(1985, 11, 11));
        User user2 = new User(null, "email2@email.com", "userLogin2",
                "user2", LocalDate.of(1985, 11, 11));
        User user3 = new User(null, "email3@email.com", "userLogin3",
                "user3", LocalDate.of(1985, 11, 11));

        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);

        List<User> users = userStorage.getAll();
        System.out.println(users);
        assertThat(users).hasSize(3);
    }

    @Test
    public void createUserTest() {
        User user1 = new User(null, "email1@email.com", "userLogin1",
                "user1", LocalDate.of(1985, 11, 11));

        User createdUser = userStorage.create(user1);

        assertThat(createdUser.getName()).isEqualTo("user1");
        assertThat(createdUser.getLogin()).isEqualTo("userLogin1");
        assertThat(createdUser.getBirthday()).isEqualTo("1985-11-11");
        assertThat(createdUser.getEmail()).isEqualTo("email1@email.com");
    }

    @Test
    public void updateUserTest() {
        User user1 = new User(null, "email1@email.com", "userLogin1",
                "user1", LocalDate.of(1985, 11, 11));

        User createdUser = userStorage.create(user1);
        createdUser.setName("UPDname");
        createdUser.setLogin("UPDlogin");
        createdUser.setEmail("UPDemail1@email.com");
        User updatedUser = userStorage.update(createdUser);

        assertThat(updatedUser.getName()).isEqualTo("UPDname");
        assertThat(updatedUser.getLogin()).isEqualTo("UPDlogin");
        assertThat(updatedUser.getBirthday()).isEqualTo("1985-11-11");
        assertThat(updatedUser.getEmail()).isEqualTo("UPDemail1@email.com");
    }

    @Test
    public void addGetFriendTest() {
        User user1 = new User(null, "email1@email.com", "userLogin1",
                "user1", LocalDate.of(1985, 11, 11));
        User user2 = new User(null, "email2@email.com", "userLogin2",
                "user2", LocalDate.of(1985, 11, 11));
        User user3 = new User(null, "email3@email.com", "userLogin3",
                "user3", LocalDate.of(1985, 11, 11));

        User createdUser1 = userStorage.create(user1);
        User createdUser2 = userStorage.create(user2);
        User createdUser3 = userStorage.create(user3);
        userStorage.addFriend(createdUser1.getId(), createdUser2.getId());
        userStorage.addFriend(createdUser1.getId(), createdUser3.getId());

        List<User> friends = userStorage.getFriends(user1.getId());
        assertThat(friends).hasSize(2);
    }

    @Test
    public void deleteFriendTest() {
        User user1 = new User(null, "email1@email.com", "userLogin1",
                "user1", LocalDate.of(1985, 11, 11));
        User user2 = new User(null, "email2@email.com", "userLogin2",
                "user2", LocalDate.of(1985, 11, 11));
        User user3 = new User(null, "email3@email.com", "userLogin3",
                "user3", LocalDate.of(1985, 11, 11));

        User createdUser1 = userStorage.create(user1);
        User createdUser2 = userStorage.create(user2);
        User createdUser3 = userStorage.create(user3);
        userStorage.addFriend(createdUser1.getId(), createdUser2.getId());
        userStorage.addFriend(createdUser1.getId(), createdUser3.getId());

        userStorage.deleteFriend(createdUser1.getId(), createdUser3.getId());

        List<User> friends = userStorage.getFriends(user1.getId());
        assertThat(friends).hasSize(1);
    }

    @Test
    public void getCommonFriendsTest() {
        User user1 = new User(null, "email1@email.com", "userLogin1",
                "user1", LocalDate.of(1985, 11, 11));
        User user2 = new User(null, "email2@email.com", "userLogin2",
                "user2", LocalDate.of(1985, 11, 11));
        User user3 = new User(null, "email3@email.com", "userLogin3",
                "user3", LocalDate.of(1985, 11, 11));

        User createdUser1 = userStorage.create(user1);
        User createdUser2 = userStorage.create(user2);
        User createdUser3 = userStorage.create(user3);
        userStorage.addFriend(createdUser1.getId(), createdUser2.getId());
        userStorage.addFriend(createdUser3.getId(), createdUser2.getId());

        List<User> friends = userStorage.getCommonFriends(createdUser1.getId(), createdUser3.getId());
        assertThat(friends).hasSize(1);
    }
}
