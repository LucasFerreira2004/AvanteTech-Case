package com.example.demo.categoria;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    @Modifying
    @Query(nativeQuery = true, value = """
        UPDATE categoria SET ativo = false WHERE id = :categoryId
    """)
    void softDelete(Long categoryId);

    Page<Categoria> findByAtivoTrue(Pageable pageable);
    Optional<Categoria> findByIdAndAtivoTrue(Long id);
}
