package com.semilleros.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "integrantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Integrante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "semillero_id", nullable = false)
    private Semillero semillero;

    @Column(nullable = false, length = 40)
    private String codigo;

    @Column(nullable = false, length = 160)
    private String nombre;

    @Column(nullable = false, length = 160)
    private String programa;

    @Column(name = "es_lider", nullable = false)
    @Builder.Default
    private Boolean esLider = false;
}

