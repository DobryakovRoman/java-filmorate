package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private HashMap<Long, Film> films = new HashMap<>();
    private long id;

    @Override
    public Film add(Film film) {
        validate(film);
        film.setId(++id);
        log.debug(String.format("Добавление фильма в хранилище. id: %d, название: %s", film.getId(), film.getName()));
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) throws NotFoundException {
        log.debug("Обновление фильма.");
        if (!films.containsKey(id)) {
            log.warn("Такого фильма нет в хранилище. Обновление не выполнено.");
            throw new NotFoundException("Фильм с таким id не найден.");
        }
        validate(film);
        films.replace(id, film);
        return films.get(id);
    }

    @Override
    public void remove(Film film) {
        if (!films.containsKey(film.getId())) {
            return;
        }
        films.remove(film);
    }

    public Film getFilm(long filmId) {
        if (films.containsKey(filmId)) {
            return films.get(filmId);
        } else {
            return null;
        }
    }

    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    private void validate(Film film) {
        if (!film.getName().isBlank()
                && film.getDescription().length() <= 200
                && film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))
                && film.getDuration() > 0) {
        } else {
            log.warn("Фильм не валидный.");
            throw new ValidationException("Фильм не соответствует критериям.");
        }
    }


}
