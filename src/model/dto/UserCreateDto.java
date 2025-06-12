package model.dto;

public record UserCreateDto(
        String username,
        String email,
        String password,
        String role
) {
}
