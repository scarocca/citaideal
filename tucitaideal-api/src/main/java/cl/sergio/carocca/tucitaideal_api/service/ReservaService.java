package cl.sergio.carocca.tucitaideal_api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.sergio.carocca.tucitaideal_api.entity.ItemCarrito;
import cl.sergio.carocca.tucitaideal_api.entity.Reserva;
import cl.sergio.carocca.tucitaideal_api.entity.Usuario;
import cl.sergio.carocca.tucitaideal_api.repository.ReservaRepository;
import cl.sergio.carocca.tucitaideal_api.util.GeneradorCodigo;
import jakarta.transaction.Transactional;

/**
 * Servicio encargado de orquestar la lógica de negocio de las reservas.
 * Gestiona la disponibilidad de horarios, la creación de registros individuales 
 * y el procesamiento masivo de reservas provenientes del carrito de compras.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private EmailService emailService;
    /**
     * Crea una reserva individual previa validación de disponibilidad horaria.
     * * @param reserva Objeto reserva con los datos del cliente y el plan solicitado.
     * @return La reserva persistida en la base de datos.
     * @throws Exception Si el horario ya se encuentra ocupado por una reserva confirmada.
     */
    @Transactional
    public Reserva crearReserva(Reserva reserva) {
        // 1. Validar disponibilidad
        boolean ocupado = reservaRepository.existeReservaEnEsaFecha(
                reserva.getPlan().getId(), 
                reserva.getFechaCita()
        );

        if (ocupado) {
            throw new RuntimeException("Lo sentimos, este horario ya no está disponible para este plan.");
        }

        // 2. Si está libre, procedemos
        reserva.setEstado("PENDIENTE");
        reserva.setCodigoSeguimiento(GeneradorCodigo.generar()); // Tu lógica de códigos
        return reservaRepository.save(reserva);
    }
    /**
     * Obtiene el listado de todas las reservas ordenadas por fecha de cita descendente.
     * * @return Lista completa de reservas registradas.
     */
    public List<Reserva> listarTodas() {
        return reservaRepository.findAllByOrderByFechaCitaDesc();
    }

    /**
     * Procesa la compra de múltiples servicios desde el carrito en un solo flujo.
     * Primero valida que TODOS los servicios estén disponibles para evitar reservas parciales
     * y luego genera registros individuales para cada ítem.
     * * @param carrito Lista de ítems seleccionados por el usuario.
     * @param fecha La fecha y hora común para la prestación de los servicios.
     * @param datosCliente Objeto que contiene la información de contacto del solicitante.
     * @return Lista de reservas confirmadas y guardadas.
     * @throws Exception Si al menos uno de los servicios solicitados no tiene disponibilidad.
     */
    public List<Reserva> guardarReservaMultiple(List<ItemCarrito> carrito, LocalDateTime fecha, Reserva datosCliente,Usuario usuarioLogueado) throws Exception {
        List<Reserva> listaConfirmadas = new ArrayList<>();        
        
        // 1. Validar disponibilidad de TODOS los planes antes de guardar nada (Consistencia)
        for (ItemCarrito item : carrito) {
            boolean ocupado = reservaRepository.existeReservaEnEsaFecha(
                                item.getPlan().getId(), 
                                fecha);
            
            if (ocupado) {
                throw new Exception("Lo sentimos, el servicio '" + item.getPlan().getNombre() + 
                                    "' ya no está disponible para esa fecha y hora.");
            }
        }

        // 2. Si todos están libres, procedemos a la persistencia
        for (ItemCarrito item : carrito) {
            Reserva nueva = new Reserva();
            nueva.setNombreCliente(datosCliente.getNombreCliente());
            nueva.setEmailCliente(datosCliente.getEmailCliente());
            nueva.setTelefonoCliente(datosCliente.getTelefonoCliente());
            nueva.setFechaCita(fecha);
            nueva.setPlan(item.getPlan());
            nueva.setEstado("CONFIRMADA");
            
            nueva.setUsuario(usuarioLogueado);
            // Generación de código único de seguimiento
            nueva.setCodigoSeguimiento(java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            
            listaConfirmadas.add(reservaRepository.save(nueva));
        }
        return listaConfirmadas;
    }

    /**
     * Elimina una reserva del sistema verificando su existencia previa.
     * * @param id Identificador único de la reserva.
     * @throws Exception Si el ID proporcionado no corresponde a ninguna reserva existente.
     */
    public void eliminarReserva(Long id) throws Exception {
        if (!reservaRepository.existsById(id)) {
            throw new Exception("La reserva con ID " + id + " no existe.");
        }
        reservaRepository.deleteById(id);
    }

    /**
     * Método alternativo para eliminar una reserva utilizando búsqueda por objeto.
     * * @param id Identificador único de la reserva.
     * @throws Exception Si la reserva no se encuentra para ser eliminada.
     */
    public void eliminar(Long id) throws Exception {
        reservaRepository.findById(id).ifPresentOrElse(
            reserva -> reservaRepository.delete(reserva),
            () -> {
                try {
                    throw new Exception("No se encontró la reserva para eliminar.");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        );
    }
    /**
     * Cambia el estado de una reserva a 'CONFIRMADA'.
     * @param id Identificador de la reserva.
     * @throws RuntimeException si la reserva no existe.
     */
    @Transactional
    public void confirmarReserva(Long id) {
        // Buscamos la reserva por ID
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva con ID " + id + " no encontrada"));
        
        // Cambiamos el estado
        reserva.setEstado("CONFIRMADA");
        
        // Guardamos los cambios
        reservaRepository.save(reserva);
    }
    public boolean verificarOcupado(Long planId, LocalDateTime fecha) {
        return reservaRepository.existeReservaEnEsaFecha(planId, fecha);
    }
    public List<Reserva> obtenerPorEmail(String email) {
        return reservaRepository.findByUsuarioEmail(email);
    }
    
    public Reserva actualizarEstado(Long id, String nuevoEstado) {
        // 1. Buscamos la reserva en la BD
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la reserva con ID: " + id));

        // 2. Actualizamos el estado en la entidad
        reserva.setEstado(nuevoEstado);
        Reserva actualizada = reservaRepository.save(reserva);

        // 3. Si el estado es CONFIRMADA, enviamos el correo con los parámetros correctos
        if ("CONFIRMADA".equalsIgnoreCase(nuevoEstado)) {
            
            // Extraemos los datos con cuidado
            String email = actualizada.getEmailCliente();
            String nombre = actualizada.getNombreCliente();
            
            // Obtenemos el nombre del plan (manejando si por alguna razón fuera null)
            String planNombre = (actualizada.getPlan() != null) ? actualizada.getPlan().getNombre() : "Plan Especial";
            
            // Formateamos la fecha a algo legible (ej: 2026-03-05 15:30)
            String fechaFormateada = (actualizada.getFechaCita() != null) 
                ? actualizada.getFechaCita().toString().replace("T", " ") 
                : "Fecha por confirmar";

            String codigo = actualizada.getCodigoSeguimiento();

            // Llamamos al método con los 5 parámetros definidos
            emailService.enviarCorreoConfirmacion(email, nombre, planNombre, fechaFormateada, codigo);
        }

        return actualizada;
    }
    public Reserva guardarReserva(Reserva reserva) {
        // Aquí puedes agregar lógica extra antes de guardar, 
        // como enviar el correo de confirmación.
        return reservaRepository.save(reserva);
    }
}