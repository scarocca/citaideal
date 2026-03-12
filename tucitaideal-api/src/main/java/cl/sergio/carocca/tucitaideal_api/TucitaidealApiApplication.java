package cl.sergio.carocca.tucitaideal_api;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import cl.sergio.carocca.tucitaideal_api.entity.Usuario;
import cl.sergio.carocca.tucitaideal_api.repository.UsuarioRepository;

/**
 * Clase principal de la API TucitaIdeal.
 * Configura y lanza la aplicación Spring Boot.
 * * @author Sergio Carocca
 * @version 1.0
 */
@SpringBootApplication
public class TucitaidealApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TucitaidealApiApplication.class, args);
    }
    @Bean
    CommandLineRunner init(UsuarioRepository repository, PasswordEncoder encoder) {
        return args -> {
            String adminEmail = "admin@citaideal.cl";
            
            // Verificamos si ya existe antes de intentar crear
            if (repository.findByEmail(adminEmail).isEmpty()) {
                Usuario admin = new Usuario();
                admin.setEmail(adminEmail);
                admin.setUsername(adminEmail);
                admin.setPassword(encoder.encode("admin123"));
                
                repository.save(admin);
                System.out.println("🚀 USUARIO ADMINISTRADOR CREADO CON ÉXITO");
            } else {
                System.out.println("✅ El administrador ya existe en la base de datos. Saltando creación.");
            }
            
            System.out.println("✨ Servidor Tucitaideal API listo y escuchando...");
        };
    }

}