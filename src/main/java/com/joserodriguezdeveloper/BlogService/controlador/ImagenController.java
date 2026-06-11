package com.joserodriguezdeveloper.BlogService.controlador;



import com.joserodriguezdeveloper.BlogService.modelos.Imagenes;
import com.joserodriguezdeveloper.BlogService.serviciosImpl.ImagenesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/imagenes")
public class ImagenController {

    private final ImagenesServiceImpl imagenService;
    @Autowired
    public ImagenController(ImagenesServiceImpl imagenService) {
        this.imagenService = imagenService;
    }


    @GetMapping
    public ResponseEntity<List<Imagenes>> listar() {
        return ResponseEntity.ok(imagenService.listarTodas());
    }

    @PostMapping("/subir")
    public ResponseEntity<Imagenes> subir(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "idUsuario", required = false) Long idUsuario,
            @RequestParam(value = "altText", required = false) String altText) {

        System.out.println("📩 ¡Petición recibida en Java!");
        System.out.println("Archivo: " + file.getOriginalFilename());

        try {
            Long userId = (idUsuario != null) ? idUsuario : 1L;
            Imagenes guardada = imagenService.guardarImagen(file, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
        } catch (Exception e) {
            System.out.println("🔥 Error en la lógica de guardado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarImagen (@PathVariable Long id){
        try {
        imagenService.eliminarImagen(id);
        return ResponseEntity.ok().build() ;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }
}