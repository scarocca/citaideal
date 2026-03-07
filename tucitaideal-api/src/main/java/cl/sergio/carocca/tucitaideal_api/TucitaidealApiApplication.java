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
    CommandLineRunner init(UsuarioRepository repository, org.springframework.security.crypto.password.PasswordEncoder encoder) {
        return args -> {
            // 1. Limpiamos la tabla para que no existan errores de duplicado o roles viejos
            repository.deleteAll();
            System.out.println("🧹 Base de datos limpia");

            // 2. Creamos al usuario administrador
            Usuario admin = new Usuario();
            admin.setEmail("admin@citaideal.cl");
            admin.setUsername("admin@citaideal.cl");
            
            // La clave será 'admin123' (pero se guarda encriptada)
            admin.setPassword(encoder.encode("admin123"));
            
            repository.save(admin);
            
            System.out.println("🚀 USUARIO CREADO PARA LOGIN:");
            System.out.println("📧 Email: admin@citaideal.cl");
            System.out.println("🔑 Clave: admin123");
        };
    }

}