package com.example.demo.categoria;

import com.example.demo.categoria.dto.CategoriaRegisterDTO;
import com.example.demo.categoria.dto.CategoriaResponseDTO;
import com.example.demo.categoria.mappers.CategoriaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private CategoriaMapper categoriaMapper;

    public CategoriaResponseDTO save(CategoriaRegisterDTO dto) {
        Categoria categoria = categoriaMapper.toEntity(dto);
        categoria = categoriaRepository.save(categoria);
        return  categoriaMapper.toResponseDto(categoria);
    }
}
