package model.dto;

public record UserResponseDto(
        String uuid,
        String username,
        String email,
        String role
) {
}
