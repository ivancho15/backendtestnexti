package com.tcsbackend.springboot.app.models.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;


import com.tcsbackend.springboot.app.models.dao.IClienteDao;
import com.tcsbackend.springboot.app.models.entity.Cliente;


@ExtendWith(MockitoExtension.class)
public class IClienteServicesTest {

    @Mock
    private IClienteDao clienteRepository;

    @InjectMocks
    private  ClienteSerivcesImpl clienteService;

    @Test
    public void testCreateCliente() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Iván");
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente result = clienteService.save(cliente);

        assertEquals("Iván", result.getNombre());
    }
}
