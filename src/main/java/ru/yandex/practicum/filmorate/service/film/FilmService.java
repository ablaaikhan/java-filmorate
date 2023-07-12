package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film add(Film film) {
        log.info("Добавлен фильм под названием " + film.getName());
        return filmStorage.add(film);
    }

    public Film put(Film film) {
        log.info("Фильм " + film.getName() + " под номерам ID - " + film.getId() + " обновлен");
        return filmStorage.put(film);
    }

    public Collection<Film> get() {
        return filmStorage.get();
    }

    public Film getFilmById(Long id) {
        log.info("Запрошен фильм под ID {}", id);
        return filmStorage.getFilmById(id);
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserByID(userId);
        Set<Long> userLikes = film.getLikes();
        if (userLikes.contains(userId)) {
            log.info("Лайк уже был поставлен пользователем {}", user.getName());
            throw new IncorrectParameterException("Лайк уже был поставлен пользователем " + user.getName());
        }
        userLikes.add(user.getId());
        log.info("Пользователь {} поставил лайк на фильм {}", user.getName(), film.getName());
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserByID(userId);
        Set<Long> userLikes = film.getLikes();
        if (!userLikes.contains(userId)) {
            log.info("Лайк не был поставлен пользователем {}", user.getName());
            throw new IncorrectParameterException("Лайк не был поставлен пользователем " + user.getName());
        }
        userLikes.remove(userId);
        log.info("Пользователь {} удалили лайк", user.getName());
    }

    public List<Film> getSortFilm(Long count) {
        return filmStorage.get().stream()
                .sorted(Comparator.comparingInt(f -> f.getLikes().size() * (-1)))
                .limit(count)
                .collect(Collectors.toList());
    }
}
