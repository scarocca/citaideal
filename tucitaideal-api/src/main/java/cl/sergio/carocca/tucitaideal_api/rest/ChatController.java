package cl.sergio.carocca.tucitaideal_api.rest; // Tu package puede variar ligeramente

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Import de tu servicio personalizado
import cl.sergio.carocca.tucitaideal_api.service.ChatAgenteService;



@RestController
@RequestMapping("/api/chat") // Sin espacios, todo en minúsculas
@CrossOrigin(origins = "*") 
public class ChatController {

    @Autowired
    private ChatAgenteService chatAgenteService;

    @PostMapping("/preguntar") // Asegúrate que no diga @GetMapping
    public ResponseEntity<String> preguntar(@RequestBody String mensaje) {
        String respuesta = chatAgenteService.generarRespuesta(mensaje);
        return ResponseEntity.ok(respuesta);
    }
}
