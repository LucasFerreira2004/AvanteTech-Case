package com.example.demo.produto.dtos;

import java.math.BigDecimal;

public record ProdutoResponseDTO(
   Long id,
   String nome,
   String descricao,
   BigDecimal preco,
   Long categoriaId
) {}
