package model.dto;

import java.time.LocalDate;
import java.util.List;

public record OrderCreateDto(
        String userUuid,
        List<String> productUuid,
        LocalDate orderDate
) {
}
