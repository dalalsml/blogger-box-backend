package com.dauphine.blogger.repositories;

import com.dauphine.blogger.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findByCategoryIdOrderByCreatedDateDesc(UUID categoryId);

    List<Post> findByCreatedDateBetween(LocalDateTime start, LocalDateTime end);

    List<Post> findAllByOrderByCreatedDateDesc();

    List<Post> findByCreatedDateBetweenOrderByCreatedDateDesc(LocalDateTime start, LocalDateTime end);
}
