package com.fabioperettig.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "u.seq")
    @SequenceGenerator(name = "u.seq", sequenceName = "sequence_user", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(name = "USUARIO", nullable = false, unique = true)
    private String apelido;

    @Column(name = "NIVEL", nullable = false)
    private Integer nivel = 1;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Achievement> achievements = new ArrayList<>();

    ///usaremos LOMBOK

}
