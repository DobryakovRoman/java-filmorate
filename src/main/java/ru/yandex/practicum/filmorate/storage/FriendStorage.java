package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {
    void addFriend(Long userId, Long otherId);
    void removeFriend(Long userId, Long otherId);
    List<User> getFriends(Long userId);
    List<User> getCommonsFriends(Long userId, Long otherId);
}
