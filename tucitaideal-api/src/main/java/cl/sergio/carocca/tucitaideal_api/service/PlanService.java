package cl.sergio.carocca.tucitaideal_api.service;


import java.util.List;


import org.springframework.stereotype.Service;

import cl.sergio.carocca.tucitaideal_api.entity.Plan;
import cl.sergio.carocca.tucitaideal_api.repository.PlanRepository;



/**
 * Servicio encargado de la lógica de negocio para la gestión de Planes.
 * Proporciona métodos para el mantenimiento del catálogo de servicios, incluyendo
 * la gestión de archivos multimedia asociados y el control de visibilidad de los planes.
 * * @author Sergio Carocca
 * @version 1.0
 */
@Service
public class PlanService {

	private final PlanRepository planRepository;
    private final FileService fileService; 

    // Inyección por Constructor (DIP)
    public PlanService(PlanRepository planRepository,FileService fileService) {
        this.planRepository = planRepository;
        this.fileService = fileService;
    }
    
    public List<Plan> listarPlanesActivos() {
        // Usamos el método optimizado del Repository
        return planRepository.findByActivoTrue();
    }

    /**
     * Busca un plan específico por su identificador único.
     * * @param id Identificador del plan.
     * @return El objeto {@link Plan} encontrado o null si no existe.
     */
    public Plan buscarPorId(Long id) {
        return planRepository.findById(id).orElse(null);
    }
	
    /**
     * Persiste o actualiza un plan en la base de datos.
     * * @param plan Objeto plan con los datos a guardar.
     */
    public Plan guardarPlan(Plan plan) {
        return planRepository.save(plan);
    }

    /**
     * Elimina un plan de forma integral. 
     * Este método primero intenta borrar el archivo de imagen físico del servidor para 
     * optimizar el almacenamiento y luego elimina el registro de la base de datos.
     * * @param id Identificador del plan a eliminar.
     */
    public void eliminar(Long id) {
        Plan plan = buscarPorId(id);
        if (plan != null) {
            // Delegamos la responsabilidad del archivo al FileService
            fileService.eliminarArchivo(plan.getImagenUrl());
            
            // Borramos de la DB
            planRepository.deleteById(id);
        }
    }
    /**
     * Recupera todos los planes registrados en el sistema, sin importar su estado.
     * * @return Lista completa de objetos {@link Plan}.
     */
    public List<Plan> listarTodos() {
        return planRepository.findAll();
    }

    /**
     * Desactiva un plan de forma lógica para que no sea visible en el catálogo público,
     * pero manteniendo sus datos en la base de datos.
     * * @param id Identificador del plan a ocultar.
     */
    public void ocultarPlan(Long id) {
        Plan plan = buscarPorId(id);
        if (plan != null) {
            plan.setActivo(false);
            planRepository.save(plan);
        }
    }

    /**
     * Activa un plan previamente oculto para que vuelva a estar disponible en el catálogo público.
     * * @param id Identificador del plan a activar.
     */
    public void activarPlan(Long id) {
        Plan plan = buscarPorId(id);
        if (plan != null) {
            plan.setActivo(true);
            planRepository.save(plan);
        }
    }
    public Plan actualizarPlan(Long id, Plan planActualizado) {
        Plan planExistente = planRepository.findById(id).orElse(null);
        if (planExistente != null) {
            planExistente.setNombre(planActualizado.getNombre());
            planExistente.setDescripcion(planActualizado.getDescripcion());
            planExistente.setPrecioBase(planActualizado.getPrecioBase());
            planExistente.setActivo(planActualizado.isActivo());
            return planRepository.save(planExistente);
        }
        return null;
    }

    
  
}