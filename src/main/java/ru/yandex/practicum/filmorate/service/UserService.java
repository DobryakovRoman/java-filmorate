package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final InMemoryUserStorage storage;

    public User add(User user) {
        return storage.add(user);
    }

    public User addFriend(long id, long friend) {
        User inMemoryUser = storage.getUser(id);
        inMemoryUser.addFriend(friend);
        return inMemoryUser;
    }

    public void removeFriend(long user, long friend) {
        User inMemoryUser = storage.getUser(user);
        if (inMemoryUser.getFriends().contains(friend)) {
            inMemoryUser.removeFriend(friend);
        }
    }

    public List<Long> getCommonFriends(long id, long otherId) {
        ArrayList<Long> friends = new ArrayList<>();
        for (Long user1Friend : storage.getFriendsOfUser(id)) {
            for (Long user2Friend : storage.getFriendsOfUser(otherId)) {
                if (user1Friend == user2Friend && !friends.contains(user1Friend)) {
                    friends.add(user1Friend);
                }
            }
        }
        return friends;
    }


    public User getUserById(long id) {
        return storage.getUser(id);
    }

    public List<Long> getFriendsOfUser(long id) {
        return new ArrayList<>(storage.getFriendsOfUser(id));
    }
}
