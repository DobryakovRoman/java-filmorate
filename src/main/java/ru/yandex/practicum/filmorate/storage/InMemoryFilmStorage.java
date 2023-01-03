package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private long id;

    @Override
    public Film add(Film film) {
        validate(film);
        film.setId(++id);
        log.debug(String.format("Добавление фильма в хранилище. id: %d, название: %s", film.getId(), film.getName()));
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        log.debug("Обновление фильма.");
        if (!films.containsKey(film.getId())) {
            log.warn("Такого фильма нет в хранилище. Обновление не выполнено.");
            throw new NotFoundException("Фильм с таким id не найден.");
        }
        validate(film);
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        films.replace(film.getId(), film);
        return films.get(film.getId());
    }

    @Override
    public void remove(Film film) {
        if (!films.containsKey(film.getId())) {
            return;
        }
        films.remove(film.getId());
    }

    @Override
    public Film getFilm(long filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Такого фильма не существует.");
        }
        return films.get(filmId);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    private void validate(Film film) {
        if (!film.getName().isBlank() && film.getDescription().length() <= 200 && film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28)) && film.getDuration() > 0) {
        } else {
            log.warn("Фильм не валидный.");
            throw new ValidationException("Фильм не соответствует критериям.");
        }
    }
}