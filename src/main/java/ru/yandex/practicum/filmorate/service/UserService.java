package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage storage;
    private final FriendStorage friendStorage;

    public User add(User user) {
        return storage.add(user);
    }

    public void addFriend(long id, long friend) {
        friendStorage.addFriend(id, friend);
    }

    public void removeFriend(long user, long friend) {
        friendStorage.removeFriend(user, friend);
    }

    public List<User> getCommonFriends(long id, long otherId) {
        return friendStorage.getCommonsFriends(id, otherId);
    }

    public User getUser(long id) {
        return storage.getUser(id);
    }

    public List<User> getFriendsOfUser(long id) {
        return friendStorage.getFriends(id);
    }

    public User update(User user) {
        return storage.update(user);
    }

    public List<User> getUsers() {
        return storage.getUsers();
    }
}
