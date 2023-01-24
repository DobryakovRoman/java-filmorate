package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage storage;
    private final LikeStorage likeStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;

    public Film add(Film film) {
        storage.add(film);
        genreStorage.addFilmGenre(film);
        return storage.getFilm(film.getId());
    }

    public Film update(Film film) {
        return storage.update(film);
    }

    public void setLike(int filmId, long userId) {
        storage.getFilm(filmId);
        userStorage.getUser(userId);
        likeStorage.addLike(filmId, userId);
    }

    public void removeLike(int filmId, long userId) {
        storage.getFilm(filmId);
        userStorage.getUser(userId);
        likeStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        return storage.getPopularFilms(count);
    }

    public List<Film> getFilms() {
        return new ArrayList<>(storage.getFilms());
    }

    public Film getFilm(long id) {
        return storage.getFilm(id);
    }
}
