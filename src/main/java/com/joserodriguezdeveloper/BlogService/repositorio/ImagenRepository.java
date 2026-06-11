package com.joserodriguezdeveloper.BlogService.repositorio;


import com.joserodriguezdeveloper.BlogService.modelos.Imagenes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagenRepository extends JpaRepository<Imagenes, Long> {
}
