package com.dauphine.blogger.repositories;

import com.dauphine.blogger.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findByCategoryIdOrderByCreatedDateDesc(UUID categoryId);

    List<Post> findByCreatedDateBetween(LocalDateTime start, LocalDateTime end);

    List<Post> findAllByOrderByCreatedDateDesc();

    List<Post> findByCreatedDateBetweenOrderByCreatedDateDesc(LocalDateTime start, LocalDateTime end);

    @Query("""
            SELECT post
            FROM Post post
            WHERE (
                :value IS NULL
                OR UPPER(post.title) LIKE UPPER(CONCAT('%', :value, '%'))
                OR UPPER(post.content) LIKE UPPER(CONCAT('%', :value, '%'))
            )
            AND (
                :start IS NULL
                OR post.createdDate >= :start
            )
            AND (
                :end IS NULL
                OR post.createdDate < :end
            )
            ORDER BY post.createdDate DESC
            """)
    List<Post> searchByValueAndDate(
            @Param("value") String value,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
