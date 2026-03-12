package com.dauphine.blogger.controllers;

import com.dauphine.blogger.models.Category;
import com.dauphine.blogger.services.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.UUID;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<Category> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Category getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public Category create(@RequestParam String name) {
        return service.create(name);
    }

    @PutMapping("/{id}")
    public Category updateName(@PathVariable UUID id, @RequestParam String name) {
        return service.updateName(id, name);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable UUID id) {
        return service.deleteById(id);
    }
}
