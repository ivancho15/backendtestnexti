package com.tcsbackend.springboot.app.models.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcsbackend.springboot.app.models.dao.ICuentaDao;
import com.tcsbackend.springboot.app.models.entity.Cuenta;
import com.tcsbackend.springboot.app.models.entity.Movimiento;

@Service
public class CuentaServicesImpl implements ICuentaServices {

    private final ICuentaDao cuentaDao;
    private final IMovimientoService movimientoService;

    // Constructor que inyecta las dependencias
    @Autowired
    public CuentaServicesImpl(ICuentaDao cuentaDao, @Lazy IMovimientoService movimientoService) {
        this.cuentaDao = cuentaDao;
        this.movimientoService = movimientoService;
    }
    
    @Override
    @Transactional(readOnly= true)
    public List<Cuenta> findAll() {
        return (List<Cuenta>)cuentaDao.findAll();
    }

    @Override
    @Transactional
    public Cuenta save(Cuenta cuenta) {
    	
    	boolean esCuentaNueva = cuentaDao.existsById(cuenta.getNro_cuenta()); // Detecta si es una cuenta nueva
    	
        if (!esCuentaNueva) {
            // Guardar la cuenta primero
            Cuenta nuevaCuenta = cuentaDao.save(cuenta);
            
            // Crear el movimiento inicial (tipo "Deposito")
            Movimiento movimientoInicial = new Movimiento();
            movimientoInicial.setTipo("Deposito");
            movimientoInicial.setValor(nuevaCuenta.getSaldo());  // Usar el saldo inicial de la cuenta
            movimientoInicial.setFecha(new Date());
            movimientoInicial.setCuenta(nuevaCuenta);
            movimientoInicial.setSaldo(nuevaCuenta.getSaldo());

            // Guardar el movimiento inicial
            movimientoService.save(movimientoInicial);

            return nuevaCuenta;
            
        } else {
            // Si la cuenta ya existe, solo se actualiza sin crear el movimiento inicial
            return cuentaDao.save(cuenta);
        }
    }

    @Override
    @Transactional(readOnly= true)
    public Cuenta findById(Long id) {
        return cuentaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        cuentaDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly= true)
    public List<Cuenta> cuentaByClienet(Long clienteID) {
        return cuentaDao.findByClienteId(clienteID);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByNroCuenta(Long nroCuenta) {
        return cuentaDao.existsById(nroCuenta);
    }
}
