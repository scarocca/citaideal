package cl.sergio.carocca.tucitaideal_api.rest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.sergio.carocca.tucitaideal_api.entity.Consulta;
import cl.sergio.carocca.tucitaideal_api.entity.MensajeChat;
import cl.sergio.carocca.tucitaideal_api.entity.Reserva;
import cl.sergio.carocca.tucitaideal_api.entity.Usuario;
import cl.sergio.carocca.tucitaideal_api.repository.ConsultaRepository;
import cl.sergio.carocca.tucitaideal_api.repository.UsuarioRepository; // Importamos el repo
import cl.sergio.carocca.tucitaideal_api.service.ChatService;
import cl.sergio.carocca.tucitaideal_api.service.ConsultaService;
import cl.sergio.carocca.tucitaideal_api.service.ReservaService;

@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "*") 
// 1. ELIMINAMOS @PreAuthorize("hasRole('ADMIN')") ya que no usaremos roles
public class AdminRestController {

    private final ConsultaRepository consultaRepository;
    private final ReservaService reservaService;
    private final UsuarioRepository usuarioRepository; // 2. USAMOS REPOSITORY DIRECTO
    private final ConsultaService consultaService;
    private final ChatService chatService;

    public AdminRestController(ConsultaRepository consultaRepository, ReservaService reservaService,
                               UsuarioRepository usuarioRepository, ConsultaService consultaService, ChatService chatService) {
        this.consultaRepository = consultaRepository;
        this.reservaService = reservaService;
        this.usuarioRepository = usuarioRepository; // Inyectamos el repo
        this.consultaService = consultaService;
        this.chatService = chatService;
    }

    // --- GESTIÓN DE CONSULTAS ---

    @GetMapping("/consultas")
    public ResponseEntity<List<Consulta>> listarConsultas() {
        List<Consulta> lista = consultaRepository.findAll();
        Collections.reverse(lista); 
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/consultas/responder")
    public ResponseEntity<?> responder(@RequestBody Map<String, Object> payload) {
        Long consultaId = Long.valueOf(payload.get("consultaId").toString());
        String contenido = payload.get("contenido").toString();

        Consulta consulta = consultaService.buscarPorId(consultaId);
        
        MensajeChat mensaje = new MensajeChat();
        mensaje.setContenido(contenido);
        mensaje.setConsulta(consulta);
        mensaje.setEsAdmin(true);
        mensaje.setFechaEnvio(LocalDateTime.now());
        
        chatService.guardarMensaje(mensaje);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Respuesta enviada al chat con éxito");
        return ResponseEntity.ok(respuesta);
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

    // --- GESTIÓN DE USUARIOS (SIMPLIFICADO) ---

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        // 3. Usamos findAll() directamente del repositorio
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @DeleteMapping("/usuarios/eliminar/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            // 4. Usamos deleteById() del repositorio
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