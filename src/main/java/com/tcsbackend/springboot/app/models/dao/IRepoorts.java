package com.bolsadeideas.springboot.app.models.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bolsadeideas.springboot.app.models.entity.Movimiento;


@Repository
public interface IRepoorts extends JpaRepository<Movimiento, Long> {
	    
	@Query("SELECT m FROM Movimiento m WHERE m.fecha BETWEEN ?1 AND ?2 and m.cuenta.cliente.id =?3")
    List<Movimiento> findMovimientosByDateBetweenAndClienteId(Date date, Date date2, Long idCliente);
}
