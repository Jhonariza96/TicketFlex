package com.tu_paquete.ticketflex;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransaccionRepository extends JpaRepository<Transaccion, Integer> {
	List<Transaccion> findByIdUsuario(Integer idUsuario);
}
