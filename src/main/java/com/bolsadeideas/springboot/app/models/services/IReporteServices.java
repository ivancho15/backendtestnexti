package com.bolsadeideas.springboot.app.models.services;

import java.util.Date;
import java.util.List;

import com.bolsadeideas.springboot.app.models.entity.Movimiento;

public interface IReporteServices {
	
	List<Movimiento> findMovimientosByDateBetweenAndClienteId(Date date, Date date2, Long idCliente);

}
