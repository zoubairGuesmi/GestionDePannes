package com.zoubair.lastusersproject.Repositories;

import com.zoubair.lastusersproject.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User getById(Long id);

    @Query("SELECT u FROM User u WHERE " +
            "'ROLE_USER' IN (SELECT r.name FROM u.roles r)" +
            "AND (LOWER(u.firstName) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :kw, '%'))" +
            "OR LOWER(u.adress) LIKE LOWER(CONCAT('%', :kw, '%')))")
    Page<User> findAllUsers(@Param("kw") String kw, Pageable pageable);



    @Query("SELECT u FROM User u WHERE " +
            "'ROLE_TECHNICIEN' IN (SELECT r.name FROM u.roles r)" +
            "AND (LOWER(u.firstName) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :kw, '%'))" +
            "OR LOWER(u.adress) LIKE LOWER(CONCAT('%', :kw, '%')))")
    Page<User> findAllTechniciens(@Param("kw") String kw, Pageable pageable);

    @Query("SELECT u FROM User u WHERE " +
            "'ROLE_TECHNICIEN' IN (SELECT r.name FROM u.roles r)")
    List<User> getAllTechniciens();


}
