package com.example.demo.produto;

import com.example.demo.produto.ProdutoService;
import com.example.demo.produto.dtos.ProdutoRegisterDTO;
import com.example.demo.produto.dtos.ProdutoResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> saveProduto(@RequestBody @Valid ProdutoRegisterDTO dto){
        ProdutoResponseDTO response = produtoService.save(dto);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<PagedModel<ProdutoResponseDTO>> findAllProduto(Pageable pageable){
        Page<ProdutoResponseDTO> page = produtoService.findAll(pageable);
        return ResponseEntity.ok( new PagedModel<>(page));
    }

    @PutMapping("/{produtoId}")
    public ResponseEntity<ProdutoResponseDTO> updateProduto(@RequestBody @Valid ProdutoRegisterDTO dto, @PathVariable Long produtoId){
        ProdutoResponseDTO response = produtoService.update(dto, produtoId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{produtoId}")
    public ResponseEntity<ProdutoResponseDTO> softDeleteProduto(@PathVariable Long produtoId){
        produtoService.softDelete(produtoId);
        return ResponseEntity.ok().build();
    }
}
