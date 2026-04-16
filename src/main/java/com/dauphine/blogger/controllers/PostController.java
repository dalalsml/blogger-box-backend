package com.dauphine.blogger.controllers;

import com.dauphine.blogger.dtos.CreatePostRequest;
import com.dauphine.blogger.dtos.PatchPostRequest;
import com.dauphine.blogger.dtos.UpdatePostRequest;
import com.dauphine.blogger.models.Post;
import com.dauphine.blogger.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
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

@RestController
@RequestMapping("/v1/posts")
@Tag(name = "Posts API", description = "CRUD endpoints for posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all posts ordered by creation date")
    public List<Post> getAll(
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
        return postService.getAll(date);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a post by id")
    public ResponseEntity<Post> getById(@PathVariable UUID id) {
        return postService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new post")
    public ResponseEntity<Post> create(@RequestBody CreatePostRequest request) {
        return postService.create(request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing post")
    public ResponseEntity<Post> update(@PathVariable UUID id, @RequestBody UpdatePostRequest request) {
        return postService.update(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a sub property of an existing post")
    public ResponseEntity<Post> patch(@PathVariable UUID id, @RequestBody PatchPostRequest request) {
        return postService.patchContent(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing post")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!postService.deleteById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
