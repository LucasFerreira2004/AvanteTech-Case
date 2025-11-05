package com.example.demo.produto.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoRegisterDTO(
        @NotBlank(message = "O nome nao pode ser vazio")
        String nome,
        @NotBlank(message = "A descricao nao pode ser vazia")
        String descricao,
        @NotNull(message = "o preco nao pode ser vazio")
        BigDecimal preco,
        @NotNull(message = "o id da categoria nao pode ser vazio")
        Long categoriaId
) {
}
