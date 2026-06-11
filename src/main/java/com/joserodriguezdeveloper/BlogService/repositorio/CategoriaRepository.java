package com.joserodriguezdeveloper.BlogService.repositorio;

import com.joserodriguezdeveloper.BlogService.modelos.Categorias;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categorias, Long> {

    Optional<Categorias> findBynombreCategoria(String  nombreCategoria);
}