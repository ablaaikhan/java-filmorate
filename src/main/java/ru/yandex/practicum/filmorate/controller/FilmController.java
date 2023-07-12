package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Validated
public class FilmController {
    private final FilmService filmService;

    @PostMapping(path = "/films")
    public Film add(@RequestBody @Valid Film film) {
        return filmService.add(film);
    }

    @PutMapping(path = "/films")
    public Optional<Film> put(@RequestBody @Valid Film film) {
        return filmService.put(film);
    }

    @GetMapping(path = "/films")
    public Collection<Film> get() {
        return filmService.get();
    }

    @GetMapping(path = "films/{id}")
    public Film getFilmById(@PathVariable(value = "id") Long id) {
        return filmService.getFilmById(id);
    }

    @GetMapping(path = "films/popular")
    public List<Film> getPopular(@Positive @RequestParam(value = "count", defaultValue = "10") Long count) {
        return filmService.getPopular(count);
    }
}
