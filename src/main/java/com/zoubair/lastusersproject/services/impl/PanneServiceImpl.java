package com.zoubair.lastusersproject.services.impl;

import com.zoubair.lastusersproject.Repositories.PanneRepository;
import com.zoubair.lastusersproject.entities.Panne;
import com.zoubair.lastusersproject.services.PanneService;
import com.zoubair.lastusersproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PanneServiceImpl implements PanneService {

    private final PanneRepository panneRepository;
    private final UserService userService;

    @Override
    public void createPanne(Panne panne) {
        panne.setDateDeclaration(LocalDateTime.now());
        panne.setStatus("CREATED");
        panne.setDeclaredBy(userService.getCurrentUser());
        panneRepository.save(panne);
    }
}
