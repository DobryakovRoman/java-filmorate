package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Slf4j
@Component
public class DbFilmStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MPAStorage mpaStorage;
    private final GenreStorage genreStorage;

    public DbFilmStorage(JdbcTemplate jdbcTemplate, MPAStorage mpaStorage, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public Film add(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue());
        genreStorage.addFilmGenre(film);
        log.info("Фильм добавлен");
        return getFilm(film.getId());
    }

    @Override
    public Film update(Film film) {
        if (getSqlRowSetByFilmId(film.getId()).next()) {
            String sqlQuery = "UPDATE films SET " +
                    "name = ?, description = ?, release_date = ?, duration = ?, " +
                    "MPA_ID = ? WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
            mpaStorage.getMPA(film.getMpa().getId());
            removeFilmGenres(film.getId());
            addFilmGenres(film);
            film.setGenres(getFilmGenres(film.getId()));
            return film;
        } else {
            throw new NotFoundException("Фильм с ID=" + film.getId() + " не найден!");
        }
    }

    @Override
    public void remove(long id) {
        if (id == 0) {
            throw new ValidationException("Передан пустой аргумент");
        }
        String sqlQuery = "DELETE FROM films WHERE FILM_ID = ? ";
        if (jdbcTemplate.update(sqlQuery, id) == 0) {
            throw new NotFoundException("Фильм с ID=" + id + " не найден!");
        }
    }

    @Override
    public Film getFilm(long id) {
        String sql = "SELECT * FROM FILMS WHERE FILM_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, id);
        if (filmRows.next()) {
            Film film = getFilmFromRow(filmRows);
            log.debug("Найден фильм: {}", film.getId());
            return film;
        } else {
            log.debug("Фильм с идентификатором {} не найден.", id);
            throw new NotFoundException("Фильм с ID=" + id + " не найден!");
        }
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT * FROM films";
        List<Film> allFilms = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql);
        while (filmRows.next()) {
            allFilms.add(getFilmFromRow(filmRows));
        }
        return allFilms;
    }

    @Override
    public List<Film> getPopularFilms(int size) {
        List<Film> films = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT fm.*, COUNT(fl.ID) FROM FILMS AS fm " +
                "LEFT OUTER JOIN FILM_LIKES AS fl on fm.film_id = fl.film_id " +
                "GROUP BY fm.film_id ORDER BY COUNT(fl.ID) DESC LIMIT ?", size);
        while (filmRows.next()) {
            films.add(getFilmFromRow(filmRows));
        }
        return films;
    }

    private Film getFilmFromRow(SqlRowSet rs) {
        Film film = new Film(
                rs.getInt("FILM_ID"),
                rs.getString("NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION"),
                mpaStorage.getMPA(rs.getInt("MPA_ID")),
                getLikesAmount(rs.getInt("FILM_ID")));
        film.setGenres(getFilmGenres(film.getId()));
        return film;
    }

    private int getLikesAmount(int id) {
        String sql = "SELECT COUNT(FILM_ID) AS amount FROM FILM_LIKES WHERE FILM_ID =?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (sqlRowSet.next()) {
            return sqlRowSet.getInt("amount");
        } else {
            return 0;
        }
    }

    private Set<Genre> getFilmGenres(long id) {
        Set<Genre> filmGenres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
        String sqlQuery = "SELECT * FROM GENRES WHERE genre_id IN " +
                "(SELECT genre_id FROM FILM_GENRES WHERE film_id = ?)";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        while (genreRows.next()) {
            filmGenres.add(new Genre(genreRows.getInt("GENRE_ID"),
                    genreRows.getString("NAME")));
        }
        return filmGenres;
    }

    private SqlRowSet getSqlRowSetByFilmId(long id) {
        String sqlQuery = "SELECT * FROM FILMS WHERE film_id = ?";
        return jdbcTemplate.queryForRowSet(sqlQuery, id);
    }

    private void removeFilmGenres(long id) {
        String sqlQuery = "DELETE FROM FILM_GENRES WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    private void addFilmGenres(Film film) {
        genreExists(film);
        String sqlQuery = "INSERT INTO FILM_GENRES(film_id, genre_id) VALUES (?, ?)";
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
        }
    }

    private void genreExists(Film film) {
        String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        if (film.getGenres() == null) {
            return;
        }
        for (Genre genre : film.getGenres()) {
            if (!jdbcTemplate.queryForRowSet(sqlQuery, genre.getId()).next()) {
                throw new NotFoundException("Жанр с id " + genre.getId() + " не найден.");
            }
        }
    }

}
