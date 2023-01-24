package ru.yandex.practicum.filmorate.storage;

public interface LikeStorage {

    void addLike(int filmId, Long userId);

    void removeLike(int filmId, Long userId);
}
