package com.tu_paquete.ticketflex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TransaccionService {
    @Autowired
    private TransaccionRepository transaccionRepository;

    public Transaccion crearTransaccion(Transaccion transaccion) {
    	System.out.println("Creando transaccion: " + transaccion);
        return transaccionRepository.save(transaccion);
    }

    public List<Transaccion> listarTransacciones() {
        return transaccionRepository.findAll();
    }

    public Transaccion obtenerTransaccionPorId(Integer id) {
        return transaccionRepository.findById(id).orElse(null);
    }

    public void eliminarTransaccion(Integer id) {
        transaccionRepository.deleteById(id);
    }
    
    public List<Transaccion> obtenerHistorialDeCompras(Integer idUsuario) {
        // Buscar todas las transacciones de un usuario específico
        return transaccionRepository.findByIdUsuario(idUsuario);
    }
}








