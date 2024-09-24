package ru.yandex.practicum.catsgram.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode(of = { "id" })
public class Image {
    Long id; // уникальный идентификатор изображения
    long postId; //уникальный идентификатор поста, к которому прикреплено изображение
    String originalFileName; //имя файла, который содержит изображение
    String filePath; //путь, по которому изображение было сохранено
}
