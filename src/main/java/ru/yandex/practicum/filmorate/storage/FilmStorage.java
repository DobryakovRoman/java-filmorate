package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film add(Film film);

    Film update(Film film);

    void remove(long film);

    Film getFilm(long filmId);

    List<Film> getFilms();

    List<Film> getPopularFilms(int size);
}
