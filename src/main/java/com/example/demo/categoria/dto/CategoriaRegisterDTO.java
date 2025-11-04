package com.example.demo.categoria.dto;

import jakarta.validation.constraints.NotBlank;
import org.antlr.v4.runtime.misc.NotNull;

public record CategoriaRegisterDTO(
        @NotBlank(message = "O nome nao pode ser vazio")
        String nome,
        @NotBlank(message = "A descricao não pode ser vazia")
        String descricao
) {
}
