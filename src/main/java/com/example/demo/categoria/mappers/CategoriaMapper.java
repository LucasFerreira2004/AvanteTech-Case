package com.example.demo.categoria.mappers;

import com.example.demo.categoria.Categoria;
import com.example.demo.categoria.dtos.CategoriaRegisterDTO;
import com.example.demo.categoria.dtos.CategoriaResponseDTO;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class CategoriaMapper {
    public Categoria toEntity(CategoriaRegisterDTO dto){
        Categoria categoria = new Categoria();
        categoria.setNome(dto.nome().toLowerCase(Locale.ROOT));
        categoria.setDescricao(dto.descricao().toLowerCase(Locale.ROOT));
        categoria.setAtivo(true);
        return categoria;
    }

    public Categoria toEntity(CategoriaRegisterDTO dto, Long id){
        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setNome(dto.nome().toLowerCase(Locale.ROOT));
        categoria.setDescricao(dto.descricao().toLowerCase(Locale.ROOT));
        categoria.setAtivo(true);
        return categoria;
    }

    public CategoriaResponseDTO toResponseDto(Categoria categoria){
        CategoriaResponseDTO dto = new CategoriaResponseDTO(categoria.getId(), categoria.getNome(), categoria.getDescricao());
        return dto;
    }

}
