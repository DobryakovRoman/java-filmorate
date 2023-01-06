package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long id;

    @Override
    public User add(User user) {
        validate(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        log.debug(String.format("Добавление пользователя в хранилище. id: %d, login: %s", user.getId(), user.getLogin()));
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        log.debug("Обновление пользователя.");
        if (!users.containsKey(user.getId())) {
            log.warn("Такого фильма нет в хранилище. Обновление не выполнено.");
            throw new NotFoundException("Пользователь с таким id не найден.");
        }
        validate(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        users.replace(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public void remove(User user) {
        if (!users.containsKey(user.getId())) {
            return;
        }
        users.remove(user.getId());
    }

    @Override
    public User getUser(long id) {
        if (!contains(id)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        return users.get(id);
    }

    @Override
    public boolean contains(Long id) {
        return users.containsKey(id);
    }

    @Override
    public User getUser(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }
        return users.get(id);
    }

    @Override
    public List<Long> getFriendsOfUser(long id) {
        return new ArrayList<>(users.get(id).getFriends());
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    private void validate(User user) {
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