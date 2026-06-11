package com.joserodriguezdeveloper.BlogService.repositorio;


import com.joserodriguezdeveloper.BlogService.modelos.PostStatus;
import com.joserodriguezdeveloper.BlogService.modelos.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Posts, Long> {
    Optional<Posts> findBySlug(String slug);
    // Devuelve TODO lo público, sin importar si tiene categorías o no.
    List<Posts> findByEstadoOrderByFechaPublicacionDesc(PostStatus estado);

    List<Posts> findByTituloContainingIgnoreCaseAndCategoriasNombreCategoriaContainingIgnoreCaseAndEstadoOrderByFechaPublicacionDesc(
            String titulo, String nombreCategoria, PostStatus estado
    );
}
