package com.tu_paquete.ticketflex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boletas")
public class BoletoController {
    @Autowired
    private BoletoService boletoService;

    @PostMapping("/comprar")
    public Boleto comprarBoleto(@RequestParam Integer idEvento, @RequestParam Integer idUsuario, @RequestParam Integer cantidad) {
        return boletoService.comprarBoleto(idEvento, idUsuario, cantidad);
    }
    
    // Nuevo endpoint para manejar el pago con tarjeta
    @PostMapping("/pagar")
    public String procesarPago(@RequestBody PagoRequest pagoRequest) {
        // Aquí procesas el pago con el banco seleccionado, tarjeta, etc.
        boolean pagoExitoso = boletoService.procesarPago(pagoRequest);

        if (pagoExitoso) {
            return "Pago procesado exitosamente";
        } else {
            return "Error en el procesamiento del pago";
        }
    }
}

