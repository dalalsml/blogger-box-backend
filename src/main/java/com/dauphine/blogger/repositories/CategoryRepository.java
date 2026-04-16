package com.dauphine.blogger.repositories;

import com.dauphine.blogger.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Query("""
            SELECT category
            FROM Category category
            WHERE UPPER(category.name) LIKE UPPER(CONCAT('%', :name, '%'))
            ORDER BY category.name ASC
            """)
    List<Category> findAllLikeName(@Param("name") String name);
}
