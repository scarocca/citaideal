package cl.sergio.carocca.tucitaideal_api.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.sergio.carocca.tucitaideal_api.entity.MensajeChat;

import java.util.List;

@Repository
public interface MensajeChatRepository extends JpaRepository<MensajeChat, Long> {
    // Esto nos servirá para traer el historial de una consulta específica ordenado por fecha
    List<MensajeChat> findByConsultaIdOrderByFechaEnvioAsc(Long consultaId);
    void deleteByConsultaId(Long consultaId);
}