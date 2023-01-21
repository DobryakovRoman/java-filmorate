package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;

@Repository
public class DbLikeStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbLikeStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(int filmId, Long userId) {
        filmExists(filmId);
        userExists(userId);
        String sql = "INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void removeLike(int filmId, Long userId) {
        filmExists(filmId);
        userExists(userId);
        String sql = "DELETE FROM FILM_LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    private void filmExists(int filmId) {
        String sqlQuery = "SELECT * FROM FILMS WHERE film_id = ?";
        SqlRowSet filmRow = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        if (!filmRow.next()) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден.");
        }
    }

    private void userExists(Long userId) {
        String sqlQuery = "SELECT * FROM USERS WHERE user_id = ?";
        SqlRowSet userRow = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        if (!userRow.next()) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден.");
        }
    }
}
