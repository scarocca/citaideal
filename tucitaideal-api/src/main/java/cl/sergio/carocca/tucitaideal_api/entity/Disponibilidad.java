package cl.sergio.carocca.tucitaideal_api.entity;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

import cl.sergio.carocca.tucitaideal_api.entity.enums.EstadoDisponibilidad;

@Entity
@Table(name = "disponibilidad")

public class Disponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDisponibilidad estado;

    // Relación con el Servicio (Mantenimiento, Consulta, etc.)
    // Un bloque de disponibilidad suele estar ligado a un tipo de servicio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_id")
    private Servicio servicio;
    
    

    public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public LocalDate getFecha() {
		return fecha;
	}



	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}



	public LocalTime getHoraInicio() {
		return horaInicio;
	}



	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}



	public LocalTime getHoraFin() {
		return horaFin;
	}



	public void setHoraFin(LocalTime horaFin) {
		this.horaFin = horaFin;
	}



	public EstadoDisponibilidad getEstado() {
		return estado;
	}



	public void setEstado(EstadoDisponibilidad estado) {
		this.estado = estado;
	}



	public Servicio getServicio() {
		return servicio;
	}



	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}



	// Helper method para saber si el bloque es pasado
    public boolean esPasado() {
        return fecha.isBefore(LocalDate.now()) || 
               (fecha.isEqual(LocalDate.now()) && horaInicio.isBefore(LocalTime.now()));
    }
    
}

