package com.zoubair.lastusersproject.services.impl;

import com.zoubair.lastusersproject.Repositories.RoleRepository;
import com.zoubair.lastusersproject.Repositories.UserRepository;
import com.zoubair.lastusersproject.dto.UserDto;
import com.zoubair.lastusersproject.entities.Role;
import com.zoubair.lastusersproject.entities.User;
import com.zoubair.lastusersproject.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
//        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setAdress(userDto.getAdress());
        user.setEmail(userDto.getEmail());
        // encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role role = roleRepository.findByName("ROLE_USER");
        //Role role = checkRoleExist("ROLE_USER");
//        if(role == null){
//            role = checkRoleExist("ROLE_USER");
//        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public void saveTechnicien(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setAdress(userDto.getAdress());
        user.setEmail(userDto.getEmail());
        // encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = checkRoleExist("ROLE_TECHNICIEN");
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void UpdateUser(UserDto userDto) {
        User user = getCurrentUser();
//        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setAdress(userDto.getAdress());
        if(!passwordEncoder.matches(userDto.getPassword(), user.getPassword())){
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        userRepository.save(user);
    }

    @Override
    public Page<User> findAllUsers(String keyword, Pageable pageable) {
        return userRepository.findAllUsers(keyword, pageable);
    }

    @Override
    public Page<User> findAllTechniciens(String keyword, Pageable pageable) {
        return userRepository.findAllTechniciens(keyword, pageable);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> getAllTechniciens() {
        return userRepository.getAllTechniciens();
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

//    @Override
//    public User getCurrentUser() {
//        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
//        return userRepository.findByEmail(email);
//    }

    @Override
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        } else {
            throw new IllegalStateException("Unexpected principal type: " + principal.getClass().getName());
        }

        return userRepository.findByEmail(email);
    }


    private UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto();
//        String[] str = user.getName().split(" ");
//        userDto.setFirstName(str[0]);
//        userDto.setLastName(str[1]);
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setAdress(user.getAdress());
        return userDto;
    }

    private Role checkRoleExist(String role){
        Role role1 = roleRepository.findByName(role);
        if(role1 == null) {
            if (role.equals("ROLE_TECHNICIEN")) {
                role1.setName("ROLE_TECHNICIEN");
            } else if (role.equals("ROLE_USER")) {
                role1.setName("ROLE_USER");
            }
            return roleRepository.save(Objects.requireNonNull(role1));
        }
        return null;

    }
}
