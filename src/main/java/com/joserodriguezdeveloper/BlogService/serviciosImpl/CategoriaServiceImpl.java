package com.joserodriguezdeveloper.BlogService.serviciosImpl;


import com.joserodriguezdeveloper.BlogService.modelos.Categorias;
import com.joserodriguezdeveloper.BlogService.repositorio.CategoriaRepository;
import com.joserodriguezdeveloper.BlogService.servicios.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements ICategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<Categorias> listarTodas() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categorias guardar(Categorias categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public void eliminarCategoria(Long id){
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
        }


    }

}