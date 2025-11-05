package com.example.demo.produto.mappers;

import com.example.demo.categoria.Categoria;
import com.example.demo.categoria.dtos.CategoriaRegisterDTO;
import com.example.demo.categoria.dtos.CategoriaResponseDTO;
import com.example.demo.produto.Produto;
import com.example.demo.produto.dtos.ProdutoRegisterDTO;
import com.example.demo.produto.dtos.ProdutoResponseDTO;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ProdutoMapper {
    public Produto toEntity(ProdutoRegisterDTO dto, Categoria categoria) {
        Produto produto = new Produto();
        produto.setNome(dto.nome().toUpperCase(Locale.ROOT));
        produto.setDescricao(dto.descricao().toUpperCase(Locale.ROOT));
        produto.setPreco(dto.preco());
        produto.setCategoria(categoria);
        produto.setAtivo(true);
        return produto;
    }

    public Produto toEntity(ProdutoRegisterDTO dto, Categoria categoria, Long produtoId){
        Produto produto = new Produto();
        produto.setId(produtoId);
        produto.setNome(dto.nome().toLowerCase(Locale.ROOT));
        produto.setDescricao(dto.descricao().toLowerCase(Locale.ROOT));
        produto.setPreco(dto.preco());
        produto.setCategoria(categoria);
        produto.setAtivo(true);
        return produto;
    }

    public ProdutoResponseDTO toResponseDto(Produto produto){
        ProdutoResponseDTO dto = new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getCategoria().getId()
        );
        return dto;
    }

}
