package com.dauphine.blogger.services;

import com.dauphine.blogger.models.Post;

import java.util.List;
import java.util.UUID;

import com.dauphine.blogger.models.Category;

public interface CategoryService {
    List<Category> getAll(String name);

    Category getById(UUID id);

    Category create(String name);

    Category updateName(UUID id, String name);

    Category patchName(UUID id, String name);

    void deleteById(UUID id);

    List<Post> getPostsByCategoryId(UUID categoryId);
}
