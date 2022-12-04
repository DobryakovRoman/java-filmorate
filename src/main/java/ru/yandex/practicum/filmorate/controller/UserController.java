package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int id;

    @GetMapping("/users")
    public List<User> getUsers() {
        log.debug("Получение списка пользователей.");
        return new ArrayList<>(users.values());
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        log.debug("Валидация пользователя.");
        validateUser(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug("Добавление пользователя в Map.");
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) throws NotFoundException {
        log.debug("Обновление пользователя.");
        if (users.containsKey(user.getId())) {
            log.debug("Валидация пользователя.");
            validateUser(user);
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.replace(user.getId(), user);
        } else {
            log.warn("Обновляется пользователь, которого нет в базе.");
            throw new NotFoundException("Пользователь с таким id не найден.");
        }
        return users.get(user.getId());
    }

    private void validateUser(User user) {
        if (user.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
                && !user.getEmail().isEmpty()
                && !user.getLogin().isEmpty()
                && !user.getLogin().contains(" ")
                && user.getBirthday().isBefore(LocalDate.now())) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
        } else {
            log.warn("Пользователь не валидный.");
            throw new ValidationException("Пользователь не соответствует критериям.");
        }
    }
}
