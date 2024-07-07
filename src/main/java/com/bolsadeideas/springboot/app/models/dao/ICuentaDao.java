package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Cuenta;

public interface ICuentaDao extends CrudRepository<Cuenta, Long>{
	
	@Query(value="SELECT c from Cuenta c where c.cliente.id =?1")
	List<Cuenta> findByClienteId(Long clienteId);
}
