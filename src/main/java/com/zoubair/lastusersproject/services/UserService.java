package com.zoubair.lastusersproject.services;

import com.zoubair.lastusersproject.dto.UserDto;
import com.zoubair.lastusersproject.entities.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}
