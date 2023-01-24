package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DbGenreStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public DbGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenre(int id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES WHERE GENRE_ID = ?", id);
        if (mpaRows.next()) {
            return new Genre(
                    mpaRows.getInt("genre_id"),
                    mpaRows.getString("name")
            );
        } else {
            throw new NotFoundException("Жанры отсутствуют");
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES");
        while (mpaRows.next()) {
            genres.add(new Genre(
                    mpaRows.getInt("genre_id"),
                    mpaRows.getString("name")));
        }
        return genres;
    }

    @Override
    public void addFilmGenre(Film film) {
        if (film.getGenres() != null) {
            List<Genre> genres = new ArrayList<>(film.getGenres());
            jdbcTemplate.batchUpdate("INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    preparedStatement.setLong(1, film.getId());
                    preparedStatement.setInt(2, genres.get(i).getId());
                }

                @Override
                public int getBatchSize() {
                    return genres.size();
                }
            });
        }
    }
}
