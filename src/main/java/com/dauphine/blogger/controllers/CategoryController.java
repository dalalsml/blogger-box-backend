package com.dauphine.blogger.controllers;

import com.dauphine.blogger.dtos.CategoryRequest;
import com.dauphine.blogger.models.Category;
import com.dauphine.blogger.models.Post;
import com.dauphine.blogger.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;
import java.net.URI;

@RestController
@RequestMapping({"/v1/categories", "/api/v1/categories", "/api/categories"})
@Tag(name = "Categories API", description = "CRUD endpoints for categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieve all categories or filter by name")
    public ResponseEntity<List<Category>> getAll(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(service.getAll(name));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by id")
    public ResponseEntity<Category> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new category")
    public ResponseEntity<Category> create(@RequestBody CategoryRequest request) {
        Category createdCategory = service.create(request.getName());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdCategory.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(createdCategory);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing category")
    public ResponseEntity<Category> updateName(@PathVariable UUID id, @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(service.updateName(id, request.getName()));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a sub property of an existing category")
    public ResponseEntity<Category> patchName(@PathVariable UUID id, @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(service.patchName(id, request.getName()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a category")
    public void deleteById(@PathVariable UUID id) {
        service.deleteById(id);
    }

    @GetMapping("/{id}/posts")
    @Operation(summary = "Get all posts of a certain category")
    public ResponseEntity<List<Post>> getPostsByCategory(@PathVariable UUID id) {
        // Ensure category exists before returning posts.
        service.getById(id);
        return ResponseEntity.ok(service.getPostsByCategoryId(id));
    }
}
