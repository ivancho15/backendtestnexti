package com.bolsadeideas.springboot.app.models.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.app.models.dao.IRepoorts;
import com.bolsadeideas.springboot.app.models.entity.Movimiento;

@Service
public class ReporteServicesImpl implements IReporteServices {

	@Autowired
	private IRepoorts reporte;
	@Override
	public List<Movimiento> findMovimientosByDateBetweenAndClienteId(Date date, Date date2, Long idCliente) {
		
		return reporte.findMovimientosByDateBetweenAndClienteId(date, date2, idCliente);
	}





}
