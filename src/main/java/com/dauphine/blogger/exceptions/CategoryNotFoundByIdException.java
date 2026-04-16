package com.dauphine.blogger.exceptions;

import java.util.UUID;

public class CategoryNotFoundByIdException extends RuntimeException {
    public CategoryNotFoundByIdException(UUID id) {
        super("Category not found with id: " + id);
    }
}
