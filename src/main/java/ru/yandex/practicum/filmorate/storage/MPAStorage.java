package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface MPAStorage {
    MPA getMPA(int id);
    List<MPA> getAllMPAs();
}
