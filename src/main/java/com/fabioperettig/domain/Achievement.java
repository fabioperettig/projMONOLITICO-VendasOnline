package com.fabioperettig.domain;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "TB_ACHIEVEMENTS")
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ach.seq")
    @SequenceGenerator(name = "ach.seq", sequenceName = "sequence_achievements", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(name = "TITULO", nullable = false)
    private String titulo;

    @Column(name = "DESCRICAO", nullable = false)
    private String descricao;

    @Column(name = "DATA", nullable = false)
    private LocalDate dataConquista;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "usuario_id",
            foreignKey = @ForeignKey(name = "fk_usuario_achievement"),
            nullable = false
    )
    private User user;

    @Column(name = "PRIVADO", nullable = false)
    private Boolean privado;

    ///usaremos LOMBOK

}
