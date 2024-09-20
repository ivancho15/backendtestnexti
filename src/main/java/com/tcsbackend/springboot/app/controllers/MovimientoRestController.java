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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.app.models.entity.Cuenta;
import com.bolsadeideas.springboot.app.models.entity.Movimiento;
import com.bolsadeideas.springboot.app.models.services.ICuentaServices;
import com.bolsadeideas.springboot.app.models.services.IMovimientoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class MovimientoRestController {
		
	@Autowired
	private IMovimientoService movimientoService;
	
	@Autowired
	private ICuentaServices cuentaService;

	@GetMapping("/movimientos")
	public List<Movimiento> index() {
		return movimientoService.findAll();
	}

	@GetMapping("/movimientos/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {

		Movimiento movimiento = null;
		Map<String, Object> response = new HashMap<>();

		try {
			movimiento = movimientoService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (movimiento == null) {
			response.put("mensaje", "El movimiento ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Movimiento>(movimiento, HttpStatus.OK);
	}
	
	@GetMapping("/cuentas/{cuentaId}/movimientos")
	public ResponseEntity<?> getAllCuentasbyCliente(@PathVariable Long cuentaId){
		
		List<Movimiento>movimientos = new ArrayList<Movimiento>();
		
		Map<String, Object> response = new HashMap<>();
		
		
		if(cuentaService.findById(cuentaId)==null) {
			response.put("mensaje", "Error: no se encuentran movimientos, La cuenta Nro: "
					.concat(cuentaId.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try {
			movimientos = movimientoService.movimientoByCuenta(cuentaId);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("movimientos", movimientos);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		
	}

	@PostMapping("/cuentas/{nroCuenta}/movimientos")
	public ResponseEntity<?> create(@Valid  @RequestBody Movimiento movimiento, BindingResult result, @PathVariable Long nroCuenta) {
		Movimiento movimientoNew = null;
		@SuppressWarnings("unused")
		Cuenta cuentaUpdated = null;
		Cuenta cuenta = cuentaService.findById(nroCuenta);
		
		Map<String, Object> response = new HashMap<>();
		
		if(cuenta==null) {
			response.put("mensaje", "Error: no se pudo crear el movimiento, la cuenta Nro: "
					.concat(nroCuenta.toString().concat(" no existe en la base de datos")));
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
		
		
		if (((movimiento.getTipo().compareTo("Retiro")==0) && movimiento.getValor() > 0) || 
				((movimiento.getTipo().compareTo("Deposito")==0) && movimiento.getValor() < 0)) {
			response.put("mensaje", "Error: Inconsistencia en la operación, para retiros coloque numero negativo ");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (movimiento.getValor() + cuenta.getSaldo() < 0) {
			response.put("mensaje", "Error: No tiene saldo suficiente pare efectuar el movimiento en la cuenta nro: "
					.concat(nroCuenta.toString()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			movimiento.setSaldo(cuenta.getSaldo() + movimiento.getValor());
			movimiento.setCuenta(cuenta);
			movimientoNew = movimientoService.save(movimiento);
			cuenta.setSaldo(movimiento.getSaldo());
			cuentaUpdated = cuentaService.save(cuenta);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El movimiento ha sido  registrado con éxito! en la cuenta Nro ".concat(nroCuenta.toString()).concat(" a nombre del cliente ")
				.concat(cuenta.getCliente().getNombre().toString().concat(" ").concat(cuenta.getCliente().getApellido())));
		response.put("movimiento", movimientoNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}




}
