package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidationTests {

    private static UserController userController;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
    }

    @Test
    public void userEmptyAddTest() {
        User user = User.builder().build();
        final NullPointerException nullPointerExceptionException = assertThrows(
                NullPointerException.class,
                () -> userController.addUser(user)
        );
        assertNull(nullPointerExceptionException.getMessage());
    }

    @Test
    public void UserWOEmailTest() {
        User user = User.builder()
                .email("mail.ru")
                .name("")
                .login("dolore ullamco")
                .birthday(LocalDate.of(1980, 8, 20))
                .id(1)
                .build();
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> userController.addUser(user)
        );
        assertEquals("Пользователь не соответствует критериям.", validationException.getMessage());
    }

    @Test
    public void UserWOLoginTest() {
        User user = User.builder()
                .email("yandex@mail.ru")
                .name("")
                .login("dolore ullamco")
                .birthday(LocalDate.of(1980, 8, 20))
                .id(1)
                .build();
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> userController.addUser(user)
        );
        assertEquals("Пользователь не соответствует критериям.", validationException.getMessage());
    }

    @Test
    public void UserWONameTest() {
        User user = User.builder()
                .email("yandex@mail.ru")
                .name("")
                .login("dolore")
                .birthday(LocalDate.of(1980, 8, 20))
                .id(1)
                .build();
        userController.addUser(user);
        assertEquals("dolore", userController.getUsers().get(0).getName());
    }

    @Test
    public void UserBirthDateTest() {
        User user = User.builder()
                .email("yandex@mail.ru")
                .name("")
                .login("dolore")
                .birthday(LocalDate.of(2180, 8, 20))
                .id(1)
                .build();
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> userController.addUser(user)
        );
        assertEquals("Пользователь не соответствует критериям.", validationException.getMessage());
    }

    @Test
    public void UsersOKTest() {
        User user = User.builder()
                .email("yandex@mail.ru")
                .name("")
                .login("dolore")
                .birthday(LocalDate.of(1980, 8, 20))
                .id(1)
                .build();
        userController.addUser(user);
        user = User.builder()
                .email("yandex@mail.ru")
                .name("")
                .login("dolore")
                .birthday(LocalDate.of(1980, 8, 20))
                .id(2)
                .build();
        userController.addUser(user);
        assertEquals(2, userController.getUsers().size());
    }
}
