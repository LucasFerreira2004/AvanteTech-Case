package com.example.demo.categoria.dtos;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRegisterDTO(
        @NotBlank(message = "O nome nao pode ser vazio")
        String nome,
        @NotBlank(message = "A descricao não pode ser vazia")
        String descricao
) {
}
