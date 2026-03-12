package com.dauphine.blogger.services;

import java.util.List;
import java.util.UUID;

import com.dauphine.blogger.models.Category;

public interface CategoryService {
    List<Category> getAll();
    Category getById(UUID id);
    Category create(String name);
    Category updateName(UUID id, String name);
    boolean deleteById(UUID id);
}
