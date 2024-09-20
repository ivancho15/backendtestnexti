package com.tcsbackend.springboot.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcsbackend.springboot.app.models.entity.Cliente;
import com.tcsbackend.springboot.app.models.services.IClienteServices;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ClienteRestController {
	
	 private static final Logger logger = LoggerFactory.getLogger(ClienteRestController.class);

	    @Autowired
	    private IClienteServices clienteService;

	    @GetMapping("/clientes")
	    public ResponseEntity<List<Cliente>> index() {
	        List<Cliente> clientes = clienteService.findAll();
	        return ResponseEntity.ok(clientes);
	    }

	    @GetMapping("/clientes/{id}")
	    public ResponseEntity<?> show(@PathVariable Long id) {
	        Cliente cliente;
	        Map<String, Object> response = new HashMap<>();

	        try {
	            cliente = clienteService.findById(id);
	            if (cliente == null) {
	                response.put("mensaje", "El cliente ID: " + id + " no existe en la base de datos");
	                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	            }
	        } catch (DataAccessException e) {
	            return handleDatabaseError(response, e, "Error al realizar la consulta en la base de datos");
	        }

	        return ResponseEntity.ok(cliente);
	    }

	    @PostMapping("/clientes")
	    public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result) {
	        if (result.hasErrors()) {
	            return handleValidationErrors(result);
	        }

	        Map<String, Object> response = new HashMap<>();
	        try {
	            Cliente clienteNew = clienteService.save(cliente);
	            response.put("mensaje", "El cliente ha sido creado con éxito!");
	            response.put("cliente", clienteNew);
	            return new ResponseEntity<>(response, HttpStatus.CREATED);
	        } catch (DataAccessException e) {
	            return handleDatabaseError(response, e, "Error al realizar el insert en la base de datos");
	        }
	    }

	    @PutMapping("/clientes/{id}")
	    public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente, BindingResult result, @PathVariable Long id) {
	        if (result.hasErrors()) {
	            return handleValidationErrors(result);
	        }

	        Map<String, Object> response = new HashMap<>();
	        Cliente clienteActual = clienteService.findById(id);

	        if (clienteActual == null) {
	            response.put("mensaje", "Error: no se pudo editar, el cliente ID: " + id + " no existe en la base de datos");
	            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	        }

	        try {
	            copyClienteProperties(cliente, clienteActual);
	            Cliente clienteUpdated = clienteService.save(clienteActual);
	            response.put("mensaje", "El cliente ha sido actualizado con éxito!");
	            response.put("cliente", clienteUpdated);
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        } catch (DataAccessException e) {
	            return handleDatabaseError(response, e, "Error al actualizar el cliente en la base de datos");
	        }
	    }

	    @DeleteMapping("/clientes/{id}")
	    public ResponseEntity<?> delete(@PathVariable Long id) {
	        Map<String, Object> response = new HashMap<>();
	        try {
	            clienteService.delete(id);
	            response.put("mensaje", "El cliente ha sido eliminado con éxito!");
	            return ResponseEntity.ok(response);
	        } catch (DataAccessException e) {
	            return handleDatabaseError(response, e, "Error al eliminar el cliente en la base de datos");
	        }
	    }

	    private ResponseEntity<Map<String, Object>> handleValidationErrors(BindingResult result) {
	        List<String> errors = result.getFieldErrors()
	            .stream()
	            .map(err -> "El campo " + err.getField() + ": " + err.getDefaultMessage())
	            .collect(Collectors.toList());

	        Map<String, Object> response = new HashMap<>();
	        response.put("errors", errors);
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }

	    private ResponseEntity<Map<String, Object>> handleDatabaseError(Map<String, Object> response, DataAccessException e, String message) {
	        logger.error(message, e);
	        response.put("mensaje", message);
	        response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }

	    private void copyClienteProperties(Cliente source, Cliente target) {
	        target.setNombre(source.getNombre());
	        target.setApellido(source.getApellido());
	        target.setTelefono(source.getTelefono());
	        target.setEmail(source.getEmail());
	        target.setDireccion(source.getDireccion());
	        target.setEstado(source.getEstado());
	        target.setPassword(source.getPassword());
	        target.setCreateAt(source.getCreateAt());
	    }
}
