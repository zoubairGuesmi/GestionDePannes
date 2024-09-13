package com.tunisietelecom.gestionDesPannes.Repositories;

import com.tunisietelecom.gestionDesPannes.entities.Panne;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PanneRepository extends JpaRepository<Panne, Long> {

//    @Query("SELECT p FROM Panne p WHERE " +
//            "(LOWER(p.description) LIKE LOWER(CONCAT('%', :kw, '%')))" +
//            "AND (p.assignedTo.email IS NOT NULL OR p.assignedTo.email IS NULL)")
//    Page<Panne> findAllPannes(@Param("kw") String kw, Pageable pageable);


    @Query("SELECT p FROM Panne p LEFT JOIN p.assignedTo t WHERE " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "AND (t IS NULL OR t.email IS NOT NULL)")
    Page<Panne> findAllPannes(@Param("kw") String kw, Pageable pageable);

    @Query("SELECT p FROM Panne p LEFT JOIN p.declaredBy t WHERE " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "AND (t IS NULL OR LOWER(t.email) = LOWER(:email))")
    Page<Panne> findAllPannesForClient(@Param("kw") String kw, @Param("email") String email, Pageable pageable);

    @Query("SELECT p FROM Panne p LEFT JOIN p.assignedTo t WHERE " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "AND LOWER(p.status) LIKE LOWER(CONCAT('%', :kw, '%')) "+
            "AND LOWER(t.email) = LOWER(:email)")
    Page<Panne> findAllPannesForTechnicien(@Param("kw") String kw, @Param("email") String email, Pageable pageable);

    Panne getById(Long id);
}
