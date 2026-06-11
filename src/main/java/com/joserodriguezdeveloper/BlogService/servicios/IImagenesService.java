package com.joserodriguezdeveloper.BlogService.servicios;


import com.joserodriguezdeveloper.BlogService.modelos.Imagenes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IImagenesService {
    List<Imagenes> listarTodas();

    Imagenes guardarImagen(MultipartFile file, Long idUsuario) throws IOException;

    void eliminarImagen(Long id);
}
