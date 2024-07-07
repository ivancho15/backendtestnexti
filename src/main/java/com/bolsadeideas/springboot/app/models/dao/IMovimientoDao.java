package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Movimiento;

public interface IMovimientoDao extends CrudRepository<Movimiento, Long>{
	
	@Query(value="SELECT m from Movimiento m where m.cuenta.nro_cuenta =?1")
	List<Movimiento> findByNroCuenta(Long nroCuenta);
	
}
