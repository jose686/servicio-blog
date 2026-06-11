package com.joserodriguezdeveloper.BlogService.serviciosImpl;


import com.joserodriguezdeveloper.BlogService.modelos.Imagenes;
import com.joserodriguezdeveloper.BlogService.repositorio.ImagenRepository;
import com.joserodriguezdeveloper.BlogService.servicios.IImagenesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Service
public class ImagenesServiceImpl implements IImagenesService {


    private ImagenRepository repository;

    @Value("${app.base-url}")
    private String baseUrl;
    private final String UPLOAD_DIR = "static/uploads/";

    @Autowired
    public ImagenesServiceImpl(ImagenRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Imagenes> listarTodas() {
        return repository.findAll();
    }

    @Override
    public Imagenes guardarImagen(MultipartFile file, Long idUsuario) throws IOException {
        // 1. Crear directorio si no existe
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) directory.mkdirs();

        // 2. Generar nombre único para evitar duplicados
        String nombreOriginal = file.getOriginalFilename();
        String nombreFinal = System.currentTimeMillis() + "_" + nombreOriginal;

        // 3. Guardar archivo físico
        Path path = Paths.get(UPLOAD_DIR + nombreFinal);
        Files.write(path, file.getBytes());
        String urlCompleta = baseUrl + "/uploads/" + nombreFinal;
        // 4. Crear objeto en Base de Datos
        Imagenes img = Imagenes.builder()
                .nombreArchivo(nombreOriginal)
                .ruta(urlCompleta) // Ruta relativa para el frontend
                .usuarioSubida(idUsuario)
                .altText(nombreOriginal) // Por defecto el nombre
                .build();

        return repository.save(img);
    }

    @Override
    public void eliminarImagen(Long id) {
        Imagenes img = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada en la BD"));
        String nombreFinal = img.getRuta().substring(img.getRuta().lastIndexOf("/") + 1);
        Path pathFisico = Paths.get(UPLOAD_DIR + nombreFinal);
        try {
            Files.deleteIfExists(pathFisico);
            System.out.println("🗑️ Archivo físico eliminado: " + nombreFinal);
        } catch (IOException e) {
            System.out.println("⚠️ No se pudo eliminar el archivo físico: " + e.getMessage());
        }

        repository.deleteById(id);
    }

}
