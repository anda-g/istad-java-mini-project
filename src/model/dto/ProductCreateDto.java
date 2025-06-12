package model.dto;

import model.entites.Category;


public record ProductCreateDto(
        String name,
        Double price,
        Integer quantity,
        Category category
) {

}
