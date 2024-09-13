package com.tunisietelecom.gestionDesPannes.services.impl;

import com.tunisietelecom.gestionDesPannes.Repositories.PanneRepository;
import com.tunisietelecom.gestionDesPannes.entities.Panne;
import com.tunisietelecom.gestionDesPannes.services.PanneService;
import com.tunisietelecom.gestionDesPannes.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PanneServiceImpl implements PanneService {

    private final PanneRepository panneRepository;
    private final UserService userService;

    @Override
    public void createPanne(Panne panne) {
        panne.setDateDeclaration(LocalDate.now());
        panne.setStatus("SIGNALE");
        panne.setDeclaredBy(userService.getCurrentUser());
        panneRepository.save(panne);
    }

    @Override
    public Page<Panne> findAllPanne(String keyword, Pageable pageable) {
        return panneRepository.findAllPannes(keyword, pageable);
    }

    @Override
    public Page<Panne> findAllPanneForClient(String keyword, String email, Pageable pageable) {
        return panneRepository.findAllPannesForClient(keyword, email, pageable);
    }

    @Override
    public Page<Panne> findAllPanneForTechnicien(String keyword, String email, Pageable pageable) {
        return panneRepository.findAllPannesForTechnicien(keyword, email, pageable);
    }

    @Override
    public Panne findPanneById(long id) {
        return panneRepository.getById(id);
    }

    @Override
    public void savePanne(Panne panne) {
        panneRepository.save(panne);
    }

}
