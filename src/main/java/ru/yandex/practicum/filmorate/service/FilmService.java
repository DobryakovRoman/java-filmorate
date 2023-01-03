package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage storage;

    public Film add(Film film) {
        return storage.add(film);
    }

    public Film update(Film film) {
        return storage.update(film);
    }

    public void removeFilm(Film film) {
        storage.remove(film);
    }

    public Film addLike(Film film, long userId) {
        Film inMemoryFilm = storage.getFilm(film.getId());
        inMemoryFilm.addLike(userId);
        return inMemoryFilm;
    }

    public void removeLike(Film film, long userId) {
        Film inMemoryFilm = storage.getFilm(film.getId());
        if (inMemoryFilm.getLikes().contains(userId)) {
            inMemoryFilm.deleteLike(userId);
        }
    }

    public List<Film> getPopularFilms(int count) {
        return storage.getFilms().stream()
                .sorted((x, y) -> y.getLikes().size() - x.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film setLike(long id, long userId) {
        Film film = storage.getFilm(id);
        film.addLike(userId);
        return film;
    }

    public Film deleteLike(long id, long userId) {
        Film film = storage.getFilm(id);
        film.deleteLike(userId);
        return film;
    }

    public List<Film> getFilms() {
        return new ArrayList<>(storage.getFilms());
    }

    public Film getFilm(long id) {
        return storage.getFilm(id);
    }
}
