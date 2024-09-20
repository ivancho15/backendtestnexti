package com.tcsbackend.springboot.app.models.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcsbackend.springboot.app.models.dao.IClienteDao;
import com.tcsbackend.springboot.app.models.entity.Cliente;

@Service
public class ClienteSerivcesImpl implements IClienteServices{
	
	@Autowired
	private IClienteDao clienteDao;

	@Override
	@Transactional(readOnly= true)
	public List<Cliente> findAll() {
		return (List<Cliente>)clienteDao.findAll();
	}

	@Override
	@Transactional
	public Cliente save(Cliente cliente) {
		return clienteDao.save(cliente);
	}

	@Override
	@Transactional(readOnly= true)
	public Cliente findById(Long id) {
		return clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		clienteDao.deleteById(id);
	}

	@Override
	public Optional<Cliente> findByClienteId(String clienteID) {
		
		return clienteDao.findClienteByclienteId(clienteID);
	}
}
