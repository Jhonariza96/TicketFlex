package com.tu_paquete.ticketflex;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class EventoService {
    @Autowired
    private EventoRepository eventoRepository;
    
    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private BoletoRepository boletoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Evento crearEvento(Evento evento) {
    	return eventoRepository.save(evento);
    }
    
    public List<Evento>listarEventos(){	
    	return eventoRepository.findAll();
    }
    
    public Evento obtenerEventorPorId(Integer id) {
    	return eventoRepository.findById(id).orElse(null);
    }
    
    public void eliminarEvento(Integer id) {
    	eventoRepository.deleteById(id);
    }

    @Transactional
    public boolean comprarBoleto(Integer idEvento, Integer cantidad, Integer idUsuario) {
        // Obtener el evento
        Evento evento = obtenerEventorPorId(idEvento);
        if (evento == null) {
            return false; // Evento no encontrado
        }

        // Verificar si hay suficientes boletos disponibles
        if (evento.getDisponibilidad() != null && evento.getDisponibilidad() >= cantidad) {
            // Restar la cantidad comprada de la disponibilidad
            evento.setDisponibilidad(evento.getDisponibilidad() - cantidad);
            eventoRepository.save(evento); // Guardar el evento actualizado

            // Obtener el usuario
            Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
            if (usuario == null) {
                return false; // Usuario no encontrado
            }

            // Crear y guardar el boleto
            Boleto boleto = new Boleto();
            boleto.setEvento(evento);
            boleto.setUsuario(usuario);
            boleto.setPrecio(evento.getPrecioBase());
            boleto.setCantidad(cantidad);
            boleto.setPrecioTotal(evento.getPrecioBase().multiply(BigDecimal.valueOf(cantidad)));
            boleto.setFechaCompra(new Date());

            boleto = boletoRepository.save(boleto); // Guardar boleto

            // Crear y guardar la transacción
            Transaccion transaccion = new Transaccion();

           

            return true; // Compra exitosa
        } else {
            return false; // No hay suficientes boletos disponibles
        }
    }

    public Evento actualizarEvento(Evento eventoActualizado) {
        // Guarda el evento actualizado en la base de datos
        return eventoRepository.save(eventoActualizado);  // Este es el método que persiste el evento actualizado
        }
    
    // Filtros de eventos
    
    public List<Evento> filtrarEventos(String lugar, LocalDate fecha, String categoria, String artista) {
        // Si no se especifica ningún filtro, devolver todos los eventos
        if (lugar == null && fecha == null && categoria == null && artista == null) {
            return eventoRepository.findAll();
        }

        // Si todos los filtros son no nulos, aplicarlos de manera combinada
        if (lugar != null && fecha != null && categoria != null && artista != null) {
            return eventoRepository.findByLugarAndFechaAndCategoriaAndArtista(lugar, fecha, categoria, artista);
        }

        // Filtrar por lugar
        if (lugar != null && !lugar.isEmpty()) {
            return eventoRepository.findByLugar(lugar);
        }

        // Filtrar por fecha
        if (fecha != null) {
            return eventoRepository.findByFecha(fecha);
        }

        // Filtrar por categoría
        if (categoria != null && !categoria.isEmpty()) {
            return eventoRepository.findByCategoria(categoria);
        }

        // Filtrar por artista
        if (artista != null && !artista.isEmpty()) {
            return eventoRepository.findByArtista(artista);
        }

        // Si no se puede hacer filtrado por combinación, retornar todos
        return eventoRepository.findAll();
    }

    }
