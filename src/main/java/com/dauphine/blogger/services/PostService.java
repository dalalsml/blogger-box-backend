package com.dauphine.blogger.services;

import com.dauphine.blogger.dtos.CreatePostRequest;
import com.dauphine.blogger.dtos.PatchPostRequest;
import com.dauphine.blogger.dtos.UpdatePostRequest;
import com.dauphine.blogger.models.Post;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PostService {
    List<Post> getAll(LocalDate date, String value);

    Post getById(UUID id);

    Post create(CreatePostRequest request);

    Post update(UUID id, UpdatePostRequest request);

    Post patchContent(UUID id, PatchPostRequest request);

    void deleteById(UUID id);
}
