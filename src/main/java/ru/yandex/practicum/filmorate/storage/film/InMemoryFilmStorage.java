package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ParameterNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private Long generatorId = 1L;
    private final LocalDate date = LocalDate.of(1895, 12, 28);


    @Override
    public Optional<Film> add(@RequestBody Film film) {
        validate(film);
        film.setId(generatorId++);
        films.put(film.getId(), film);
        return Optional.of(film);
    }

    @Override
    public Optional<Film> put(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ParameterNotFoundException("Фильм " + film.getId() + " не найден");
        }
        films.put(film.getId(), film);
        return Optional.of(film);
    }

    @Override
    public Collection<Film> get() {
        log.info("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @Override
    public Film getFilmById(Long id) {
        if (!films.containsKey(id)) {
            throw new ParameterNotFoundException("Фильм " + id + " не найден");
        }
        return films.get(id);
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(date)) {
            log.info("Дата публикации фильма раньше положенного, фильм должен быть опубликован не раньше чем - " + date);
            throw new ValidationException("Дата публикации фильма раньше положенного, фильм должен быть опубликован не раньше чем - " + date);
        }
    }
}
