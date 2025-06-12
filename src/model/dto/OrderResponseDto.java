package model.dto;

import java.time.LocalDate;

public record OrderResponseDto(
        String username,
        String productName,
        LocalDate orderDate
) {
}
