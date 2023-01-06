package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage storage;


    public User add(User user) {
        return storage.add(user);
    }

    public User addFriend(long id, long friend) {
        User inMemoryUser = storage.getUser(id);
        User inMemoryUserFriend = storage.getUser(friend);
        inMemoryUser.addFriend(friend);
        inMemoryUserFriend.addFriend(id);
        return inMemoryUser;
    }

    public void removeFriend(long user, long friend) {
        User inMemoryUser = storage.getUser(user);
        if (inMemoryUser.getFriends().contains(friend)) {
            inMemoryUser.removeFriend(friend);
        }
    }

    public List<User> getCommonFriends(long id, long otherId) {
        if (!storage.contains(id) && !storage.contains(otherId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        return storage.getFriendsOfUser(id).stream()
                .filter(x -> storage.getFriendsOfUser(otherId).contains(x))
                .map(x -> getUserById(x))
                .collect(Collectors.toList());
    }

    public User getUserById(long id) {
        if (!storage.contains(id)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        return storage.getUser(id);

    }

    public List<User> getFriendsOfUser(long id) {
        return convertIdsToUsers(storage.getFriendsOfUser(id));
    }

    private List<User> convertIdsToUsers(List<Long> ids) {
        return ids.stream().map(x -> storage.getUser(x)).collect(Collectors.toList());
    }

    public User update(User user) {
        return storage.update(user);
    }

    public List<User> getUsers() {
        return storage.getUsers();
    }
}
