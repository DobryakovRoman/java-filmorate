package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public User add(User user);

    public User update(User user);

    public void remove(User user);

    public User getUser(long id);

    public boolean contains(Long id);

    public User getUser(Long id);

    public List<Long> getFriendsOfUser(long id);

    public List<User> getUsers();
}
