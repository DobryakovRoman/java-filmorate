package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private long id;

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.debug("Получение списка фильмов.");
        return new ArrayList<>(films.values());
    }

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) {
        validate(film);
        film.setId(++id);
        log.debug(String.format("Добавление фильма в хранилище. id: %d, название: %s", film.getId(), film.getName()));
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) throws NotFoundException {
        log.debug("Обновление фильма.");
        if (films.containsKey(film.getId())) {
            log.warn("Такого фильма нет в хранилище. Обновление не выполнено.");
            throw new NotFoundException("Фильм с таким id не найден.");
        }
        validate(film);
        films.replace(film.getId(), film);
        return films.get(film.getId());
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
