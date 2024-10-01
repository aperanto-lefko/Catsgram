package ru.yandex.practicum.catsgram.controller;


import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping({"/posts/","/posts/{id}"})
    public Post findPost(@PathVariable Optional<String> id) {
        if (id.isPresent()) {
            return postService.searchPostById(id.get());
        }
        throw new NotFoundException("id не указан");
    }
    /*@GetMapping
    public Collection<Post> findAll() {
        return postService.findAll();
    }*/
    @GetMapping
    public Collection<Post> findAll(@RequestParam (required = false) Map<String, String> allParam) {
        if(allParam.isEmpty()) {
            return postService.findLastTen();
        } return postService.findPostParam(allParam);
    }
//добавить @Valid

    @PostMapping
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }
}