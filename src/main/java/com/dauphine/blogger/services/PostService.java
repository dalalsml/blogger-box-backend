package com.dauphine.blogger.services;

import com.dauphine.blogger.dtos.CreatePostRequest;
import com.dauphine.blogger.dtos.PatchPostRequest;
import com.dauphine.blogger.dtos.UpdatePostRequest;
import com.dauphine.blogger.models.Post;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostService {
    List<Post> getAll(LocalDate date);

    Optional<Post> getById(UUID id);

    Optional<Post> create(CreatePostRequest request);

    Optional<Post> update(UUID id, UpdatePostRequest request);

    Optional<Post> patchContent(UUID id, PatchPostRequest request);

    boolean deleteById(UUID id);
}
