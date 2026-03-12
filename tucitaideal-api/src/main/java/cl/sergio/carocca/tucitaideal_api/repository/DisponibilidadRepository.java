package cl.sergio.carocca.tucitaideal_api.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.sergio.carocca.tucitaideal_api.entity.Disponibilidad;
import cl.sergio.carocca.tucitaideal_api.entity.enums.EstadoDisponibilidad;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {

    // El Agente usará esto para responder "¿Qué tienes libre el lunes?"
    List<Disponibilidad> findByFechaAndEstado(LocalDate fecha, EstadoDisponibilidad estado);

    // Para mostrar disponibilidad en un rango de fechas (ej: esta semana)
    List<Disponibilidad> findByFechaBetweenAndEstado(LocalDate inicio, LocalDate fin, EstadoDisponibilidad estado);
}
