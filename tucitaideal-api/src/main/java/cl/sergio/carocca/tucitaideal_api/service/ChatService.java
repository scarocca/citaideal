package cl.sergio.carocca.tucitaideal_api.service;


import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.sergio.carocca.tucitaideal_api.entity.MensajeChat;
import cl.sergio.carocca.tucitaideal_api.repository.MensajeChatRepository;

@Service
public class ChatService {

    @Autowired
    private MensajeChatRepository chatRepo;

    public void guardarMensaje(MensajeChat mensaje) {
        chatRepo.save(mensaje);
    }
    @Transactional
    public void borrarHistorialPorConsulta(Long consultaId) {
        // Esto asume que tienes el método en el repositorio
        chatRepo.deleteByConsultaId(consultaId);
    }
}