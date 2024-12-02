package com.tu_paquete.ticketflex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private TransaccionService transaccionService;

    @PostMapping("/registrar")
    public ResponseEntity<Map<String, String>> registrarUsuario(@RequestBody Usuario usuario) {
        Map<String, String> response = new HashMap<>();
        try {
            usuarioService.registrarUsuario(usuario);
            response.put("mensaje", "Usuario registrado exitosamente");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }




    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> iniciarSesion(@RequestParam String email, @RequestParam String password) {
        Usuario usuario = usuarioService.iniciarSesion(email, password);
        if (usuario != null) {
        	Map<String, Object> response = new HashMap<>();
        	response.put("id", usuario.getIdUsuario());
        	response.put("nombre", usuario.getNombre());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(null);
    }
    
 // Manejar solicitudes GET con un mensaje adecuado
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity<String> loginGet() {
        // Puedes devolver un mensaje de error o una página de error
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                             .body("Método GET no permitido.");
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> cerrarSesion() {
        // Aquí puedes invalidar la sesión del usuario
        return ResponseEntity.ok("Sesión cerrada exitosamente");
    }
    
    @GetMapping("/{id}/historial")
    public ResponseEntity<List<Transaccion>> obtenerHistorialDeCompras(@PathVariable Integer id) {
        try {
            // Obtener las transacciones del usuario
            List<Transaccion> historial = transaccionService.obtenerHistorialDeCompras(id);

            // Si el historial está vacío, respondemos con un 204 No Content
            if (historial.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }

            // De lo contrario, respondemos con el historial de compras
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}

