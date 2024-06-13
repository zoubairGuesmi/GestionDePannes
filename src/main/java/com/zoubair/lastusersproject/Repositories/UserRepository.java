package com.zoubair.lastusersproject.Repositories;

import com.zoubair.lastusersproject.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :kw, '%'))" +
            "OR LOWER(u.adress) LIKE LOWER(CONCAT('%', :kw, '%'))")
    Page<User> findAllUsers(@Param("kw") String kw, Pageable pageable);
}
