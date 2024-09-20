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

import com.tcsbackend.springboot.app.models.entity.Cliente;
import com.tcsbackend.springboot.app.models.entity.Cuenta;
import com.tcsbackend.springboot.app.models.services.IClienteServices;
import com.tcsbackend.springboot.app.models.services.ICuentaServices;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CuentaRestController {
    
    @Autowired
    private ICuentaServices cuentaService;
    
    @Autowired
    private IClienteServices clienteService;

    @GetMapping("/cuentas")
    public ResponseEntity<?> getAllCuentas() {
        return new ResponseEntity<>(cuentaService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/cuentas/{id}")
    public ResponseEntity<?> getCuentaById(@PathVariable Long id) {
        return handleServiceCall(() -> cuentaService.findById(id), "La cuenta Nro: " + id + " no existe en la base de datos");
    }
    
    @GetMapping("/clientes/{clienteId}/cuentas")
    public ResponseEntity<?> getCuentasByClienteId(@PathVariable Long clienteId) {
        if (clienteService.findById(clienteId) == null) {
            return generateErrorResponse("El cliente ID: " + clienteId + " no existe en la base de datos", HttpStatus.NOT_FOUND);
        }
        return handleServiceCall(() -> cuentaService.cuentaByClienet(clienteId), "Error al consultar cuentas del cliente.");
    }

    @PostMapping("/clientes/{clienteId}/cuentas")
    public ResponseEntity<?> createCuenta(@Valid @RequestBody Cuenta cuenta, BindingResult result, @PathVariable Long clienteId) {
        Cliente cliente = clienteService.findById(clienteId);
        if (cliente == null) {
            return generateErrorResponse("El cliente ID: " + clienteId + " no existe en la base de datos", HttpStatus.NOT_FOUND);
        }
        if (result.hasErrors()) {
            return generateValidationErrorResponse(result);
        }
        cuenta.setCliente(cliente);
        return handleServiceCall(() -> cuentaService.save(cuenta), "Error al crear la cuenta." );
    }

    @PutMapping("/cuentas/{id}")
    public ResponseEntity<?> updateCuenta(@Valid @RequestBody Cuenta cuenta, BindingResult result, @PathVariable Long id) {
        Cuenta existingCuenta = cuentaService.findById(id);
        if (existingCuenta == null) {
            return generateErrorResponse("La cuenta Nro: " + id + " no existe en la base de datos", HttpStatus.NOT_FOUND);
        }
        if (result.hasErrors()) {
            return generateValidationErrorResponse(result);
        }

        existingCuenta.setSaldo(cuenta.getSaldo());
        existingCuenta.setEstado(cuenta.getEstado());
        return handleServiceCall(() -> cuentaService.save(existingCuenta), "Error al actualizar la cuenta.");
    }

    @DeleteMapping("/cuentas/{id}")
    public ResponseEntity<?> deleteCuenta(@PathVariable Long id) {
        return handleServiceCall(() -> {
            cuentaService.delete(id);
            return null;
        }, "Error al eliminar la cuenta.", HttpStatus.OK, "La cuenta ha sido eliminada con éxito!");
    }

    // Utility methods to streamline error handling and response generation

    private ResponseEntity<?> handleServiceCall(ServiceExecutor serviceCall, String notFoundMsg) {
        return handleServiceCall(serviceCall, notFoundMsg, HttpStatus.OK, null);
    }

    private ResponseEntity<?> handleServiceCall(ServiceExecutor serviceCall, String errorMsg, HttpStatus successStatus, String successMessage) {
        Map<String, Object> response = new HashMap<>();
        try {
            Object result = serviceCall.execute();
            if (result == null) {
                return generateErrorResponse(errorMsg, HttpStatus.NOT_FOUND);
            }
            if (successMessage != null) {
                response.put("mensaje", successMessage);
            }
            response.put("result", result);
            return new ResponseEntity<>(response, successStatus);
        } catch (DataAccessException e) {
            return generateErrorResponse("Error en la base de datos: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<?> generateValidationErrorResponse(BindingResult result) {
        List<String> errors = result.getFieldErrors()
            .stream()
            .map(err -> "El campo " + err.getField() + ": " + err.getDefaultMessage())
            .collect(Collectors.toList());

        return generateErrorResponse(errors, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<?> generateErrorResponse(Object message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", message);
        return new ResponseEntity<>(response, status);
    }

    @FunctionalInterface
    private interface ServiceExecutor {
        Object execute();
    }
}
