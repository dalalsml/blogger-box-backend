package com.dauphine.blogger.services;

import com.dauphine.blogger.dtos.CreatePostRequest;
import com.dauphine.blogger.dtos.PatchPostRequest;
import com.dauphine.blogger.dtos.UpdatePostRequest;
import com.dauphine.blogger.models.Category;
import com.dauphine.blogger.models.Post;
import com.dauphine.blogger.repositories.CategoryRepository;
import com.dauphine.blogger.repositories.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    public PostServiceImpl(PostRepository postRepository, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Post> getAll(LocalDate date) {
        if (date == null) {
            return postRepository.findAllByOrderByCreatedDateDesc();
        }
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        return postRepository.findByCreatedDateBetweenOrderByCreatedDateDesc(start, end);
    }

    @Override
    public Optional<Post> getById(UUID id) {
        return postRepository.findById(id);
    }

    @Override
    public Optional<Post> create(CreatePostRequest request) {
        if (request == null || request.getCategoryId() == null) {
            return Optional.empty();
        }
        if (request.getTitle() == null || request.getTitle().isBlank()
                || request.getContent() == null || request.getContent().isBlank()) {
            return Optional.empty();
        }
        return categoryRepository.findById(request.getCategoryId())
                .map(category -> postRepository.save(new Post(
                        request.getTitle().trim(),
                        request.getContent().trim(),
                        category
                )));
    }

    @Override
    public Optional<Post> update(UUID id, UpdatePostRequest request) {
        if (request == null || request.getCategoryId() == null) {
            return Optional.empty();
        }
        if (request.getTitle() == null || request.getTitle().isBlank()
                || request.getContent() == null || request.getContent().isBlank()) {
            return Optional.empty();
        }
        Optional<Category> category = categoryRepository.findById(request.getCategoryId());
        if (category.isEmpty()) {
            return Optional.empty();
        }
        return postRepository.findById(id)
                .map(post -> {
                    post.setTitle(request.getTitle().trim());
                    post.setContent(request.getContent().trim());
                    post.setCategory(category.get());
                    return postRepository.save(post);
                });
    }

    @Override
    public Optional<Post> patchContent(UUID id, PatchPostRequest request) {
        return postRepository.findById(id)
                .map(post -> {
                    if (request.getContent() != null && !request.getContent().isBlank()) {
                        post.setContent(request.getContent());
                    }
                    return postRepository.save(post);
                });
    }

    @Override
    public boolean deleteById(UUID id) {
        if (!postRepository.existsById(id)) {
            return false;
        }
        postRepository.deleteById(id);
        return true;
    }
}
