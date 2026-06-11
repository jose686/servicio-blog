package com.joserodriguezdeveloper.BlogService.modelos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloquesPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBloque;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "idPost", nullable = false)
    private Posts post;
    @Enumerated(EnumType.STRING)
    private TipoBloque tipoBloque;
    private long orden;
    @Column(name = "contenido", columnDefinition = "TEXT")
    private String contenido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idImagen")
    private Imagenes imagen;
}
