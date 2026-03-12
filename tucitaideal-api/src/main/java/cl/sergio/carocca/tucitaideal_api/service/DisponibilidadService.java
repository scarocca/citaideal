package cl.sergio.carocca.tucitaideal_api.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.sergio.carocca.tucitaideal_api.entity.Disponibilidad;
import cl.sergio.carocca.tucitaideal_api.entity.enums.EstadoDisponibilidad;
import cl.sergio.carocca.tucitaideal_api.repository.DisponibilidadRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DisponibilidadService {

    private final DisponibilidadRepository repository;

    // Constructor manual (sin Lombok) para inyectar el repositorio
    public DisponibilidadService(DisponibilidadRepository repository) {
        this.repository = repository;
    }

    /**
     * Busca los bloques libres para una fecha específica.
     * Esta es la función que consultará el Agente Valentín.
     */
    public List<Disponibilidad> obtenerDisponiblesPorFecha(LocalDate fecha) {
        return repository.findByFechaAndEstado(fecha, EstadoDisponibilidad.DISPONIBLE);
    }

    /**
     * Cambia el estado de un bloque a RESERVADO.
     * Se usa @Transactional para asegurar que si falla el guardado, no haya errores en la BD.
     */
    @Transactional
    public String confirmarReserva(Long disponibilidadId) {
        Optional<Disponibilidad> resultado = repository.findById(disponibilidadId);
        
        if (resultado.isPresent()) {
            Disponibilidad bloque = resultado.get();
            
            if (bloque.getEstado() == EstadoDisponibilidad.DISPONIBLE) {
                bloque.setEstado(EstadoDisponibilidad.RESERVADO);
                repository.save(bloque);
                return "¡Excelente elección! Su cita para el día " + bloque.getFecha() + " ha sido confirmada.";
            } else {
                return "Lo siento, ese horario ya no se encuentra disponible.";
            }
        }
        return "Error: No se encontró el horario solicitado.";
    }
}