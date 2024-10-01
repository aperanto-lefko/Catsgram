package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public User create(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }

        if (users.values().stream()
                .map(email -> email.getEmail())
                .anyMatch(email -> email.equals(user.getEmail()))) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        return user;
    }

    public User update(User newUser) {
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (newUser.getEmail() == null || newUser.getUsername() == null || newUser.getPassword() == null) {
                throw new ConditionsNotMetException("Данные пользователя не обновлены");
            }
            if (!(newUser.getEmail() == null) && !(newUser.getEmail().isBlank())) {
                if (users.values().stream()
                        .map(email -> email.getEmail())
                        .anyMatch(email -> email.equals(newUser.getEmail()))) {
                    throw new DuplicatedDataException("Этот имейл уже используется");
                }

                oldUser.setEmail(newUser.getEmail());
            }
            if (!(newUser.getUsername() == null) && !(newUser.getUsername().isBlank())) {
                oldUser.setUsername(newUser.getUsername());
            }
            if (!(newUser.getPassword() == null) && !(newUser.getPassword().isBlank())) {
                oldUser.setPassword(newUser.getPassword());
            }
            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }
public Optional<User> findUserById(Long idUser){
        Optional<User> searchUser = users.keySet()
                .stream()
                .filter(id->id==idUser)
                .map(users::get)
                .findFirst();
             if (searchUser.isEmpty()){
            throw new ConditionsNotMetException("«Автор с id = " + idUser + " не найден");
        }
        return searchUser;
}
    public User searchUserById(String id) {
          Long idNew = Long.parseLong(id);
            if (users.containsKey(idNew)) {
                return users.get(idNew);
            } throw new NotFoundException("Пост с id = " + id + " не найден");
          }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
