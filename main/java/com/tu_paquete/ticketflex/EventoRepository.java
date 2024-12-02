package com.tu_paquete.ticketflex;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Integer> {
 
    List<Evento> findByLugar(String lugar);
    List<Evento> findByFecha(LocalDate fecha);
    List<Evento> findByCategoria(String categoria);
    List<Evento> findByArtista(String artista);

    // También puedes agregar un método con combinación de filtros si es necesario
    List<Evento> findByLugarAndFechaAndCategoriaAndArtista(String lugar, LocalDate fecha, String categoria, String artista);
}

