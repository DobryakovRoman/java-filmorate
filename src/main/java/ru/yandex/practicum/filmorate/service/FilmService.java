package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage storage;
    private final LikeStorage likeStorage;

    public Film add(Film film) {
        return storage.add(film);
    }

    public Film update(Film film) {
        return storage.update(film);
    }

    public void setLike(int filmId, long userId) {
        likeStorage.addLike(filmId, userId);
    }

    public void removeLike(int filmId, long userId) {
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
