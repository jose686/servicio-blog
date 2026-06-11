package com.joserodriguezdeveloper.BlogService.modelos;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter // Para obtener los datos
@Setter // Para que JPA pueda inicializar los campos
@NoArgsConstructor // OBLIGATORIO para Hibernate
@AllArgsConstructor
@Builder
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPost;

    @Column(nullable = false)
    private Long idAutor;

    @ManyToOne // ⬅️ ¡CAMBIO FUNDAMENTAL!
    @JoinColumn(name = "id_imagen_destacada_fk", referencedColumnName = "idImagen")
    private Imagenes imagenDestacada;

    @ManyToMany
    @JoinTable(
            name = "posts_categorias",
            joinColumns = @JoinColumn(name = "idPost"),
            inverseJoinColumns = @JoinColumn(name = "idCategoria"))
    private Set<Categorias> categorias;

    private String titulo;
    private String metaDescripcion;
    @Column(unique = true, nullable = false)
    private String slug;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime fechaPublicacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_bloque", length = 50)
    private PostStatus estado;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orden ASC")
    private List<BloquesPost> bloquesDeContenido;
}
