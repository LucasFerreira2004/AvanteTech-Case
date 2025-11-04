package com.example.demo.categoria;

import com.example.demo.categoria.dto.CategoriaRegisterDTO;
import com.example.demo.categoria.dto.CategoriaResponseDTO;
import com.example.demo.categoria.mappers.CategoriaMapper;
import com.example.demo.shared.globalExceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Page<CategoriaResponseDTO> findAll(Pageable pageable) {
        return categoriaRepository.findAll(pageable).map(x -> categoriaMapper.toResponseDto(x));
    }

    public CategoriaResponseDTO update(CategoriaRegisterDTO dto, Long categoriaId) {
        Optional<Categoria> categoria = categoriaRepository.findById(categoriaId);
        if (categoria.isEmpty()){
            throw new ResourceNotFoundException("categoria");
        }
        Categoria categoriaUpdate = categoriaMapper.toEntity(dto, categoriaId);
        categoriaRepository.save(categoriaUpdate);
        return categoriaMapper.toResponseDto(categoriaUpdate);
    }
}
