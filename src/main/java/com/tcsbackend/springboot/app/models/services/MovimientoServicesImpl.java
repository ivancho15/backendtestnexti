package com.tcsbackend.springboot.app.models.services;

import java.util.Arrays;
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
	private  IMovimientoDao movimientoDao;
	@Autowired
	private  ICuentaServices cuentaService;
    
    private static final List<String> MOVIMIENTOS_CREDITOS = Arrays.asList("Deposito", "Nota de Credito", "Transferencia Acreditada");
    private static final List<String> MOVIMIENTOS_DEBITOS = Arrays.asList("Retiro", "Debito", "Transferencia Debitada", "Pago");
    

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
        // Validar tipo de movimiento
        if (!MOVIMIENTOS_CREDITOS.contains(movimiento.getTipo()) && !MOVIMIENTOS_DEBITOS.contains(movimiento.getTipo())) {
            throw new IllegalArgumentException("Tipo de movimiento no permitido");
        }

        // Determinar si el movimiento acredita o debita
        double nuevoSaldo;
        if (MOVIMIENTOS_CREDITOS.contains(movimiento.getTipo())) {
            nuevoSaldo = cuenta.getSaldo() + movimiento.getValor();  // Acredita a la cuenta
        } else if (MOVIMIENTOS_DEBITOS.contains(movimiento.getTipo())) {
            // Validar que haya suficiente saldo antes de debitar
            if (movimiento.getValor() > cuenta.getSaldo()) {
                throw new SaldoInsuficienteException("Saldo insuficiente");
            }
            nuevoSaldo = cuenta.getSaldo() - movimiento.getValor();  // Debita de la cuenta
        } else {
            throw new IllegalArgumentException("Tipo de movimiento inválido");
        }

        // Actualizar saldo y guardar movimiento
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setCuenta(cuenta);
        cuenta.setSaldo(nuevoSaldo);
        save(movimiento);
        cuentaService.save(cuenta);
    }
}
