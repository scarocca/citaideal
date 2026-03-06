package cl.sergio.carocca.tucitaideal_api.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sergio.carocca.tucitaideal_api.dto.LoginRequest;

/**
 * Controlador para gestionar la autenticación de usuarios administradores.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173") // Permiso para tu React
public class AuthController {

    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // 1. Validamos las credenciales contra la Base de Datos
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), 
                    loginRequest.getPassword()
                )
            );

            // 2. Si es exitoso, devolvemos la info que React necesita
            Map<String, String> response = new HashMap<>();
            response.put("username", authentication.getName());
            response.put("status", "SUCCESS");
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // 3. Si las credenciales fallan o el usuario no existe
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }
}