package cl.sergio.carocca.tucitaideal_api.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sergio.carocca.tucitaideal_api.entity.Visita;
import cl.sergio.carocca.tucitaideal_api.repository.VisitaRepository;

@RestController
@RequestMapping("/api/visits")
@CrossOrigin(origins = "*") // Permite que tu frontend de React se conecte
public class VisitaController {

    @Autowired
    private VisitaRepository repository;

    // 1. Endpoint para SUMAR una visita (Se usará desde el cliente)
    @PostMapping("/increment")
    public void incrementar() {
        Visita v = repository.findById("contador_general").orElse(new Visita());
        v.setTotal(v.getTotal() + 1);
        repository.save(v);
    }

    // 2. Endpoint para VER el total (Se usará desde tu ContadorVisitasAdmin.jsx)
    @GetMapping("/total")
    public Long obtenerTotal() {
        return repository.findById("contador_general")
                .map(Visita::getTotal)
                .orElse(0L);
    }
}
