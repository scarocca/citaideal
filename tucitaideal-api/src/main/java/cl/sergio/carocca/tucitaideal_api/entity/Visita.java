package cl.sergio.carocca.tucitaideal_api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "visitas")
public class Visita {

    @Id
    private String id = "contador_general"; // Usamos un ID fijo para tener un solo contador
    private Long total = 0L;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Long getTotal() { return total; }
    public void setTotal(Long total) { this.total = total; }
}