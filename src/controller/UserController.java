package controller;

import mapper.UserMapper;
import model.dto.UserCreateDto;
import model.dto.UserLoginDto;
import model.dto.UserResponseDto;
import model.service.UserServiceImpl;

import java.util.List;

public class UserController {
    private final UserServiceImpl userService = new UserServiceImpl();

    public List<UserResponseDto> getAllUsers() {
        return userService.getUsers();
    }
    public UserResponseDto findUserByUuid(String uuid) {
        return userService.getUserUuid(uuid);
    }
    public UserResponseDto login(UserLoginDto userLoginDto) {
        return userService.loginUser(userLoginDto);
    }
    public UserResponseDto register(UserCreateDto userCreateDto){
        return userService.registerUser(
                UserMapper.mapUserCreateDtoToUser(userCreateDto)
        );
    }
    public UserResponseDto searchUserByUuid(String uuid) {
        return userService.getUserUuid(uuid);
    }

    public Boolean deleteUserByUuid(String uuid) {
        return userService.deleteUser(uuid);
    }
}
