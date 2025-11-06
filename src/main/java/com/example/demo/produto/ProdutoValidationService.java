package com.example.demo.produto;

import com.example.demo.produto.Produto;
import com.example.demo.produto.ProdutoRepository;
import com.example.demo.shared.globalExceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProdutoValidationService {
    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto validateProdutoExistsAndGet(Long produtoId){
        Optional<Produto> produto = produtoRepository.findById(produtoId);
        if (produto.isEmpty()){
            throw new ResourceNotFoundException("produto");
        }
        return produto.get();
    }
    public void validateProdutoExists(Long produtoId){
        Optional<Produto> produto = produtoRepository.findById(produtoId);
        if (produto.isEmpty()){
            throw new ResourceNotFoundException("produto");
        }
    }

    public Produto validateProdutoExistsAndIsAtivaAndGet(Long produtoId){
        Produto  produto = validateProdutoExistsAndGet(produtoId);
        if (!produto.getAtivo())
            throw new ResourceNotFoundException("produto");
        return produto;
    }
}
