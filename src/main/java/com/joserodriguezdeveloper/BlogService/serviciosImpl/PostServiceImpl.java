package com.joserodriguezdeveloper.BlogService.serviciosImpl;


import com.joserodriguezdeveloper.BlogService.dto.PostDTO;
import com.joserodriguezdeveloper.BlogService.modelos.*;
import com.joserodriguezdeveloper.BlogService.repositorio.CategoriaRepository;
import com.joserodriguezdeveloper.BlogService.repositorio.ImagenRepository;
import com.joserodriguezdeveloper.BlogService.repositorio.PostRepository;
import com.joserodriguezdeveloper.BlogService.servicios.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class PostServiceImpl implements IPostService {

    private final PostRepository postRepository;
    private final ImagenRepository imagenRepository;
    private final CategoriaRepository categoriaRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, ImagenRepository imagenRepository, CategoriaRepository categoriaRepository) {
        this.postRepository = postRepository;
        this.imagenRepository = imagenRepository;
        this.categoriaRepository = categoriaRepository;
    }



    @Override
    public List<Posts> listarTodos() {
        return postRepository.findAll();
    }
    @Override
    public Posts guardarDesdeDto(PostDTO dto) {
        // 1. Buscamos si el post ya existe por slug para actualizar o crear nuevo
        Posts post = postRepository.findBySlug(dto.getSlug())
                .orElse(new Posts());

        // 2. Mapeo de datos básicos
        post.setTitulo(dto.getTitulo());
        post.setSlug(dto.getSlug());
        post.setMetaDescripcion(dto.getMetaDescripcion());
        post.setIdAutor(dto.getIdAutor());
        post.setEstado(PostStatus.valueOf(dto.getEstado()));

        // 3. Vincular Imagen Destacada (El ID 1 que vimos en el JSON)
        if (dto.getImagenDestacada() != null && dto.getImagenDestacada().getIdImagen() != null) {
            Imagenes img = imagenRepository.findById(dto.getImagenDestacada().getIdImagen())
                    .orElseThrow(() -> new RuntimeException("Imagen destacada no encontrada con ID: " + dto.getImagenDestacada().getIdImagen()));
            post.setImagenDestacada(img);
        }

        // 4. Vincular Categorías
        if (dto.getCategorias() != null) {
            System.out.println("--- DEBUG: Entrando al if de categorias");
            Set<Categorias> categoriasSet = dto.getCategorias().stream()
                    .map(catDto -> categoriaRepository.findById(catDto.getIdCategoria())
                            .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + catDto.getIdCategoria())))
                    .collect(Collectors.toSet());
            System.out.println("--- DEBUG: Categorias mapeadas a entidades: " + categoriasSet.size());
            post.setCategorias(categoriasSet);
        }

        if (post.getBloquesDeContenido() != null) {
            post.getBloquesDeContenido().clear();
        } else {
            post.setBloquesDeContenido(new ArrayList<>());
        }

        if (dto.getBloquesDeContenido() != null) {
            for (PostDTO.BloqueSimpleDTO bloqueDto : dto.getBloquesDeContenido()) {
                BloquesPost bloque = BloquesPost.builder()
                        .tipoBloque(TipoBloque.valueOf(bloqueDto.getTipoBloque()))
                        .orden(bloqueDto.getOrden())
                        .contenido(bloqueDto.getContenido())
                        .post(post) // Relación bidireccional
                        .build();

                // Si el bloque tiene una imagen asociada (ej: bloque tipo IMAGEN)
                if (bloqueDto.getImagen() != null && bloqueDto.getImagen().getIdImagen() != null) {
                    Imagenes imgBloque = imagenRepository.findById(bloqueDto.getImagen().getIdImagen())
                            .orElse(null);
                    bloque.setImagen(imgBloque);
                }

                post.getBloquesDeContenido().add(bloque);
            }
        }
        return postRepository.save(post);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void eliminarPorSlug(String slug) {
        Posts post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("No se puede eliminar: Post no encontrado"));
        postRepository.delete(post);
    }


    @Override
    public Optional<Posts> buscarPorSlug(String slug) {
        return postRepository.findBySlug(slug);
    }

    @Override
    public List<Posts> buscarPostsFiltrados(String query, String categoria) {
        boolean tieneQuery = (query != null && !query.trim().isEmpty());
        boolean tieneCategoria = (categoria != null && !categoria.trim().isEmpty());

        if (!tieneQuery && !tieneCategoria) {
            return postRepository.findByEstadoOrderByFechaPublicacionDesc(PostStatus.PUBLICADO);
        }

        String q = tieneQuery ? query : "";
        String cat = tieneCategoria ? categoria : "";

        return postRepository.findByTituloContainingIgnoreCaseAndCategoriasNombreCategoriaContainingIgnoreCaseAndEstadoOrderByFechaPublicacionDesc(
                q, cat, PostStatus.PUBLICADO
        );
    }

}
