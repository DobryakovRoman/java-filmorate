package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
@Slf4j
@Component
public class DbFriendStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbFriendStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(Long userId, Long otherId) {
        contains(userId);
        contains(otherId);
        String sql = "INSERT INTO FRIENDS(USER_ID, FRIEND_ID) VALUES ( ?, ? )";
        jdbcTemplate.update(sql, userId, otherId);
        log.debug("Пользователю с id=" + userId + " добавлен друг с id=" + otherId);
    }

    @Override
    public void removeFriend(Long userId, Long otherId) {
        contains(userId);
        contains(otherId);
        String sql = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, otherId);
        log.debug("У пользователя с id=" + userId + " удалён друг с id=" + otherId);
    }

    @Override
    public List<User> getFriends(Long userId) {
        contains(userId);
        String sql = "SELECT USERS.* FROM friends" +
                " INNER JOIN USERS ON FRIENDS.FRIEND_ID = USERS.USER_ID WHERE FRIENDS.USER_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                        rs.getLong("USER_ID"),
                        rs.getString("EMAIL"),
                        rs.getString("LOGIN"),
                        rs.getString("NAME"),
                        rs.getDate("BIRTHDAY").toLocalDate(),
                        null),
                userId
        );
    }

    @Override
    public List<User> getCommonsFriends(Long userId, Long otherId) {
        contains(userId);
        contains(otherId);
        String sql = " SELECT *" +
                " FROM USERS " +
                " WHERE user_id IN (" +
                " (SELECT f1.friend_id " +
                " FROM (SELECT user_id, friend_id  " +
                " FROM friends " +
                " WHERE user_id = ?) AS f1 " +
                " INNER JOIN " +
                " (SELECT user_id, friend_id " +
                " FROM friends " +
                " WHERE user_id = ?) as f3 " +
                " ON f1.friend_id = f3.friend_id)) ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                        rs.getLong("USER_ID"),
                        rs.getString("EMAIL"),
                        rs.getString("LOGIN"),
                        rs.getString("NAME"),
                        rs.getDate("BIRTHDAY").toLocalDate(),
                        null),
                userId, otherId);
    }

    private void contains(Long id) {
        String sql = "SELECT * FROM USERS WHERE USER_ID =?";
        SqlRowSet userRow = jdbcTemplate.queryForRowSet(sql, id);
        if (userRow.next()) {
            log.debug("Пользовтель с ID = " + id + " найден");
        } else {
            log.debug("Пользовтеля с ID = " + id + " не существует");
            throw new NotFoundException("Пользовтеля с ID = " + id + " не существует");
        }
    }
}
