package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Repository
public class DbUserStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getLong("USER_ID"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("NAME"),
                rs.getDate("BIRTHDAY").toLocalDate(),
                null)
        );
    }

    @Override
    public User add(User user) {
        validate(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("USER_ID");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        log.debug(String.format("Добавление пользователя в хранилище. id: %d, login: %s", user.getId(), user.getLogin()));
        return user;
    }

    @Override
    public User update(User user) {
        log.debug("Обновление пользователя.");
        if (getUser(user.getId()) != null) {
            String sqlQuery = "UPDATE users SET " +
                    "email = ?, login = ?, name = ?, birthday = ? " +
                    "WHERE USER_ID = ?";
            jdbcTemplate.update(sqlQuery,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());
            log.debug("Пользователь с ID={} успешно обновлен", user.getId());
            return user;
        } else {
            throw new NotFoundException("Пользователь с ID=" + user.getId() + " не найден!");
        }
    }

    @Override
    public void remove(User user) {
        if (getUser(user.getId()) != null) {
            String sqlQuery = "DELETE FROM USERS WHERE USER_ID = ?";
            jdbcTemplate.update(sqlQuery, user.getId());
        }
    }

    @Override
    public User getUser(Long id) {
        User user;
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE USER_ID = ?", id);
        if (userRows.first()) {
            user = new User(
                    userRows.getLong("USER_ID"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate(),
                    null);
        } else {
            throw new NotFoundException("Пользователь с ID=" + id + " не найден!");
        }
        return user;
    }

    private void validate(User user) {
        if (user.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
                && !user.getEmail().isEmpty()
                && !user.getLogin().isEmpty()
                && !user.getLogin().contains(" ")
                && user.getBirthday().isBefore(LocalDate.now())) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
        } else {
            log.warn("Пользователь не валидный.");
            throw new ru.yandex.practicum.filmorate.exceptions.ValidationException("Пользователь не соответствует критериям.");
        }
    }
}
