package com.joserodriguezdeveloper.BlogService.servicios;


import com.joserodriguezdeveloper.BlogService.modelos.Categorias;

import java.util.List;

public interface ICategoriaService {
    List<Categorias> listarTodas();

    Categorias guardar(Categorias categoria);

    void eliminarCategoria(Long id);
}
