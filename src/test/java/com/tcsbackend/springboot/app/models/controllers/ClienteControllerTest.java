package com.tcsbackend.springboot.app.models.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcsbackend.springboot.app.models.entity.Cliente;

@SpringBootTest
@AutoConfigureMockMvc
public class ClienteControllerTest {
	
	 @Autowired
	 private MockMvc mockMvc;

	 @Test
	 public void testCreateCliente() throws Exception {
		 Cliente cliente = new Cliente();
	     cliente.setNombre("Iván");
	     cliente.setApellido("Marcano");
	     cliente.setEmail("ijmm54@gmail.com");
	     cliente.setDireccion("mi casa por la ciudad");
	     cliente.setEdad(40);
	     cliente.setEstado(true);
	     cliente.setTelefono("0987456612");
	     cliente.setClienteId("178547859");
	     cliente.setPassword("12345");

	     mockMvc.perform(post("/api/clientes")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(new ObjectMapper().writeValueAsString(cliente)))
	     		.andExpect(status().isCreated());
	    }

		@Test
		public void testGetAllClientes() throws Exception {
			mockMvc.perform(get("/api/clientes")
					.contentType(MediaType.APPLICATION_JSON))
		                .andExpect(status().isOk()) // Verifica que el estatus sea 200 OK
		                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verifica que el tipo de contenido sea JSON
		                .andExpect(jsonPath("$").isArray()) // Verifica que la respuesta sea un array JSON
		                .andExpect(jsonPath("$[0].nombre").exists()); // Verifica que el primer cliente tiene el campo 'nombre'
		    }
}
