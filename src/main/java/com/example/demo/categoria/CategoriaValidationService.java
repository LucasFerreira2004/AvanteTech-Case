package com.example.demo.categoria;

import com.example.demo.shared.globalExceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CategoriaValidationService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public Categoria validateCategoriaExistsAndGet(Long categoriaId){
        Optional<Categoria> categoria = categoriaRepository.findById(categoriaId);
        if (categoria.isEmpty()){
            throw new ResourceNotFoundException("categoria");
        }
        return categoria.get();
    }
    public void validateCategoriaExists(Long categoriaId){
        Optional<Categoria> categoria = categoriaRepository.findById(categoriaId);
        if (categoria.isEmpty()){
            throw new ResourceNotFoundException("categoria");
        }
    }
}
