package cl.sergio.carocca.tucitaideal_api.config;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cl.sergio.carocca.tucitaideal_api.entity.Usuario;
import cl.sergio.carocca.tucitaideal_api.repository.UsuarioRepository;

/**
 * Servicio de autenticación minimalista.
 * Solo valida Email y Password contra la base de datos.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Buscamos al usuario por email
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // 2. Retornamos el User de Spring Security con una lista vacía de autoridades
        // Esto evita errores de roles inexistentes.
        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(), 
                usuario.getPassword(), 
                new ArrayList<>() // 👈 Lista vacía: No necesitamos roles para entrar
        );
    }
}