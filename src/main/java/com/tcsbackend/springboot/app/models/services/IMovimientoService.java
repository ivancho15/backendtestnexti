package com.tcsbackend.springboot.app.models.services;

import java.util.List;

import com.tcsbackend.springboot.app.models.entity.Cuenta;
import com.tcsbackend.springboot.app.models.entity.Movimiento;

public interface IMovimientoService {
	public List<Movimiento> findAll();
	public Movimiento save (Movimiento movimiento);
	public Movimiento findById(Long id);
	public List<Movimiento> movimientoByCuenta(Long id);
	public void delete(Long id);
	public void processMovimiento(Cuenta cuenta, Movimiento movimiento);

}
