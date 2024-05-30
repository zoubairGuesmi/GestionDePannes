package com.zoubair.lastusersproject.Repositories;

import com.zoubair.lastusersproject.entities.Role;
import com.zoubair.lastusersproject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

}
