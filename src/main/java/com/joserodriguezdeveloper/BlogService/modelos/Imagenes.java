package com.joserodriguezdeveloper.BlogService.modelos;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Imagenes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImagen;
    private Long usuarioSubida;
    private String nombreArchivo;
    private String ruta;
    private String altText;
    @CreationTimestamp
    private LocalDateTime fechaSubida;
}
