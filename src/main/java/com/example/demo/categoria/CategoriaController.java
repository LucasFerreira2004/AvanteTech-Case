package com.example.demo.categoria;

import com.example.demo.categoria.dto.CategoriaRegisterDTO;
import com.example.demo.categoria.dto.CategoriaResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> saveCategoria(@RequestBody @Valid CategoriaRegisterDTO dto){
        CategoriaResponseDTO response = categoriaService.save(dto);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<PagedModel<CategoriaResponseDTO>> findAllCategoria(Pageable pageable){
        Page<CategoriaResponseDTO> page = categoriaService.findAll(pageable);
        return ResponseEntity.ok( new PagedModel<>(page));
    }

    @PutMapping("/{categoriaId}")
    public ResponseEntity<CategoriaResponseDTO> updateCategoria(@RequestBody @Valid CategoriaRegisterDTO dto, @PathVariable Long categoriaId){
        CategoriaResponseDTO response = categoriaService.update(dto, categoriaId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{categoriaId}")
    public ResponseEntity<CategoriaResponseDTO> softDeleteCategoria(@PathVariable Long categoriaId){
        categoriaService.softDelete(categoriaId);
        return ResponseEntity.ok().build();
    }
}
