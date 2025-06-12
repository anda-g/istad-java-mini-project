package mapper;

import model.dto.UserCreateDto;
import model.dto.UserResponseDto;
import model.entites.User;

import java.util.UUID;

public class UserMapper {
    public static User mapUserCreateDtoToUser(UserCreateDto userCreateDto) {
        return new User(
                null,
                UUID.randomUUID().toString(),
                userCreateDto.username(),
                userCreateDto.email(),
                userCreateDto.password(),
                userCreateDto.role()
        );
    }

    public static UserResponseDto mapUserToUserResponseDto(User user) {
        return new UserResponseDto(
                user.getUuid(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }
}
