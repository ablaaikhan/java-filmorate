package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserStorage userStorage;

    public User add(User user) {
        log.info("Добавлен пользователь под именем " + user.getName());
        return userStorage.add(user);
    }

    public User put(User user) {
        log.info("Пользователь " + user.getName() + " под номерам ID - " + user.getId() + " обновлен");
        return userStorage.put(user);
    }

    public Collection<User> get() {
        return userStorage.get();
    }

    public User getUserByID(Long id) {
        log.info("Запрошен пользователем под ID {}", id);
        return userStorage.getUserByID(id);
    }

    public void addFriends(Long id, Long friendId) {
        User user = userStorage.getUserByID(id);
        User otherUser = userStorage.getUserByID(friendId);
        if (user.getId().equals(friendId)) {
            throw new IncorrectParameterException("Пользователь не может добавить самого себя");
        }
        user.getFriends().add(friendId);
        otherUser.getFriends().add(id);
        log.info("Пользователь {} добавил в друзья {}", user.getName(), otherUser.getName());
    }

    public void deleteFriends(Long id, Long friendId) {
        User user = userStorage.getUserByID(id);
        User otherUser = userStorage.getUserByID(friendId);
        if (!user.getFriends().contains(friendId) && user.getId().equals(id)) {
            throw new IncorrectParameterException("Пользователя " + otherUser.getName() + " нет в друзьях");
        }
        user.getFriends().remove(friendId);
        otherUser.getFriends().remove(id);
        log.info("Пользователь {} удалил из друзей {}", user.getName(), otherUser.getName());
    }

    public List<User> getFriends(Long id) {
        User user = userStorage.getUserByID(id);
        log.info("Возвращен список друзей пользователя {} ", user.getName());
        return user.getFriends().stream()
                .map(userStorage::getUserByID)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long id, Long otherID) {
        User user = userStorage.getUserByID(id);
        User otherUser = userStorage.getUserByID(otherID);
        Set<Long> userFriends = user.getFriends();
        return otherUser.getFriends().stream()
                .filter(userFriends::contains)
                .map(userStorage::getUserByID)
                .collect(Collectors.toList());
    }
}
