package com.tu_paquete.ticketflex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario registrarUsuario(Usuario usuario) {
    	// Verificar si el usuario ya existe
        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            throw new IllegalArgumentException("El usuario ya existe con ese correo electrónico");
        }
        return usuarioRepository.save(usuario);
    }
    
    // Inicio de sesion sin cifrado
    public Usuario iniciarSesion(String email, String password) {
    	Usuario usuario = usuarioRepository.findByEmail(email);
    	
    	if(usuario != null && usuario.getPassword().equals(password)) {
    		return usuario; // Autenticacion exitosa 
    	}
    	return null; // Credenciales incorrectas
    }
    
    public boolean esAdministrador(int idUsuario) {
        // Buscar al usuario por ID
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

        // Si el usuario no existe o no tiene rol asignado, retornar false
        if (usuario == null || usuario.getRol() == null) {
            return false;
        }

        // Verificar si el rol es Administrador
        return "Administrador".equals(usuario.getRol().getNombreRol());
    }

}
