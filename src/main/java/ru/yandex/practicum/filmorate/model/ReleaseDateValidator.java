package ru.yandex.practicum.filmorate.model;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDateValidation, LocalDate> {

    public static final LocalDate FIRST_FILM_DATE = LocalDate.of(1895,12,28);

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate.isAfter(FIRST_FILM_DATE);
    }
}
