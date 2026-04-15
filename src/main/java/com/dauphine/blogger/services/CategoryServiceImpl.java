package com.dauphine.blogger.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

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
    public List<Category> getAll() {
        return repository.findAll();
    }
    
    @Override
    public Optional<Category> getById(UUID id) {
        return repository.findById(id);
    }
    
    @Override
    public Category create(String name) {
        Category category = new Category(name);
        return repository.save(category);
    }
    
    @Override
    public Optional<Category> updateName(UUID id, String name) {
        return repository.findById(id)
                .map(category -> {
                    category.setName(name);
                    return repository.save(category);
                });
    }

    @Override
    public Optional<Category> patchName(UUID id, String name) {
        return repository.findById(id)
                .map(category -> {
                    if (name != null && !name.isBlank()) {
                        category.setName(name);
                    }
                    return repository.save(category);
                });
    }
    
    @Override
    public boolean deleteById(UUID id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    @Override
    public List<Post> getPostsByCategoryId(UUID categoryId) {
        return postRepository.findByCategoryIdOrderByCreatedDateDesc(categoryId);
    }
}
