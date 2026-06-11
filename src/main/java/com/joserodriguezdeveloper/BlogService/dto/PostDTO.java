package com.joserodriguezdeveloper.BlogService.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostDTO {
    private String titulo;
    private String slug;
    private String metaDescripcion;
    private Long idAutor;
    private String estado;
    private List<CategoriaSimpleDTO> categorias;
    private ImagenSimpleDTO imagenDestacada;
    private List<BloqueSimpleDTO> bloquesDeContenido;

    @Data
    public static class CategoriaSimpleDTO {
        private Long idCategoria;
    }

    @Data
    public static class ImagenSimpleDTO {
        private Long idImagen;
    }

    @Data
    public static class BloqueSimpleDTO {
        private String tipoBloque;
        private Integer orden;
        private String contenido;
        private ImagenSimpleDTO imagen; // Para bloques que tengan imagen
    }
}