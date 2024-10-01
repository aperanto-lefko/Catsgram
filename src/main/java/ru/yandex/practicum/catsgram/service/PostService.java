package ru.yandex.practicum.catsgram.service;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.apache.tomcat.util.http.parser.Cookie;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import jakarta.validation.constraints.Positive;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


// Указываем, что класс PostService - является бином и его
// нужно добавить в контекст приложения
@Service
public class PostService {
    private final Map<Long, Post> posts = new LinkedHashMap<>();

    private final UserService userService;

    public PostService(UserService userService) {
        this.userService = userService;
    }

    private int page;

    private int size;

    public Collection<Post> findAll() {
        return posts.values();
    }

    public Collection<Post> findLastTen() { //исправить, чтобы выдавала последние 10
        if (posts.size() <= 10) {
            return posts.values();
        }
        return posts.keySet().stream()
                .filter(id -> id > (posts.size() - 10))
                .map(posts::get)
                .collect(Collectors.toList());
    }

    public Collection<Post> findPostParam(Map<String, String> allParam) {
        //строка запроса http://localhost:8080/posts?from=2&size=0&sort=desc&page=1

        //Optional<Integer> page = Optional.of(Integer.parseInt(allParam.get("page")));
        Optional<Integer> size = Optional.of(Integer.parseInt(allParam.get("size")));
        Optional<Integer> from = Optional.of(Integer.parseInt(allParam.get("from")));
        Optional<SortOrder> sorting = Optional.ofNullable(SortOrder.sortingMethod(allParam.get("sort")));
        if (from.get() < 0 || size.get() < 0 ) {
            throw new ConditionsNotMetException("Поле from/size не может быть отрицательным ");
        }
        Collection<Post> selectedPosts;
        if(size.get()==0) {
             selectedPosts = posts.keySet().stream()
                    .filter(id -> id >= from.get())
                    .map(posts::get)
                    .toList();
        } else {
            selectedPosts = posts.keySet().stream()
                    .filter(id -> id >= from.get())
                    .filter(id -> id < from.get() + size.get())
                    .map(posts::get)
                    .toList();
        }
        if (sorting.isPresent()) {
            if (sorting.get() == SortOrder.DESCENDING) {
                return selectedPosts.stream()
                        .map(Post::getId)
                        .sorted(Collections.reverseOrder())
                        .map(posts::get)
                        .toList();

            }
        }
        return selectedPosts;
    }


    public Post create(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }

        post.setId(getNextId());
        post.setPostDate(Instant.now());
        userService.findUserById(post.getAuthorId());
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    public Post searchPostById(String id) {
        Long idNew = Long.parseLong(id);
        if (posts.containsKey(idNew)) {
            return posts.get(idNew);
        }
        throw new NotFoundException("Пост с id = " + id + " не найден");
    }


    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public enum SortOrder {
        ASCENDING, DESCENDING;

        // Преобразует строку в элемент перечисления
        public static SortOrder sortingMethod(String order) {
            switch (order.toLowerCase()) {
                case "ascending":
                case "asc":
                    return ASCENDING;
                case "descending":
                case "desc":
                    return DESCENDING;
                default:
                    return null;
            }
        }
    }
}