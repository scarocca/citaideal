package cl.sergio.carocca.tucitaideal_api.rest;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.sergio.carocca.tucitaideal_api.entity.Reserva;
import cl.sergio.carocca.tucitaideal_api.entity.Usuario;
import cl.sergio.carocca.tucitaideal_api.repository.UsuarioRepository; 
import cl.sergio.carocca.tucitaideal_api.service.ReservaService;

@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "*") 
public class AdminRestController {

    private final ReservaService reservaService;
    private final UsuarioRepository usuarioRepository;

    public AdminRestController(ReservaService reservaService,
                               UsuarioRepository usuarioRepository) {
        this.reservaService = reservaService;
        this.usuarioRepository = usuarioRepository; 
    }

    // --- GESTIÓN DE RESERVAS ---
    @GetMapping("/reservas")
    public ResponseEntity<List<Reserva>> listarReservas() {
        return ResponseEntity.ok(reservaService.listarTodas());
    }

    @PutMapping("/reservas/confirmar/{id}")
    public ResponseEntity<?> confirmarReserva(@PathVariable Long id) {
        try {
            reservaService.confirmarReserva(id);
            Map<String, String> res = new HashMap<>();
            res.put("mensaje", "Reserva confirmada exitosamente");
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo confirmar la reserva");
        }
    }

    // --- GESTIÓN DE USUARIOS ---
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @DeleteMapping("/usuarios/eliminar/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al eliminar usuario");
        }
    }

    @DeleteMapping("/reservas/eliminar/{id}")
    public ResponseEntity<?> eliminarReserva(@PathVariable Long id) {
        try {
            reservaService.eliminar(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("No se pudo eliminar la reserva");
        }
    }
}