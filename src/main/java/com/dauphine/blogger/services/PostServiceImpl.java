package com.dauphine.blogger.services;

import com.dauphine.blogger.dtos.CreatePostRequest;
import com.dauphine.blogger.dtos.PatchPostRequest;
import com.dauphine.blogger.dtos.UpdatePostRequest;
import com.dauphine.blogger.exceptions.BadRequestException;
import com.dauphine.blogger.exceptions.PostNotFoundByIdException;
import com.dauphine.blogger.models.Category;
import com.dauphine.blogger.models.Post;
import com.dauphine.blogger.repositories.CategoryRepository;
import com.dauphine.blogger.repositories.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
    public List<Post> getAll(LocalDate date, String value) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (date != null) {
            start = date.atStartOfDay();
            end = date.plusDays(1).atStartOfDay();
        }

        String sanitizedValue = (value == null || value.isBlank()) ? null : value.trim();

        if (start == null && sanitizedValue == null) {
            return postRepository.findAllByOrderByCreatedDateDesc();
        }

        return postRepository.searchByValueAndDate(sanitizedValue, start, end);
    }

    @Override
    public Post getById(UUID id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundByIdException(id));
    }

    @Override
    public Post create(CreatePostRequest request) {
        if (request == null || request.getCategoryId() == null) {
            throw new BadRequestException("Category id is required");
        }
        if (request.getTitle() == null || request.getTitle().isBlank()
                || request.getContent() == null || request.getContent().isBlank()) {
            throw new BadRequestException("Title and content are required");
        }
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BadRequestException("Category does not exist"));
        return postRepository.save(new Post(
                request.getTitle().trim(),
                request.getContent().trim(),
                category
        ));
    }

    @Override
    public Post update(UUID id, UpdatePostRequest request) {
        if (request == null || request.getCategoryId() == null) {
            throw new BadRequestException("Category id is required");
        }
        if (request.getTitle() == null || request.getTitle().isBlank()
                || request.getContent() == null || request.getContent().isBlank()) {
            throw new BadRequestException("Title and content are required");
        }
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BadRequestException("Category does not exist"));
        Post post = getById(id);
        post.setTitle(request.getTitle().trim());
        post.setContent(request.getContent().trim());
        post.setCategory(category);
        return postRepository.save(post);
    }

    @Override
    public Post patchContent(UUID id, PatchPostRequest request) {
        if (request == null || request.getContent() == null || request.getContent().isBlank()) {
            throw new BadRequestException("Content is required");
        }
        Post post = getById(id);
        post.setContent(request.getContent().trim());
        return postRepository.save(post);
    }

    @Override
    public void deleteById(UUID id) {
        getById(id);
        postRepository.deleteById(id);
    }
}
