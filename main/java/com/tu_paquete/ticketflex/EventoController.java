package com.tu_paquete.ticketflex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {
    @Autowired
    private EventoService eventoService;
    @Autowired
    private TransaccionService transaccionService;
    @Autowired
    private BoletoService boletoService;

    @PostMapping
    public Evento crearEvento(@RequestBody Evento evento) {
    	if (evento.getImagen() == null || evento.getImagen().trim().isEmpty()) {
            evento.setImagen("default.jpg"); // Asigna la imagen predeterminada
        }
    	return eventoService.crearEvento(evento);
    }
    
    @GetMapping("/listar")
    public List<Evento> listarEventos(){
    	List<Evento> eventos = eventoService.listarEventos();
    	// Recorrer los eventos y asignar imagen predeterminada si es necesario
        for (Evento evento : eventos) {
            // Si la imagen es null o vacía, asigna una imagen por defecto
            if (evento.getImagen() == null || evento.getImagen().trim().isEmpty()) {
                evento.setImagen("default.jpg"); // Imagen predeterminada
            }
        }
    	
    	return eventos;
    }
    
    @GetMapping("/{id}")
    public Evento obtenerEventoPorId(@PathVariable Integer id) {
    	return eventoService.obtenerEventorPorId(id);
    }
    
    @DeleteMapping("/{id}")
    public void eliminarEvento(@PathVariable Integer id) {
    	eventoService.eliminarEvento(id);
    }
    
    // Actualizar un evento
    @PutMapping("/{id}")
    public Evento actualizarEvento(@PathVariable Integer id, @RequestBody Evento eventoActualizado) {
    	Evento eventoExistente = eventoService.obtenerEventorPorId(id);
    	if (eventoExistente != null) {
    		//Actualizamos los campos del evento
    		eventoExistente.setNombreEvento(eventoActualizado.getNombreEvento());
    		eventoExistente.setFecha(eventoActualizado.getFecha());
    		eventoExistente.setLugar(eventoActualizado.getLugar());
    		eventoExistente.setPrecioBase(eventoActualizado.getPrecioBase());
    		
    		 if (eventoActualizado.getImagen() == null || eventoActualizado.getImagen().trim().isEmpty()) {
    	            eventoExistente.setImagen("default.jpg"); // Imagen predeterminada
    	        } else {
    	            eventoExistente.setImagen(eventoActualizado.getImagen()); // Mantener la imagen si no es null
    	        }
    		return eventoService.actualizarEvento(eventoExistente);
    	}
    	return null;
    }
    
    @PostMapping("/{id_evento}/comprar")
    public ResponseEntity<String> comprarBoleto(@PathVariable Integer id_evento, @RequestParam Integer cantidad, @RequestParam Integer idUsuario) {
        if (cantidad <= 0 || cantidad > 5) {
            return ResponseEntity.badRequest().body("La cantidad debe ser mayor o igual a 1 y menor o igual a 5.");
        }

        // Lógica para procesar la compra
        try {
            Boleto boleto = boletoService.comprarBoleto(id_evento, idUsuario, cantidad);
            return ResponseEntity.ok("Compra exitosa, boletos comprados: " + cantidad);
        }catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
            }
        }
    // Endpoint para filtrar los eventos
    @PostMapping("/filtrar")
    public List<Evento> filtrarEventos(@RequestBody Map<String, Object> filtros) {
        // Obtener los parámetros de los filtros
        String lugar = (String) filtros.get("lugar");
        String fechaStr = (String) filtros.get("fecha");
        String categoria = (String) filtros.get("categoria");
        String artista = (String) filtros.get("artista");

        // Convertir la fecha de String a LocalDate
        LocalDate fecha = null;
        if (fechaStr != null && !fechaStr.isEmpty()) {
            try {
                fecha = LocalDate.parse(fechaStr);
            } catch (DateTimeParseException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fecha inválida", e);
            }
        }
        
        // Llamar al servicio de filtrado
        return eventoService.filtrarEventos(lugar, fecha, categoria, artista);
    }
    @PostMapping("/pagar")
    public ResponseEntity<String> procesarPago(@RequestBody PagoRequest pagoRequest) {
        // Lógica de procesamiento del pago
        return ResponseEntity.ok("Pago procesado con éxito");
    }
    }

