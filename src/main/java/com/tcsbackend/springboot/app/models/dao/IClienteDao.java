package com.bolsadeideas.springboot.app.models.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

public interface IClienteDao extends CrudRepository<Cliente, Long>{

	@Query("SELECT c FROM Cliente c WHERE c.clienteId = ?1")
	Optional<Cliente> findClienteByclienteId(String clienteId);

}
