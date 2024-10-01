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
@EqualsAndHashCode(of = { "id" })
public class Post {
    Long id; //уникальный идентификатор сообщения
    long authorId; //пользователь, который создал сообщение
    String description; //текстовое описание сообщения
    Instant postDate; //дата и время создания сообщения


}
/*
"authorId":1,
 "description": "Сказка о царе Салтане"
 */
