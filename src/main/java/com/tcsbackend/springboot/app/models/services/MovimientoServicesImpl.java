package com.tcsbackend.springboot.app.models.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tcsbackend.springboot.app.exceptions.SaldoInsuficienteException;
import com.tcsbackend.springboot.app.models.dao.IMovimientoDao;
import com.tcsbackend.springboot.app.models.entity.Cuenta;
import com.tcsbackend.springboot.app.models.entity.Movimiento;

@Service
public class MovimientoServicesImpl implements IMovimientoService {

    @Autowired
    private IMovimientoDao movimientoDao;

    @Autowired
    private ICuentaServices cuentaService;

    @Override
    @Transactional(readOnly = true)
    public List<Movimiento> findAll() {
        return (List<Movimiento>)movimientoDao.findAll();
    }

    @Override
    @Transactional
    public Movimiento save(Movimiento movimiento) {
        return movimientoDao.save(movimiento);
    }

    @Override
    @Transactional(readOnly = true)
    public Movimiento findById(Long id) {
        return movimientoDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        movimientoDao.deleteById(id);
    }

    @Override
    public List<Movimiento> movimientoByCuenta(Long id) {
        return movimientoDao.findByNroCuenta(id);
    }

    @Override
    @Transactional
    public void processMovimiento(Cuenta cuenta, Movimiento movimiento) {
        // Validate movement type and value
        if (((movimiento.getTipo().equals("Retiro") && movimiento.getValor() > 0)
                || (movimiento.getTipo().equals("Deposito") && movimiento.getValor() < 0))) {
            throw new IllegalArgumentException("Inconsistencia en la operación");
        }

        // Check for sufficient funds
        if (movimiento.getValor() + cuenta.getSaldo() < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente");
        }

        // Update balance and save movement
        movimiento.setSaldo(cuenta.getSaldo() + movimiento.getValor());
        movimiento.setCuenta(cuenta);
        cuenta.setSaldo(movimiento.getSaldo());
        save(movimiento);
        cuentaService.save(cuenta);
    }
}
