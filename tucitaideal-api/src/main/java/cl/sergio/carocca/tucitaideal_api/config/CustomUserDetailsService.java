package cl.sergio.carocca.tucitaideal_api.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cl.sergio.carocca.tucitaideal_api.entity.Usuario;
import cl.sergio.carocca.tucitaideal_api.repository.UsuarioRepository;



/**
 * Servicio de autenticación personalizado que implementa {@link UserDetailsService}.
 * Esta clase es el núcleo de la seguridad, ya que permite a Spring Security 
 * verificar las credenciales de un usuario consultando directamente la base de datos 
 * y cargando sus permisos (autoridades).
 * * @author Sergio Carocca
 * @version 1.0
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    /**
     * Localiza al usuario basado en su nombre de usuario (email) y construye un 
     * objeto {@link UserDetails} compatible con el framework de seguridad.
     * * @param username El nombre de usuario que intenta iniciar sesión.
     * @return Una instancia de {@link UserDetails} que contiene el username, password y roles.
     * @throws UsernameNotFoundException Si el usuario no existe en la base de datos.
     */
    @Override
    public UserDetails loadUserByUsername(String loginInput) throws UsernameNotFoundException {
        // 1. IMPORTANTE: Cambiamos findByUsername por findByEmail
        // Porque 'loginInput' contiene el correo que viene del formulario
        Usuario usuario = usuarioRepo.findByEmail(loginInput)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró el usuario con email: " + loginInput));

        // 2. Cargamos las autoridades (esto ya lo tienes bien)
        List<GrantedAuthority> autoridades = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
                .collect(Collectors.toList());

        // 3. Retornamos el objeto User
        // Usamos el email como nombre de usuario principal para Spring
        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(), 
                usuario.getPassword(), 
                autoridades
        );
    }
}