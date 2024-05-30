package com.zoubair.lastusersproject.Repositories;

import com.zoubair.lastusersproject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByName(String username);

}
