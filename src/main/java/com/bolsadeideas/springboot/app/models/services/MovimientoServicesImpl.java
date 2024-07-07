package com.bolsadeideas.springboot.app.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.IMovimientoDao;
import com.bolsadeideas.springboot.app.models.entity.Movimiento;

@Service
public class MovimientoServicesImpl implements IMovimientoService {

	@Autowired 
	private IMovimientoDao movimientoDao;

	
	@Override
	@Transactional(readOnly= true)
	public List<Movimiento> findAll() {
		return (List<Movimiento>)movimientoDao.findAll();
	}

	@Override
	@Transactional
	public Movimiento save(Movimiento cuenta) {
		return  movimientoDao.save(cuenta);
	}

	@Override
	@Transactional(readOnly= true)
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
		// TODO Auto-generated method stub
		return movimientoDao.findByNroCuenta(id);
	}

}
