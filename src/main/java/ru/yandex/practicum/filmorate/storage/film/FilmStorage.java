package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Optional<Film> add(Film film);

    Optional<Film> put(Film film);

    Collection<Film> get();

    Film getFilmById(Long id);
}
