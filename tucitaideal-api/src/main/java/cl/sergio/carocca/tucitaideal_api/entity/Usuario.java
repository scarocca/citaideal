package cl.sergio.carocca.tucitaideal_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad simplificada para la gestión de usuarios.
 * Se eliminaron los roles para agilizar el proceso de autenticación.
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;

    // Mantenemos username por si el sistema lo requiere, pero usaremos el email para loguear
    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    // Constructor por defecto (obligatorio para JPA)
    public Usuario() {
    }

    // Constructor simplificado
    public Usuario(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    // --- GETTERS Y SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}