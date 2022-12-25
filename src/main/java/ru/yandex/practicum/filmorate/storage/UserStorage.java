package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
    public User add(User user);

    public User update(User user) throws NotFoundException;

    public void remove(User user);
}
