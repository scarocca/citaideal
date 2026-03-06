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
import cl.sergio.carocca.tucitaideal_api.service.ChatService;
import cl.sergio.carocca.tucitaideal_api.service.ConsultaService;
import cl.sergio.carocca.tucitaideal_api.service.ReservaService;
import cl.sergio.carocca.tucitaideal_api.service.UsuarioService;



/**
 * Adaptación del AdminController a arquitectura REST para consumo desde React.
 * @author Sergio Carocca
 */
@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "http://localhost:5173") // Permiso para tu React
public class AdminRestController {

    private final ConsultaRepository consultaRepository;
    private final ReservaService reservaService;
    private final UsuarioService usuarioService;
    private final ConsultaService consultaService;
    private final ChatService chatService;

    public AdminRestController(ConsultaRepository consultaRepository, ReservaService reservaService,
                               UsuarioService usuarioService, ConsultaService consultaService, ChatService chatService) {
        this.consultaRepository = consultaRepository;
        this.reservaService = reservaService;
        this.usuarioService = usuarioService;
        this.consultaService = consultaService;
        this.chatService = chatService;
    }

    // --- GESTIÓN DE CONSULTAS ---

    @GetMapping("/consultas")
    public ResponseEntity<List<Consulta>> listarConsultas() {
        List<Consulta> lista = consultaRepository.findAll();
        Collections.reverse(lista); 
        return ResponseEntity.ok(lista); // Retorna el JSON de consultas
    }

    @PostMapping("/consultas/responder")
    public ResponseEntity<?> responder(@RequestBody Map<String, Object> payload) {
        // En REST usamos @RequestBody para recibir JSON desde React
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

    @PutMapping("/reservas/confirmar/{id}") // Usamos PUT para actualizar estado
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
        return ResponseEntity.ok(usuarioService.listartodo());
    }

    @DeleteMapping("/usuarios/eliminar/{id}") // Usamos DELETE para eliminar
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminar(id);
            return ResponseEntity.ok().build(); // Retorna 200 OK sin cuerpo
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al eliminar usuario");
        }
    }
 // --- MÉTODO PARA ELIMINAR RESERVAS ---
    @DeleteMapping("/reservas/eliminar/{id}")
    public ResponseEntity<?> eliminarReserva(@PathVariable Long id) {
        try {
            reservaService.eliminar(id); // Asegúrate de que tu ReservaService tenga el método eliminar
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("No se pudo eliminar la reserva");
        }
    }
}