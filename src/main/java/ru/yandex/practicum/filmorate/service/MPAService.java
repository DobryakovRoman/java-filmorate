package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class MPAService {
    private final MPAStorage mpaStorage;

    @Autowired
    public MPAService(MPAStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<MPA> getMPAs() {
        return new ArrayList<>(mpaStorage.getAllMPAs());
    }

    public MPA getMPA(int id) {
        return mpaStorage.getMPA(id);
    }
}
