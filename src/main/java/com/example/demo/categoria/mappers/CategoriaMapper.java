package com.example.demo.categoria.mappers;

import com.example.demo.categoria.Categoria;
import com.example.demo.categoria.dto.CategoriaRegisterDTO;
import com.example.demo.categoria.dto.CategoriaResponseDTO;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {
    public Categoria toEntity(CategoriaRegisterDTO dto){
        Categoria categoria = new Categoria();
        categoria.setDescricao(dto.descricao());
        categoria.setNome(dto.nome());
        categoria.setAtivo(true);
        return categoria;
    }

    public Categoria toEntity(CategoriaRegisterDTO dto, Long id){
        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setDescricao(dto.descricao());
        categoria.setNome(dto.nome());
        categoria.setAtivo(true);
        return categoria;
    }

    public CategoriaResponseDTO toResponseDto(Categoria categoria){
        CategoriaResponseDTO dto = new CategoriaResponseDTO(categoria.getId(),  categoria.getDescricao(), categoria.getNome());
        return dto;
    }

}
