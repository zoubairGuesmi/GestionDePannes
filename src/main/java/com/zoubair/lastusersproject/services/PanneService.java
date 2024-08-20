package com.zoubair.lastusersproject.services;

import com.zoubair.lastusersproject.entities.Panne;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PanneService {

    void createPanne(Panne panne);

    Page<Panne> findAllPanne(String keyword, Pageable pageable);

    Page<Panne> findAllPanneForClient(String keyword, String email, Pageable pageable);
    Page<Panne> findAllPanneForTechnicien(String keyword, String email, Pageable pageable);

    Panne findPanneById(long id);

    void savePanne(Panne panne);
}
