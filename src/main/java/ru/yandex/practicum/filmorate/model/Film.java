package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class Film {

    private long id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @ReleaseDateValidation
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private MPA mpa;
    private int likes;
    private Set<Genre> genres = new HashSet<>();

    public Film(int id, String name, String description, LocalDate releaseDate, int duration,
                MPA mpa, int likes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.likes = likes;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_Date", releaseDate);
        values.put("duration", duration);
        values.put("mpa_id", mpa.getId());
        return values;
    }
}
