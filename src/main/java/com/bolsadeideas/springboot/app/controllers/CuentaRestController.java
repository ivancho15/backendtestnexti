package com.bolsadeideas.springboot.app.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.Cuenta;
import com.bolsadeideas.springboot.app.models.services.IClienteServices;
import com.bolsadeideas.springboot.app.models.services.ICuentaServices;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CuentaRestController {
	
	@Autowired
	private ICuentaServices cuentaService;
	
	@Autowired
	private IClienteServices clienteService;

	@GetMapping("/cuentas")
	public List<Cuenta> index() {
		return cuentaService.findAll();
	}

	@GetMapping("/cuentas/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {

		Cuenta cuenta = null;
		Map<String, Object> response = new HashMap<>();

		try {
			cuenta = cuentaService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (cuenta == null) {
			response.put("mensaje", "La Cuenta Nro: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Cuenta>(cuenta, HttpStatus.OK);
	}
	
	@GetMapping("/clientes/{clienteId}/cuentas")
	public ResponseEntity<?> getAllCuentasbyCliente(@PathVariable Long clienteId){
		
		List<Cuenta>cuentas = new ArrayList<Cuenta>();
		
		Map<String, Object> response = new HashMap<>();
		
		
		if(clienteService.findById(clienteId)==null) {
			response.put("mensaje", "Error: no se encuentran cuentas, el cliente ID: "
					.concat(clienteId.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			cuentas = cuentaService.cuentaByClienet(clienteId);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("cuentas", cuentas);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		
	}

	@PostMapping("/clientes/{clienteId}/cuentas")
	public ResponseEntity<?> create(@Valid  @RequestBody Cuenta cuenta, BindingResult result, @PathVariable Long clienteId) {
		Cuenta cuentaNew = null;
		Cliente cliente = clienteService.findById(clienteId);
		
		Map<String, Object> response = new HashMap<>();
		
		if (cliente == null) {
			response.put("mensaje", "Error: no se pudo crear la cuenta, el cliente ID: "
					.concat(clienteId.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo " + err.getField() + "' '" + err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
				
		try {
			cuenta.setCliente(cliente);
			cuentaNew = cuentaService.save(cuenta);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La Cuenta ha sido creado con éxito!");
		response.put("cuenta", cuentaNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/cuentas/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Cuenta cuenta, BindingResult result, @PathVariable Long id) {
		Cuenta cuentaActual = cuentaService.findById(id);
		Cuenta cuentaUpdated = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo " + err.getField() + "' '" + err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (cuentaActual == null) {
			response.put("mensaje", "Error: no se pudo editar, la cuenta Nro: "
					.concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			cuentaActual.setSaldo(cuenta.getSaldo());
			cuentaActual.setEstado(cuenta.getEstado());
			cuentaUpdated = cuentaService.save(cuentaActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar la cuenta  en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La cuenta se ha sido actualizado con éxito!");
		response.put("cuenta", cuentaUpdated);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/cuentas/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			cuentaService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar la cuenta de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La cuenta ha sido eliminada con éxito!");
		return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}
}
