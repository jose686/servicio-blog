package com.joserodriguezdeveloper.BlogService.controlador;



import com.joserodriguezdeveloper.BlogService.dto.PostDTO;
import com.joserodriguezdeveloper.BlogService.modelos.Posts;
import com.joserodriguezdeveloper.BlogService.servicios.IPostService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final IPostService postService;

    @Autowired
    public PostController(IPostService postService) {
        this.postService = postService;
    }


    @GetMapping
    public ResponseEntity<List<Posts>> listarTodos() {
        List<Posts> posts = postService.listarTodos();
        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Posts>> listarTodos(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String categoria) {
        List<Posts> posts = postService.buscarPostsFiltrados(query, categoria);

        if (posts.isEmpty()) {

            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<?> crearPost(@RequestBody PostDTO dto) {
        //System.out.println("📩 Recibido DTO con título: " + dto.getTitulo());
        try {
            Posts postGuardado = postService.guardarDesdeDto(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(postGuardado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al procesar: " + e.getMessage());
        }
    }
    @GetMapping("/{slug}")
    public ResponseEntity<Posts> obtenerPorSlug(@PathVariable String slug) {
        return postService.buscarPorSlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<?> eliminarPost(@PathVariable String slug) {
        try {
            postService.eliminarPorSlug(slug);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar: " + e.getMessage());
        }
    }

}
