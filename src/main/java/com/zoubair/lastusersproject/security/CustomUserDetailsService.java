package com.zoubair.lastusersproject.security;

import com.zoubair.lastusersproject.Repositories.UserRepository;
import com.zoubair.lastusersproject.entities.Role;
import com.zoubair.lastusersproject.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        UserDetails user1 = new CustomUserDetails(user);

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(user1.getUsername(),
                    user1.getPassword(),
                    mapRolesToAuthorities(user.getRoles()));
            //return new CustomUserDetails(user);
        }else{
            throw new UsernameNotFoundException("Invalid username or password.");
        }

    }

    private Collection < ? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
//        Collection < ? extends GrantedAuthority> mapRoles = roles.stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName()))
//                .collect(Collectors.toList());
//        return mapRoles;

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }



}
