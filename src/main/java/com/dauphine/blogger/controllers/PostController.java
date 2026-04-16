package com.dauphine.blogger.controllers;

import com.dauphine.blogger.dtos.CreatePostRequest;
import com.dauphine.blogger.dtos.PatchPostRequest;
import com.dauphine.blogger.dtos.UpdatePostRequest;
import com.dauphine.blogger.models.Post;
import com.dauphine.blogger.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.net.URI;

@RestController
@RequestMapping("/v1/posts")
@Tag(name = "Posts API", description = "CRUD endpoints for posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all posts ordered by creation date or filter by date/value")
    public ResponseEntity<List<Post>> getAll(
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date,
            @RequestParam(required = false) String value) {
        return ResponseEntity.ok(postService.getAll(date, value));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a post by id")
    public ResponseEntity<Post> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(postService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new post")
    public ResponseEntity<Post> create(@RequestBody CreatePostRequest request) {
        Post createdPost = postService.create(request);
        return ResponseEntity
                .created(URI.create("/v1/posts/" + createdPost.getId()))
                .body(createdPost);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing post")
    public ResponseEntity<Post> update(@PathVariable UUID id, @RequestBody UpdatePostRequest request) {
        return ResponseEntity.ok(postService.update(id, request));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a sub property of an existing post")
    public ResponseEntity<Post> patch(@PathVariable UUID id, @RequestBody PatchPostRequest request) {
        return ResponseEntity.ok(postService.patchContent(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an existing post")
    public void delete(@PathVariable UUID id) {
        postService.deleteById(id);
    }
}
