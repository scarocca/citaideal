package cl.sergio.carocca.tucitaideal_api.rest;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sergio.carocca.tucitaideal_api.entity.Reserva;
import cl.sergio.carocca.tucitaideal_api.service.ReservaService;

@RestController
@RequestMapping("/api/v1/reservas")
@CrossOrigin(origins = "http://localhost:5173") // Reemplaza con el puerto de tu React
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping("/todas")
    public List<Reserva> listarTodas() {
        return reservaService.listarTodas();
    }
    @PutMapping("/{id}/estado")
    public ResponseEntity<Reserva> actualizarEstado(@PathVariable Long id, @RequestBody String nuevoEstado) {
        Reserva reservaActualizada = reservaService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(reservaActualizada);
    }
 // NUEVO MÉTODO PARA CREAR RESERVAS
    @PostMapping("/crear")
    public ResponseEntity<Reserva> crearReserva(@RequestBody Reserva reserva) {
        // El servicio se encarga de la lógica de guardado
        Reserva nuevaReserva = reservaService.guardarReserva(reserva);
        return ResponseEntity.ok(nuevaReserva);
    }
    @GetMapping("/fechas-ocupadas")
    public List<LocalDateTime> getFechasOcupadas() {
        return reservaService.obtenerFechasOcupadas();
    }
    @GetMapping("/fechas-bloqueadas")
    public ResponseEntity<List<LocalDateTime>> getFechasBloqueadas() {
        List<LocalDateTime> fechas = reservaService.obtenerFechasOcupadas();
        return ResponseEntity.ok(fechas);
    }
}