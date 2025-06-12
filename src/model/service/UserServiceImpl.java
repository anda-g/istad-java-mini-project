package model.service;

import mapper.UserMapper;
import model.dto.UserLoginDto;
import model.dto.UserResponseDto;
import model.entites.User;
import model.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService{
    private final UserRepository userRepository = new UserRepository();
    @Override
    public List<UserResponseDto> getUsers() {
        List<User> users = userRepository.findAll();
        if(users.isEmpty()){
            return List.of();
        }
        List<UserResponseDto> userResponseDtoList = new ArrayList<>();
        users.forEach(user -> {
            userResponseDtoList.add(
                    UserMapper.mapUserToUserResponseDto(user)
            );
        });
        return userResponseDtoList;
    }

    @Override
    public UserResponseDto getUserUuid(String uuid) {
        User user = userRepository.findByUuid(uuid);
        if(user == null){
            return null;
        }
        return UserMapper.mapUserToUserResponseDto(user);
    }

    @Override
    public UserResponseDto registerUser(User user) {
        if(userRepository.save(user)){
            return UserMapper.mapUserToUserResponseDto(user);
        }
        return null;
    }

    @Override
    public UserResponseDto loginUser(UserLoginDto userLoginDto) {
       User user = userRepository.authUser(userLoginDto);
       if(user == null){
           return null;
       }
       return UserMapper.mapUserToUserResponseDto(user);
    }

    public Boolean deleteUser(String uuid) {
        return userRepository.deleteByUuid(uuid);
    }
}
