package com.tcsbackend.springboot.app.models.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tcsbackend.springboot.app.models.entity.Cliente;

public interface IClienteDao extends JpaRepository<Cliente, Long>{

	@Query("SELECT c FROM Cliente c WHERE c.clienteId = ?1")
	Optional<Cliente> findClienteByclienteId(String clienteId);

}
