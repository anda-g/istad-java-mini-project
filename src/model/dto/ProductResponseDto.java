package model.dto;

import model.entites.Category;

public record ProductResponseDto (
        String uuid,
        String name,
        Double price,
        Integer quantity,
        Category category
){
}
