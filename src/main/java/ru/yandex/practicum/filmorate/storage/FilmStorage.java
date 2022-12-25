package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {
    public Film add(Film film);

    public Film update(Film film) throws NotFoundException;
    public void remove(Film film);
}
