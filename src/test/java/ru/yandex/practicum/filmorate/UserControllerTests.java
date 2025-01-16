package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTests {

    UserController userController = new UserController();

    @Test
    public void testEmptyEmail() {
        User user = new User();
        user.setId(1L);
        user.setEmail(" ");
        user.setLogin("Testlogin");
        user.setName("Testname");
        user.setBirthday(LocalDate.of(2000,1,1));

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    public void testEmailSymbol() {
        User user = new User();
        user.setId(1L);
        user.setEmail("testexample.com");
        user.setLogin("Testlogin");
        user.setName("Testname");
        user.setBirthday(LocalDate.of(2000,1,1));

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    public void testEmptyLogin() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLogin(" ");
        user.setName("Testname");
        user.setBirthday(LocalDate.of(2000,1,1));

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    public void testSpaceInLogin() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLogin("Test login");
        user.setName("Testname");
        user.setBirthday(LocalDate.of(2000,1,1));

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    public void testEmptyName() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLogin("Testlogin");
        user.setName(" ");
        user.setBirthday(LocalDate.of(2000,1,1));

        userController.create(user);

        assertEquals("Testlogin", user.getName());
    }

    @Test
    public void testBirthday() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLogin("Testlogin");
        user.setName("Testname");
        user.setBirthday(LocalDate.now().plusDays(1));

        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    public void testUpdateUSer() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLogin("Testlogin");
        user.setName("Testname");
        user.setBirthday(LocalDate.of(2000,1,1));
        userController.create(user);
        User updUser = new User();
        updUser.setId(user.getId());
        updUser.setEmail("updtest@xample.com");
        updUser.setLogin("updTestlogin");
        updUser.setName("updTestname");
        updUser.setBirthday(LocalDate.of(2001,2,2));

        User resUser = userController.update(updUser);

        assertEquals(updUser, resUser);
    }
}
