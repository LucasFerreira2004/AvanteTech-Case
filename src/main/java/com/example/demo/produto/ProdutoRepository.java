package com.example.demo.produto;

import com.example.demo.produto.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Modifying
    @Query(nativeQuery = true, value = """
        update produto 
        set ativo = false 
        where id = :categoryId
    """)
    void softDelete(Long categoryId);

    @Query("select p from Produto p " +
            "where p.ativo = true and p.categoria.ativo = true")
    Page<Produto> findByAtivoTrue(Pageable pageable);
    Optional<Produto> findByIdAndAtivoTrue(Long id);
    Optional<List<Produto>> findByCategoriaIdAndAtivoTrue(Long categoriaId);
}
