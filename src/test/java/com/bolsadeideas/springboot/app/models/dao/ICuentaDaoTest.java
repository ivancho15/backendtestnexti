package com.bolsadeideas.springboot.app.models.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ICuentaDaoTest {

	@Autowired
	private IClienteDao clienteDao;
	
	@Autowired
	TestEntityManager testEntityManager;
	
	@BeforeEach
	void setUp() throws Exception {
		Cliente localCliente = new Cliente();
		localCliente.setApellido("Marcano");
		localCliente.setNombre("Ivan");
		localCliente.setDireccion("Mi Casa en la calle");
		localCliente.setEmail("ijmm15@gmail.com");
		localCliente.setClienteId("1758627564");
		localCliente.setPassword("jajaja2024");
		localCliente.setEdad(40);
		localCliente.setTelefono("0963978451");
		localCliente.setEstado(true);
		testEntityManager.persist(localCliente);
	}

	@Test
	void testFindByClienteIdFound() {
		Optional<Cliente> cliente  = clienteDao.findClienteByclienteId("1758627564");
		assertEquals(cliente.get().getClienteId(), "1758627564");
		System.out.println("cliente.get(): " + cliente.get());
	}
	
	@Test
	void testFindByClienteIdNotFound() {
		Optional<Cliente> cliente  = clienteDao.findClienteByclienteId("1758627565");
		assertEquals(cliente, Optional.empty());
	}
	

}
