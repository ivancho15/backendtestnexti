package com.bolsadeideas.springboot.app.models.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.bolsadeideas.springboot.app.models.dao.IClienteDao;
import com.bolsadeideas.springboot.app.models.entity.Cliente;

@SpringBootTest
class IClienteServicesTest {

	@Autowired
	private IClienteServices clienteService;
	@MockBean
	private IClienteDao clienteRepository;
	
	@BeforeEach
	void setUp() throws Exception {
		Cliente localCliente = new Cliente();
			localCliente.setApellido("Marcano");
			localCliente.setNombre("Ivan");
			localCliente.setDireccion("Mi Casa");
			localCliente.setEmail("ijmm15@gmail.com");
			localCliente.setClienteId("1758627564");
			localCliente.setPassword("jajaja2024");
			localCliente.setEdad(40);
			localCliente.setTelefono("0963978451");
			Mockito.when(clienteRepository.findClienteByclienteId("1758627564")).thenReturn(Optional.of(localCliente));
	}

	@Test
	@DisplayName("Prueba de obtencion de  informacion  de un local enviando un clienteId calido")
	public void findClienteByIdClienteShouldFound() {
		String localClienteId = "1758627564";
		Cliente cliente = clienteService.findByClienteId(localClienteId).get();
		assertEquals(localClienteId, cliente.getClienteId());
	}
	
	

}