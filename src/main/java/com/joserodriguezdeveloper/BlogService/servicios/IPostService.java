package com.joserodriguezdeveloper.BlogService.servicios;



import com.joserodriguezdeveloper.BlogService.dto.PostDTO;
import com.joserodriguezdeveloper.BlogService.modelos.Posts;

import java.util.List;
import java.util.Optional;

public interface IPostService {
    List<Posts> listarTodos();


    Posts guardarDesdeDto(PostDTO dto);
    void eliminarPorSlug(String slug);
    Optional<Posts> buscarPorSlug(String slug);

    List<Posts> buscarPostsFiltrados(String query, String categoria);
}
