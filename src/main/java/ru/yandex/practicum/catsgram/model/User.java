package ru.yandex.practicum.catsgram.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode(of = { "email" })
public class User {
    Long id; //уникальный идентификатор пользователя
    String username; //имя пользователя
    String email; //электронная почта пользователя
    String password; //пароль пользователя
    Instant registrationDate; //дата и время регистрации
}
/*
"username": "Иван Петров",
"email": "mail@mail.ru",
"password": 12345

 */
