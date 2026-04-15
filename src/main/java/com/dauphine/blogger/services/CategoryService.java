package com.dauphine.blogger.services;

import com.dauphine.blogger.models.Post;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.dauphine.blogger.models.Category;

public interface CategoryService {
    List<Category> getAll();
    Optional<Category> getById(UUID id);
    Category create(String name);
    Optional<Category> updateName(UUID id, String name);
    Optional<Category> patchName(UUID id, String name);
    boolean deleteById(UUID id);
    List<Post> getPostsByCategoryId(UUID categoryId);
}
