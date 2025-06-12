package model.service;

import model.dto.UserLoginDto;
import model.dto.UserResponseDto;
import model.entites.User;

import java.util.List;

public interface UserService {
    List<UserResponseDto> getUsers();
    UserResponseDto getUserUuid(String uuid);
    UserResponseDto registerUser(User user);
    UserResponseDto loginUser(UserLoginDto userLoginDto);

}
