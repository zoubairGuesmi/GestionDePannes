package com.zoubair.lastusersproject.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="pannes")
public class Panne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private LocalDateTime dateDeclaration;
    private LocalDateTime dateResolution;
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User declaredBy;

    @ManyToOne
    @JoinColumn(name = "technician_id")
    private User assignedTo;
}
