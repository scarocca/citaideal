package cl.sergio.carocca.tucitaideal_api.rest;

import cl.sergio.carocca.tucitaideal_api.entity.Foto;
import cl.sergio.carocca.tucitaideal_api.repository.FotoRepository;
import cl.sergio.carocca.tucitaideal_api.service.FileService;
import cl.sergio.carocca.tucitaideal_api.service.FotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/galeria")
@CrossOrigin(origins = "*") // Tu puerto de React
public class FotoRestController {

    private final FotoService fotoService;
    private final FileService fileService;
    private final FotoRepository fotoRepository;

    public FotoRestController(FotoService fotoService, FileService fileService,FotoRepository fotoRepository) {
        this.fotoService = fotoService;
        this.fileService = fileService;
        this.fotoRepository = fotoRepository;
    }

    @GetMapping("/todas")
    public List<Foto> listarTodas() {
        // Usamos el repositorio directamente o a través del service para ordenar por fecha
        return fotoRepository.findAllByOrderByFechaCargaDesc();
    }

    @PostMapping("/subir")
    public ResponseEntity<?> subirFoto(@RequestParam("archivo") MultipartFile file, 
                                       @RequestParam("titulo") String titulo) {
        try {
            // 1. Guardar el archivo físico usando el FileService que creamos antes
            // Lo guardamos en la subcarpeta "galeria"
            String nombreArchivoGuardado = fileService.guardarArchivo(file, "galeria");

            if (nombreArchivoGuardado != null) {
                // 2. Crear el objeto Foto con tu entidad corregida
                Foto nuevaFoto = new Foto();
                nuevaFoto.setTitulo(titulo);
                nuevaFoto.setArchivo(nombreArchivoGuardado); // Guardamos la ruta/nombre
                
                // 3. Persistir en la BD (el @PrePersist se encarga de la fecha)
                fotoService.guardar(nuevaFoto);
                
                return ResponseEntity.ok(nuevaFoto);
            }
            return ResponseEntity.internalServerError().body("No se pudo guardar el archivo físico");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        fotoService.eliminarFotoCompleta(id);
        return ResponseEntity.ok().build();
    }
}