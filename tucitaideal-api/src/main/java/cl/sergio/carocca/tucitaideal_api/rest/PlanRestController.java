package cl.sergio.carocca.tucitaideal_api.rest;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import cl.sergio.carocca.tucitaideal_api.entity.Plan;
import cl.sergio.carocca.tucitaideal_api.service.PlanService;
import cl.sergio.carocca.tucitaideal_api.service.FileService; // IMPORTANTE: Importar el service

@RestController
@RequestMapping("/api/v1/planes")
@CrossOrigin(origins = "http://localhost:5173")
public class PlanRestController {

    private final PlanService planService;
    private final FileService fileService; // Agregamos la referencia

    // Actualizamos el constructor para que Spring inyecte ambos servicios
    public PlanRestController(PlanService planService, FileService fileService) {
        this.planService = planService;
        this.fileService = fileService;
    }

    @GetMapping("/ver/activos")
    public ResponseEntity<List<Plan>> listarActivos() {
        List<Plan> lista = planService.listarPlanesActivos();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plan> obtenerPorId(@PathVariable Long id) {
        Plan plan = planService.buscarPorId(id);
        return (plan != null) ? ResponseEntity.ok(plan) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/subir-imagen")
    public ResponseEntity<String> subirImagen(@PathVariable Long id, @RequestParam("archivo") MultipartFile archivo) {
        try {
            // 1. Buscamos el plan primero
            Plan plan = planService.buscarPorId(id);
            if (plan == null) return ResponseEntity.notFound().build();

            // 2. Usamos el FileService para subir a Cloudinary (carpeta "planes")
            String urlNube = fileService.guardarArchivo(archivo, "planes");

            if (urlNube != null) {
                // 3. Guardamos la URL de Cloudinary en la base de datos
                plan.setImagenUrl(urlNube);
                planService.guardarPlan(plan);
                return ResponseEntity.ok("Imagen de Plan subida a Cloudinary con éxito");
            } else {
                return ResponseEntity.status(500).body("Error al procesar la imagen en la nube");
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
    @PostMapping("/crear-con-foto")
    public ResponseEntity<Plan> crearConFoto(
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precioBase") java.math.BigDecimal precioBase, // Nombre y tipo exacto
            @RequestParam("activo") boolean activo) {
        try {
            // 1. Subir a Cloudinary usando el servicio que ya configuramos
            String urlNube = fileService.guardarArchivo(archivo, "planes");

            // 2. Crear instancia de Plan con tus atributos exactos
            Plan nuevoPlan = new Plan();
            nuevoPlan.setNombre(nombre);
            nuevoPlan.setDescripcion(descripcion);
            nuevoPlan.setPrecioBase(precioBase); // Atributo correcto
            nuevoPlan.setActivo(activo);
            nuevoPlan.setImagenUrl(urlNube); // Atributo correcto

            // 3. Guardar en BD
            Plan guardado = planService.guardarPlan(nuevoPlan);
            return ResponseEntity.ok(guardado);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Plan> actualizar(@PathVariable Long id, @RequestBody Plan plan) {
        Plan actualizado = planService.actualizarPlan(id, plan);
        return (actualizado != null) ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }
}