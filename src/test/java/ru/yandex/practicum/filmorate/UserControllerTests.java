package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTests {

    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLogin("Testlogin");
        user.setName("Testname");
        user.setBirthday(LocalDate.of(2000,1,1));
        inMemoryUserStorage.create(user);
        User updUser = new User();
        updUser.setId(user.getId());
        updUser.setEmail("updtest@xample.com");
        updUser.setLogin("updTestlogin");
        updUser.setName("updTestname");
        updUser.setBirthday(LocalDate.of(2001,2,2));

        User resUser = inMemoryUserStorage.update(updUser);

        assertEquals(updUser, resUser);
    }
}
