package cl.sergio.carocca.tucitaideal_api.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder; // Nuevo Import
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import cl.sergio.carocca.tucitaideal_api.dto.AuthResponse;
import cl.sergio.carocca.tucitaideal_api.dto.LoginRequest;
import cl.sergio.carocca.tucitaideal_api.repository.UsuarioRepository; // Nuevo Import
import cl.sergio.carocca.tucitaideal_api.entity.Usuario; // Nuevo Import

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    // Necesitamos estas dos herramientas para el login manual
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // El constructor inyecta lo que necesitamos
    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Intentando login manual para: " + loginRequest.getEmail());

        // 1. Buscamos al usuario
        return usuarioRepository.findByEmail(loginRequest.getEmail())
            .map(usuario -> {
                // 2. Comparamos la clave (React envía texto plano, DB tiene Hash)
                if (passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
                    System.out.println("✅ Login exitoso en el Backend");
                    return ResponseEntity.ok(new AuthResponse(usuario.getEmail(), "Login exitoso"));
                } else {
                    System.out.println("❌ Contraseña incorrecta para: " + usuario.getEmail());
                    return ResponseEntity.status(401).body("Contraseña incorrecta");
                }
            })
            .orElseGet(() -> {
                System.out.println("❌ Usuario no encontrado: " + loginRequest.getEmail());
                return ResponseEntity.status(401).body("Usuario no encontrado");
            });
    }
}