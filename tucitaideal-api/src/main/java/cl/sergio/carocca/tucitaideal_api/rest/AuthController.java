package cl.sergio.carocca.tucitaideal_api.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import cl.sergio.carocca.tucitaideal_api.dto.AuthResponse; // Importamos tu nuevo DTO
import cl.sergio.carocca.tucitaideal_api.dto.LoginRequest;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Cambiamos a "*" para que funcione tanto en Local como en Render
public class AuthController {

    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // 1. Validamos las credenciales
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), 
                    loginRequest.getPassword()
                )
            );

            // 2. Usamos tu clase AuthResponse en lugar del HashMap manual
            // authentication.getName() nos da el email del usuario logueado
            return ResponseEntity.ok(new AuthResponse(
                authentication.getName(), 
                "Login exitoso"
            ));

        } catch (Exception e) {
            // 3. Error 401 claro para React
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }
}