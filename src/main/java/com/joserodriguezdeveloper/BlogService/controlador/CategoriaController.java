package com.joserodriguezdeveloper.BlogService.controlador;


import com.joserodriguezdeveloper.BlogService.modelos.Categorias;
import com.joserodriguezdeveloper.BlogService.servicios.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final ICategoriaService categoriaService;

    @Autowired
    public CategoriaController(ICategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }


    @GetMapping
    public ResponseEntity<List<Categorias>> obtenerCategorias() {
      List<Categorias> lista = categoriaService.listarTodas();
      return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<Categorias> crearCategoria(@RequestBody Categorias categoria) {
        return ResponseEntity.ok(categoriaService.guardar(categoria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCategoria (@PathVariable Long id){
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.ok().build();

    }
}