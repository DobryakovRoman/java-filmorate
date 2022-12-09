package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmValidationTests {

    private static FilmController filmController;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    public void filmEmptyAddTest() {
        Film film = Film.builder().build();
        final NullPointerException nullPointerExceptionException = assertThrows(
                NullPointerException.class,
                () -> filmController.addFilm(film)
        );
        assertNull(nullPointerExceptionException.getMessage());
    }

    @Test
    public void filmOKAddTest() {
        Film film = Film.builder()
                .id(1)
                .name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .build();
        filmController.addFilm(film);
        film = Film.builder()
                .id(2)
                .name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .build();
        filmController.addFilm(film);
        assertEquals(2, filmController.getFilms().size());
    }

    @Test
    public void filmWONameTest() {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("adipisicing")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .build();
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film)
        );
        assertEquals("Фильм не соответствует критериям.", validationException.getMessage());
    }

    @Test
    public void filmWOLongDescriptionTest() {
        Film film = Film.builder()
                .id(1)
                .name("nisi eiusmod")
                .description(new String(new char[201]))
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .build();
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film)
        );
        assertEquals("Фильм не соответствует критериям.", validationException.getMessage());
    }

    @Test
    public void filmReleaseDateTest() {
        Film film = Film.builder()
                .id(1)
                .name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(100)
                .build();
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film)
        );
        assertEquals("Фильм не соответствует критериям.", validationException.getMessage());
    }

    @Test
    public void filmNegativeDurationTest() {
        Film film = Film.builder()
                .id(1)
                .name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(-100)
                .build();
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film)
        );
        assertEquals("Фильм не соответствует критериям.", validationException.getMessage());
    }
}
