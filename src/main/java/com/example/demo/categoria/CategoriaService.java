package com.example.demo.categoria;

import com.example.demo.categoria.dtos.CategoriaRegisterDTO;
import com.example.demo.categoria.dtos.CategoriaResponseDTO;
import com.example.demo.categoria.mappers.CategoriaMapper;
import com.example.demo.shared.globalExceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
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
    @Autowired
    private CategoriaValidationService categoriaValidationService;

    public CategoriaResponseDTO save(CategoriaRegisterDTO dto) {
        Categoria categoria = categoriaMapper.toEntity(dto);
        categoria = categoriaRepository.save(categoria);
        return  categoriaMapper.toResponseDto(categoria);
    }

    public Page<CategoriaResponseDTO> findAll(Pageable pageable) {
        Page<CategoriaResponseDTO> page = categoriaRepository.findByAtivoTrue(pageable).map(x -> categoriaMapper.toResponseDto(x));
        return page;
    }

    @Transactional
    public CategoriaResponseDTO update(CategoriaRegisterDTO dto, Long categoriaId) {
        categoriaValidationService.validateCategoriaExists(categoriaId);

        Categoria categoriaUpdate = categoriaMapper.toEntity(dto, categoriaId);
        categoriaRepository.save(categoriaUpdate);
        return categoriaMapper.toResponseDto(categoriaUpdate);
    }

    @Transactional
    public void softDelete(Long categoriaId) {
        categoriaValidationService.validateCategoriaExists(categoriaId);
        categoriaRepository.softDelete(categoriaId);
    }
}
