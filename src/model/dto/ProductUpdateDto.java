package model.dto;

public record ProductUpdateDto(
        String name,
        Integer qty,
        Double price
) {
}
