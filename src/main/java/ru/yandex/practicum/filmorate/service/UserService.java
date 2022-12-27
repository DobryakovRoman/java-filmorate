package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
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

    public User addFriend(long id, long friend) throws NotFoundException {
        User inMemoryUser = storage.getUser(id);
        User inMemoryUserFriend = storage.getUser(friend);
        inMemoryUser.addFriend(friend);
        inMemoryUserFriend.addFriend(id);
        return inMemoryUser;
    }

    public void removeFriend(long user, long friend) throws NotFoundException {
        User inMemoryUser = storage.getUser(user);
        if (inMemoryUser.getFriends().contains(friend)) {
            inMemoryUser.removeFriend(friend);
        }
    }

    public List<User> getCommonFriends(long id, long otherId) {
        ArrayList<Long> friends = new ArrayList<>();
        try {
            for (Long user1Friend : storage.getFriendsOfUser(id)) {
                for (Long user2Friend : storage.getFriendsOfUser(otherId)) {
                    if (user1Friend == user2Friend && !friends.contains(user1Friend)) {
                        friends.add(user1Friend);
                    }
                }
            }
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
        return convertIdsToUsers(friends);
    }


    public User getUserById(long id) throws NotFoundException {
        return storage.getUser(id);
    }

    public List<User> getFriendsOfUser(long id) {
        return convertIdsToUsers(storage.getFriendsOfUser(id));
    }

    private List<User> convertIdsToUsers(List<Long> ids) {
        ArrayList<User> users = new ArrayList<>();
        for (long userid : ids) {
            users.add(storage.getUser(userid));
        }
        return users;
    }

    public User update(User user) throws NotFoundException {
        return storage.update(user);
    }

    public List<User> getUsers() {
        return storage.getUsers();
    }
}
