package com.tunisietelecom.gestionDesPannes.Repositories;

import com.tunisietelecom.gestionDesPannes.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

}
