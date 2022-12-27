package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friends;

    public void addFriend(long friend) {
        if (friends != null) {
            friends.add(friend);
        } else {
            friends = new HashSet<>();
            friends.add(friend);
        }
    }

    public void removeFriend(long friend) {
        friends.remove(friend);
    }
}
