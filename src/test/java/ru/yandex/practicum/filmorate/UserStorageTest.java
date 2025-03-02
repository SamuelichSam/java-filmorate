package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.user.UserDto;
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
    public void findAllUsersTest() {
        UserDto user1 = new UserDto(null, "email1@email.com", "userLogin1",
                "user1", LocalDate.of(1985, 11, 11));
        UserDto user2 = new UserDto(null, "email2@email.com", "userLogin2",
                "user2", LocalDate.of(1985, 11, 11));
        UserDto user3 = new UserDto(null, "email3@email.com", "userLogin3",
                "user3", LocalDate.of(1985, 11, 11));

        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);

        List<UserDto> users = userStorage.getAll();
        assertThat(users).hasSize(3);
    }

    @Test
    public void createUserTest() {
        UserDto user1 = new UserDto(null, "email1@email.com", "userLogin1",
                "user1", LocalDate.of(1985, 11, 11));

        UserDto createdUser = userStorage.create(user1);

        assertThat(createdUser.name()).isEqualTo("user1");
        assertThat(createdUser.login()).isEqualTo("userLogin1");
        assertThat(createdUser.birthday()).isEqualTo("1985-11-11");
        assertThat(createdUser.email()).isEqualTo("email1@email.com");
    }

    @Test
    public void updateUserTest() {
        UserDto user1 = new UserDto(null, "email1@email.com", "userLogin1",
                "user1", LocalDate.of(1985, 11, 11));

        UserDto createdUser = userStorage.create(user1);
        UserDto user2 = new UserDto(createdUser.id(), "UPDemail1@email.com", "UPDlogin",
                "UPDname", LocalDate.of(1985, 11, 11));
        UserDto updatedUser = userStorage.update(user2);

        assertThat(updatedUser.name()).isEqualTo("UPDname");
        assertThat(updatedUser.login()).isEqualTo("UPDlogin");
        assertThat(updatedUser.birthday()).isEqualTo("1985-11-11");
        assertThat(updatedUser.email()).isEqualTo("UPDemail1@email.com");
    }

    @Test
    public void addGetFriendTest() {
        UserDto user1 = new UserDto(null, "email1@email.com", "userLogin1",
                "user1", LocalDate.of(1985, 11, 11));
        UserDto user2 = new UserDto(null, "email2@email.com", "userLogin2",
                "user2", LocalDate.of(1985, 11, 11));
        UserDto user3 = new UserDto(null, "email3@email.com", "userLogin3",
                "user3", LocalDate.of(1985, 11, 11));

        UserDto createdUser1 = userStorage.create(user1);
        UserDto createdUser2 = userStorage.create(user2);
        UserDto createdUser3 = userStorage.create(user3);
        userStorage.addFriend(createdUser1.id(), createdUser2.id());
        userStorage.addFriend(createdUser1.id(), createdUser3.id());

        List<UserDto> friends = userStorage.getFriends(createdUser1.id());
        assertThat(friends).hasSize(2);
    }

    @Test
    public void deleteFriendTest() {
        UserDto user1 = new UserDto(null, "email1@email.com", "userLogin1",
                "user1", LocalDate.of(1985, 11, 11));
        UserDto user2 = new UserDto(null, "email2@email.com", "userLogin2",
                "user2", LocalDate.of(1985, 11, 11));
        UserDto user3 = new UserDto(null, "email3@email.com", "userLogin3",
                "user3", LocalDate.of(1985, 11, 11));

        UserDto createdUser1 = userStorage.create(user1);
        UserDto createdUser2 = userStorage.create(user2);
        UserDto createdUser3 = userStorage.create(user3);
        userStorage.addFriend(createdUser1.id(), createdUser2.id());
        userStorage.addFriend(createdUser1.id(), createdUser3.id());

        userStorage.deleteFriend(createdUser1.id(), createdUser3.id());

        List<UserDto> friends = userStorage.getFriends(createdUser1.id());
        assertThat(friends).hasSize(1);
    }

    @Test
    public void getCommonFriendsTest() {
        UserDto user1 = new UserDto(null, "email1@email.com", "userLogin1",
                "user1", LocalDate.of(1985, 11, 11));
        UserDto user2 = new UserDto(null, "email2@email.com", "userLogin2",
                "user2", LocalDate.of(1985, 11, 11));
        UserDto user3 = new UserDto(null, "email3@email.com", "userLogin3",
                "user3", LocalDate.of(1985, 11, 11));

        UserDto createdUser1 = userStorage.create(user1);
        UserDto createdUser2 = userStorage.create(user2);
        UserDto createdUser3 = userStorage.create(user3);
        userStorage.addFriend(createdUser1.id(), createdUser2.id());
        userStorage.addFriend(createdUser3.id(), createdUser2.id());

        List<UserDto> friends = userStorage.getCommonFriends(createdUser1.id(), createdUser3.id());
        assertThat(friends).hasSize(1);
    }
}