package com.tcsbackend.springboot.app.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.tcsbackend.springboot.app.exceptions.SaldoInsuficienteException;
import com.tcsbackend.springboot.app.models.entity.Cuenta;
import com.tcsbackend.springboot.app.models.entity.Movimiento;
import com.tcsbackend.springboot.app.models.services.ICuentaServices;
import com.tcsbackend.springboot.app.models.services.IMovimientoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class MovimientoRestController {

    @Autowired
    private IMovimientoService movimientoService;

    @Autowired
    private ICuentaServices cuentaService;

    // Helper method to handle validation errors
    private ResponseEntity<Map<String, Object>> handleValidationErrors(BindingResult result) {
        List<String> errors = result.getFieldErrors().stream()
            .map(err -> "El campo " + err.getField() + " " + err.getDefaultMessage())
            .collect(Collectors.toList());
        return new ResponseEntity<>(Map.of("errors", errors), HttpStatus.BAD_REQUEST);
    }

    // Helper method to handle database errors
    private ResponseEntity<Map<String, Object>> handleDatabaseError(DataAccessException e) {
        return new ResponseEntity<>(Map.of(
            "mensaje", "Error en la base de datos",
            "error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage())),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @GetMapping("/movimientos")
    public ResponseEntity<List<Movimiento>> getAllMovimientos() {
        return ResponseEntity.ok(movimientoService.findAll());
    }

    @GetMapping("/movimientos/{id}")
    public ResponseEntity<?> getMovimiento(@PathVariable Long id) {
        try {
            Movimiento movimiento = movimientoService.findById(id);
            if (movimiento == null) {
                return new ResponseEntity<>(Map.of("mensaje", "Movimiento no encontrado"), HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(movimiento);
        } catch (DataAccessException e) {
            return handleDatabaseError(e);
        }
    }

    @GetMapping("/cuentas/{cuentaId}/movimientos")
    public ResponseEntity<?> getMovimientosByCuenta(@PathVariable Long cuentaId) {
        try {
            Cuenta cuenta = cuentaService.findById(cuentaId);
            if (cuenta == null) {
                return new ResponseEntity<>(Map.of("mensaje", "Cuenta no encontrada"), HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(Map.of("movimientos", movimientoService.movimientoByCuenta(cuentaId)));
        } catch (DataAccessException e) {
            return handleDatabaseError(e);
        }
    }

    @PostMapping("/cuentas/{nroCuenta}/movimientos")
    public ResponseEntity<?> createMovimiento(@Valid @RequestBody Movimiento movimiento, BindingResult result,
                                              @PathVariable Long nroCuenta) {
        if (result.hasErrors()) {
            return handleValidationErrors(result);
        }

        try {
            Cuenta cuenta = cuentaService.findById(nroCuenta);
            if (cuenta == null) {
                return new ResponseEntity<>(Map.of("mensaje", "Cuenta no encontrada"), HttpStatus.NOT_FOUND);
            }
            
            movimientoService.processMovimiento(cuenta, movimiento);

            return new ResponseEntity<>(Map.of(
                "mensaje", "Movimiento registrado con éxito",
                "movimiento", movimiento), HttpStatus.CREATED);
        } catch (SaldoInsuficienteException e) {
            return new ResponseEntity<>(Map.of("mensaje", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            return handleDatabaseError(e);
        }
    }
}
