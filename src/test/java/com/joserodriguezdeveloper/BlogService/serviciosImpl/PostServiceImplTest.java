package com.joserodriguezdeveloper.BlogService.serviciosImpl;

import com.joserodriguezdeveloper.BlogService.dto.PostDTO;
import com.joserodriguezdeveloper.BlogService.modelos.*;
import com.joserodriguezdeveloper.BlogService.repositorio.CategoriaRepository;
import com.joserodriguezdeveloper.BlogService.repositorio.ImagenRepository;
import com.joserodriguezdeveloper.BlogService.repositorio.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private ImagenRepository imagenRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private PostServiceImpl postService;

    // ==========================================
    // TESTS: listarTodos()
    // ==========================================

    @Test
    void listarTodos_ShouldReturnListOfPosts() {
        // Arrange
        Posts post1 = Posts.builder().idPost(1L).titulo("Post 1").build();
        Posts post2 = Posts.builder().idPost(2L).titulo("Post 2").build();
        List<Posts> mockPosts = Arrays.asList(post1, post2);

        when(postRepository.findAll()).thenReturn(mockPosts);

        // Act
        List<Posts> resultado = postService.listarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Post 1", resultado.get(0).getTitulo());
        assertEquals("Post 2", resultado.get(1).getTitulo());
        verify(postRepository, times(1)).findAll();
    }

    // ==========================================
    // TESTS: buscarPorSlug(String slug)
    // ==========================================

    @Test
    void buscarPorSlug_WhenPostExists_ShouldReturnOptionalWithPost() {
        // Arrange
        String slug = "mi-primer-post";
        Posts mockPost = Posts.builder().idPost(1L).titulo("Mi Primer Post").slug(slug).build();

        when(postRepository.findBySlug(slug)).thenReturn(Optional.of(mockPost));

        // Act
        Optional<Posts> resultado = postService.buscarPorSlug(slug);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Mi Primer Post", resultado.get().getTitulo());
        verify(postRepository, times(1)).findBySlug(slug);
    }

    @Test
    void buscarPorSlug_WhenPostDoesNotExist_ShouldReturnEmptyOptional() {
        // Arrange
        String slug = "inexistente";
        when(postRepository.findBySlug(slug)).thenReturn(Optional.empty());

        // Act
        Optional<Posts> resultado = postService.buscarPorSlug(slug);

        // Assert
        assertFalse(resultado.isPresent());
        verify(postRepository, times(1)).findBySlug(slug);
    }

    // ==========================================
    // TESTS: eliminarPorSlug(String slug)
    // ==========================================

    @Test
    void eliminarPorSlug_WhenPostExists_ShouldDeletePost() {
        // Arrange
        String slug = "post-a-eliminar";
        Posts mockPost = Posts.builder().idPost(1L).titulo("Post a Eliminar").slug(slug).build();

        when(postRepository.findBySlug(slug)).thenReturn(Optional.of(mockPost));
        doNothing().when(postRepository).delete(mockPost);

        // Act
        postService.eliminarPorSlug(slug);

        // Assert
        verify(postRepository, times(1)).findBySlug(slug);
        verify(postRepository, times(1)).delete(mockPost);
    }

    @Test
    void eliminarPorSlug_WhenPostDoesNotExist_ShouldThrowRuntimeException() {
        // Arrange
        String slug = "inexistente";
        when(postRepository.findBySlug(slug)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            postService.eliminarPorSlug(slug);
        });

        assertEquals("No se puede eliminar: Post no encontrado", exception.getMessage());
        verify(postRepository, times(1)).findBySlug(slug);
        verify(postRepository, never()).delete(any(Posts.class));
    }

    // ==========================================
    // TESTS: buscarPostsFiltrados(String query, String categoria)
    // ==========================================

    @Test
    void buscarPostsFiltrados_WhenNoQueryAndNoCategory_ShouldReturnAllPublicados() {
        // Arrange
        Posts post = Posts.builder().idPost(1L).titulo("Post Publicado").estado(PostStatus.PUBLICADO).build();
        List<Posts> mockPosts = Collections.singletonList(post);

        when(postRepository.findByEstadoOrderByFechaPublicacionDesc(PostStatus.PUBLICADO)).thenReturn(mockPosts);

        // Act
        List<Posts> resultado = postService.buscarPostsFiltrados("", "");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(postRepository, times(1)).findByEstadoOrderByFechaPublicacionDesc(PostStatus.PUBLICADO);
        verify(postRepository, never()).findByTituloContainingIgnoreCaseAndCategoriasNombreCategoriaContainingIgnoreCaseAndEstadoOrderByFechaPublicacionDesc(anyString(), anyString(), any(PostStatus.class));
    }

    @Test
    void buscarPostsFiltrados_WhenQueryOrCategoryProvided_ShouldReturnFilteredPosts() {
        // Arrange
        Posts post = Posts.builder().idPost(1L).titulo("Post Java").estado(PostStatus.PUBLICADO).build();
        List<Posts> mockPosts = Collections.singletonList(post);

        when(postRepository.findByTituloContainingIgnoreCaseAndCategoriasNombreCategoriaContainingIgnoreCaseAndEstadoOrderByFechaPublicacionDesc(
                "java", "programacion", PostStatus.PUBLICADO
        )).thenReturn(mockPosts);

        // Act
        List<Posts> resultado = postService.buscarPostsFiltrados("java", "programacion");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(postRepository, times(1)).findByTituloContainingIgnoreCaseAndCategoriasNombreCategoriaContainingIgnoreCaseAndEstadoOrderByFechaPublicacionDesc(
                "java", "programacion", PostStatus.PUBLICADO
        );
        verify(postRepository, never()).findByEstadoOrderByFechaPublicacionDesc(any(PostStatus.class));
    }

    // ==========================================
    // TESTS: guardarDesdeDto(PostDTO dto)
    // ==========================================

    @Test
    void guardarDesdeDto_WhenNewPostBasic_ShouldSaveAndReturn() {
        // Arrange
        PostDTO dto = new PostDTO();
        dto.setTitulo("Nuevo Post");
        dto.setSlug("nuevo-post");
        dto.setMetaDescripcion("Meta descripción");
        dto.setIdAutor(1L);
        dto.setEstado("BORRADOR");

        when(postRepository.findBySlug(dto.getSlug())).thenReturn(Optional.empty());
        when(postRepository.save(any(Posts.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Posts resultado = postService.guardarDesdeDto(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Nuevo Post", resultado.getTitulo());
        assertEquals("nuevo-post", resultado.getSlug());
        assertEquals("Meta descripción", resultado.getMetaDescripcion());
        assertEquals(1L, resultado.getIdAutor());
        assertEquals(PostStatus.BORRADOR, resultado.getEstado());
        verify(postRepository, times(1)).findBySlug(dto.getSlug());
        verify(postRepository, times(1)).save(any(Posts.class));
    }

    @Test
    void guardarDesdeDto_WhenExistingPostBasic_ShouldUpdateAndReturn() {
        // Arrange
        PostDTO dto = new PostDTO();
        dto.setTitulo("Post Actualizado");
        dto.setSlug("post-existente");
        dto.setMetaDescripcion("Nueva meta");
        dto.setIdAutor(1L);
        dto.setEstado("PUBLICADO");

        Posts postExistente = Posts.builder()
                .idPost(5L)
                .titulo("Post Antiguo")
                .slug("post-existente")
                .metaDescripcion("Vieja meta")
                .idAutor(1L)
                .estado(PostStatus.BORRADOR)
                .build();

        when(postRepository.findBySlug(dto.getSlug())).thenReturn(Optional.of(postExistente));
        when(postRepository.save(any(Posts.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Posts resultado = postService.guardarDesdeDto(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(5L, resultado.getIdPost()); // Mismo ID
        assertEquals("Post Actualizado", resultado.getTitulo());
        assertEquals("Nueva meta", resultado.getMetaDescripcion());
        assertEquals(PostStatus.PUBLICADO, resultado.getEstado());
        verify(postRepository, times(1)).findBySlug(dto.getSlug());
        verify(postRepository, times(1)).save(any(Posts.class));
    }

    @Test
    void guardarDesdeDto_WithImageAndCategoryAndBlocks_ShouldSaveCorrectly() {
        // Arrange
        PostDTO dto = new PostDTO();
        dto.setTitulo("Post Completo");
        dto.setSlug("post-completo");
        dto.setMetaDescripcion("Meta completa");
        dto.setIdAutor(1L);
        dto.setEstado("PUBLICADO");

        // Set up Imagen Destacada
        PostDTO.ImagenSimpleDTO imagenDestacadaDto = new PostDTO.ImagenSimpleDTO();
        imagenDestacadaDto.setIdImagen(10L);
        dto.setImagenDestacada(imagenDestacadaDto);

        // Set up Categoria
        PostDTO.CategoriaSimpleDTO catDto = new PostDTO.CategoriaSimpleDTO();
        catDto.setIdCategoria(20L);
        dto.setCategorias(Collections.singletonList(catDto));

        // Set up Bloque de contenido con imagen
        PostDTO.BloqueSimpleDTO bloqueDto = new PostDTO.BloqueSimpleDTO();
        bloqueDto.setTipoBloque("IMAGEN");
        bloqueDto.setOrden(1);
        bloqueDto.setContenido("Url de la imagen");
        PostDTO.ImagenSimpleDTO imgBloqueDto = new PostDTO.ImagenSimpleDTO();
        imgBloqueDto.setIdImagen(11L);
        bloqueDto.setImagen(imgBloqueDto);
        dto.setBloquesDeContenido(Collections.singletonList(bloqueDto));

        // Mocks para repositorios
        Imagenes imgDestacada = Imagenes.builder().idImagen(10L).nombreArchivo("destacada.jpg").build();
        Imagenes imgBloque = Imagenes.builder().idImagen(11L).nombreArchivo("bloque.jpg").build();
        Categorias categoria = Categorias.builder().idCategoria(20L).nombreCategoria("Tecnologia").build();

        when(postRepository.findBySlug(dto.getSlug())).thenReturn(Optional.empty());
        when(imagenRepository.findById(10L)).thenReturn(Optional.of(imgDestacada));
        when(categoriaRepository.findById(20L)).thenReturn(Optional.of(categoria));
        when(imagenRepository.findById(11L)).thenReturn(Optional.of(imgBloque));
        when(postRepository.save(any(Posts.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Posts resultado = postService.guardarDesdeDto(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(imgDestacada, resultado.getImagenDestacada());
        assertEquals(1, resultado.getCategorias().size());
        assertTrue(resultado.getCategorias().contains(categoria));
        assertEquals(1, resultado.getBloquesDeContenido().size());
        
        BloquesPost bloqueResultado = resultado.getBloquesDeContenido().get(0);
        assertEquals(TipoBloque.IMAGEN, bloqueResultado.getTipoBloque());
        assertEquals(1, bloqueResultado.getOrden());
        assertEquals("Url de la imagen", bloqueResultado.getContenido());
        assertEquals(imgBloque, bloqueResultado.getImagen());
        assertEquals(resultado, bloqueResultado.getPost()); // Relación bidireccional

        verify(postRepository, times(1)).findBySlug(dto.getSlug());
        verify(imagenRepository, times(1)).findById(10L);
        verify(categoriaRepository, times(1)).findById(20L);
        verify(imagenRepository, times(1)).findById(11L);
        verify(postRepository, times(1)).save(any(Posts.class));
    }

    @Test
    void guardarDesdeDto_WhenImageNotFound_ShouldThrowRuntimeException() {
        // Arrange
        PostDTO dto = new PostDTO();
        dto.setTitulo("Post Con Imagen Invalida");
        dto.setSlug("post-imagen-invalida");
        dto.setIdAutor(1L);
        dto.setEstado("BORRADOR");

        PostDTO.ImagenSimpleDTO imagenDestacadaDto = new PostDTO.ImagenSimpleDTO();
        imagenDestacadaDto.setIdImagen(99L);
        dto.setImagenDestacada(imagenDestacadaDto);

        when(postRepository.findBySlug(dto.getSlug())).thenReturn(Optional.empty());
        when(imagenRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            postService.guardarDesdeDto(dto);
        });

        assertEquals("Imagen destacada no encontrada con ID: 99", exception.getMessage());
        verify(postRepository, times(1)).findBySlug(dto.getSlug());
        verify(imagenRepository, times(1)).findById(99L);
        verify(postRepository, never()).save(any(Posts.class));
    }

    @Test
    void guardarDesdeDto_WhenCategoryNotFound_ShouldThrowRuntimeException() {
        // Arrange
        PostDTO dto = new PostDTO();
        dto.setTitulo("Post Con Categoria Invalida");
        dto.setSlug("post-categoria-invalida");
        dto.setIdAutor(1L);
        dto.setEstado("BORRADOR");

        PostDTO.CategoriaSimpleDTO catDto = new PostDTO.CategoriaSimpleDTO();
        catDto.setIdCategoria(99L);
        dto.setCategorias(Collections.singletonList(catDto));

        when(postRepository.findBySlug(dto.getSlug())).thenReturn(Optional.empty());
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            postService.guardarDesdeDto(dto);
        });

        assertEquals("Categoría no encontrada: 99", exception.getMessage());
        verify(postRepository, times(1)).findBySlug(dto.getSlug());
        verify(categoriaRepository, times(1)).findById(99L);
        verify(postRepository, never()).save(any(Posts.class));
    }
}
