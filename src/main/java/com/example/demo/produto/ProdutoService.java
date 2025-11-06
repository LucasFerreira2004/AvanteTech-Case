package com.example.demo.produto;

import com.example.demo.categoria.Categoria;
import com.example.demo.categoria.CategoriaValidationService;
import com.example.demo.produto.Produto;
import com.example.demo.produto.ProdutoRepository;
import com.example.demo.produto.dtos.ProdutoRegisterDTO;
import com.example.demo.produto.dtos.ProdutoResponseDTO;
import com.example.demo.produto.mappers.ProdutoMapper;
import com.example.demo.shared.globalExceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private ProdutoMapper produtoMapper;
    @Autowired
    private CategoriaValidationService categoriaValidationService;
    @Autowired
    private ProdutoValidationService produtoValidationService;

    public ProdutoResponseDTO save(ProdutoRegisterDTO dto) {
        Categoria categoria = categoriaValidationService.validateCategoriaIsAtivaAndGet(dto.categoriaId());
        Produto produto = produtoMapper.toEntity(dto, categoria);
        produto = produtoRepository.save(produto);
        return  produtoMapper.toResponseDto(produto);
    }

    public Page<ProdutoResponseDTO> findAll(Pageable pageable) {
        Page<ProdutoResponseDTO> page = produtoRepository.findByAtivoTrue(pageable).map(x -> produtoMapper.toResponseDto(x));
        return page;
    }

    @Transactional
    public ProdutoResponseDTO update(ProdutoRegisterDTO dto, Long produtoId) {
        produtoValidationService.validateProdutoExistsAndIsAtivaAndGet(produtoId);
        Categoria categoria = categoriaValidationService.validateCategoriaIsAtivaAndGet(dto.categoriaId());

        Produto produtoUpdate = produtoMapper.toEntity(dto, categoria, produtoId);
        produtoRepository.save(produtoUpdate);
        return produtoMapper.toResponseDto(produtoUpdate);
    }

    @Transactional
    public void softDelete(Long produtoId) {
        produtoValidationService.validateProdutoExistsAndIsAtivaAndGet(produtoId);
        produtoRepository.softDelete(produtoId);
    }

    @Transactional
    public void softDeleteAllWithCategoriaId(Long categoriaId){
        Optional<List<Produto>> produtos =  produtoRepository.findByCategoriaIdAndAtivoTrue(categoriaId);
        if (produtos.isPresent()){
            produtos.get().forEach(produto -> produtoRepository.softDelete(produto.getId()));
        }
    }
}
