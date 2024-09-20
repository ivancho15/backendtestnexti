package com.tcsbackend.springboot.app.models.services;

import java.util.List;
import java.util.Optional;

import com.tcsbackend.springboot.app.models.entity.Cliente;

public interface IClienteServices {
	public List<Cliente> findAll();
	public Cliente save (Cliente cliente);
	public Cliente findById(Long id);
	public void delete(Long id);
	public Optional<Cliente> findByClienteId(String clienteID);
}
