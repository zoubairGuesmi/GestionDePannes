package com.zoubair.lastusersproject.services;

import com.zoubair.lastusersproject.dto.UserDto;
import com.zoubair.lastusersproject.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    void saveUser(UserDto userDto);
    void saveTechnicien(UserDto userDto);
    void save(User user);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();

    User getCurrentUser();

    void UpdateUser(UserDto userDto);

    Page<User> findAllUsers(String keyword, Pageable pageable);
    Page<User> findAllTechniciens(String keyword, Pageable pageable);

    void deleteUser(Long id);
    User findUserById(Long id);

    List<User> getAllTechniciens();


}
