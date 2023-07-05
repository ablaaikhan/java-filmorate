package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/films")
@Slf4j
@RestController
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int generatorId = 1;
    private static final LocalDate RELEASE_DATE = LocalDate.of(1895, 12, 28);


    @PostMapping
    public Film add(@RequestBody @Valid Film film) throws ValidationException {
        check(film);
        film.setId(generatorId++);
        films.put(film.getId(), film);
        log.info("Добавлен фильм под названием " + film.getName());
        return film;
    }

    @PutMapping
    public Film put(@RequestBody @Valid Film film) {
        if (!films.containsKey(film.getId())) {
            log.info("Фильм " + film.getId() + " не найден");
            throw new Exception("Фильм " + film.getId() + " не найден");
        }
        films.put(film.getId(), film);
        log.info("Фильм " + film.getName() + " под номерам ID - " + film.getId() + " обновлен");
        return film;
    }

    @GetMapping
    public Collection<Film> get() {
        log.info("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    private void check(Film film) {
        if (film.getReleaseDate().isBefore(date)) {
            log.info("Дата публикации фильма раньше положенного, фильм должен быть опубликован не раньше чем - " + date);
            throw new ValidationException("Дата публикации фильма раньше положенного, фильм должен быть опубликован не раньше чем - " + date);
        }
    }
}
