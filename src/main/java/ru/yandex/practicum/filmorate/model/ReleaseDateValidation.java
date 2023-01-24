package ru.yandex.practicum.filmorate.model;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReleaseDateValidator.class)
public @interface ReleaseDateValidation {

    String message() default "Ошибка валидации даты";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
