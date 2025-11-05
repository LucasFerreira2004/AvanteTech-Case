package com.example.demo.categoria;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

@Data
@NoArgsConstructor
@Entity
@FilterDef(name = "ativoFilter", parameters = @ParamDef(name = "ativo", type = Boolean.class))
@Filter(name = "ativoFilter", condition = "ativo = :ativo")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, columnDefinition = "text")
    private String descricao;

    @Column(nullable = false)
    private Boolean ativo;
}
