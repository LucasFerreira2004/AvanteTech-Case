package com.example.demo.categoria;

import com.example.demo.categoria.dto.CategoriaRegisterDTO;
import com.example.demo.categoria.dto.CategoriaResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Object> findAll(){
        return  ResponseEntity.ok("ola mundo");
    }
}
