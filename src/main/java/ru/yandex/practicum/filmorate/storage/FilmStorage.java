package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    public Film add(Film film);

    public Film update(Film film);

    public void remove(Film film);

    public Film getFilm(long filmId);

    public List<Film> getFilms();

}
