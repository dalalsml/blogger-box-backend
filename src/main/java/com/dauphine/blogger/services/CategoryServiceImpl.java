package com.dauphine.blogger.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dauphine.blogger.exceptions.BadRequestException;
import com.dauphine.blogger.exceptions.CategoryNotFoundByIdException;
import com.dauphine.blogger.models.Category;
import com.dauphine.blogger.models.Post;
import com.dauphine.blogger.repositories.CategoryRepository;
import com.dauphine.blogger.repositories.PostRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final PostRepository postRepository;

    public CategoryServiceImpl(CategoryRepository repository, PostRepository postRepository) {
        this.repository = repository;
        this.postRepository = postRepository;
    }

    @Override
    public List<Category> getAll(String name) {
        if (name != null && !name.isBlank()) {
            return repository.findAllLikeName(name.trim());
        }
        return repository.findAll();
    }
    
    @Override
    public Category getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundByIdException(id));
    }
    
    @Override
    public Category create(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Category name is required");
        }
        Category category = new Category(name.trim());
        return repository.save(category);
    }
    
    @Override
    public Category updateName(UUID id, String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Category name is required");
        }
        Category category = getById(id);
        category.setName(name.trim());
        return repository.save(category);
    }

    @Override
    public Category patchName(UUID id, String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Category name is required");
        }
        Category category = getById(id);
        category.setName(name.trim());
        return repository.save(category);
    }
    
    @Override
    public void deleteById(UUID id) {
        getById(id);
        repository.deleteById(id);
    }

    @Override
    public List<Post> getPostsByCategoryId(UUID categoryId) {
        getById(categoryId);
        return postRepository.findByCategoryIdOrderByCreatedDateDesc(categoryId);
    }
}
