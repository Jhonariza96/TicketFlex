package com.tu_paquete.ticketflex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tu_paquete.ticketflex.Boleto;
import com.tu_paquete.ticketflex.Evento;
import com.tu_paquete.ticketflex.PagoRequest;
import com.tu_paquete.ticketflex.Usuario;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class BoletoService {
    @Autowired
    private BoletoRepository boletoRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Boleto comprarBoleto(Integer idEvento, Integer idUsuario, Integer cantidad) {
        Evento evento = eventoRepository.findById(idEvento).orElseThrow(() -> new RuntimeException("Evento no encontrado"));
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (evento.getDisponibilidad() < cantidad) {
            throw new RuntimeException("No hay suficiente disponibilidad para este evento");
        }

        Boleto boleto = new Boleto();
        boleto.setEvento(evento);
        boleto.setUsuario(usuario);
        boleto.setFechaCompra(new Date());
        boleto.setCantidad(cantidad);
        boleto.setPrecio(evento.getPrecioBase()); // Asumiendo que `precio_base` es el precio del evento
        boleto.setPrecioTotal(evento.getPrecioBase().multiply(BigDecimal.valueOf(cantidad)));

        evento.setDisponibilidad(evento.getDisponibilidad() - cantidad);
        eventoRepository.save(evento); // Actualizar disponibilidad

        return boletoRepository.save(boleto);
    }
    
 // Método para procesar el pago
    public boolean procesarPago(PagoRequest pagoRequest) {
        // Aquí podrías realizar la integración con un sistema de pago, por ejemplo PayU, MercadoPago, etc.
        // Este es un ejemplo básico sin integración real con un servicio de pago.

        // Ejemplo de validación simple (solo para fines demostrativos)
        if (pagoRequest.getBanco() == null || pagoRequest.getNumeroTarjeta().isEmpty() || 
            pagoRequest.getFechaVencimiento().isEmpty() || pagoRequest.getCvv().isEmpty()) {
            return false;  // Si falta algún dato, el pago falla.
        }

        // Simula el pago
        System.out.println("Procesando pago de " + pagoRequest.getCantidad() + " boletos para el evento " +
                pagoRequest.getIdEvento() + " del usuario " + pagoRequest.getIdUsuario() + " a través de " +
                pagoRequest.getBanco());

        // Aquí va la lógica de integración real con el sistema de pago.
        // Podrías conectarte a un API de pagos, validar la tarjeta, verificar fondos, etc.

        // Si todo va bien, simula que el pago fue exitoso
        return true;
    }
}


