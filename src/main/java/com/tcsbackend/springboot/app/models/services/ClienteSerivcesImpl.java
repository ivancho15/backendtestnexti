package com.tcsbackend.springboot.app.models.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcsbackend.springboot.app.models.dao.IClienteDao;
import com.tcsbackend.springboot.app.models.entity.Cliente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ClienteSerivcesImpl implements IClienteServices {

    private static final Logger logger = LoggerFactory.getLogger(ClienteSerivcesImpl.class);

    @Autowired
    private IClienteDao clienteDao;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return (List<Cliente>) clienteDao.findAll();
    }

    @Override
    @Transactional
    public Cliente save(Cliente cliente) {
        logger.info("Saving cliente with ID: {}", cliente.getId());
        return clienteDao.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findById(Long id) {
        logger.info("Finding cliente with ID: {}", id);
        return clienteDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        logger.info("Deleting cliente with ID: {}", id);
        clienteDao.deleteById(id);
    }

    @Override
    public Optional<Cliente> findByClienteId(String clienteID) {
        logger.info("Finding cliente by clienteID: {}", clienteID);
        return clienteDao.findClienteByclienteId(clienteID);
    }
}
