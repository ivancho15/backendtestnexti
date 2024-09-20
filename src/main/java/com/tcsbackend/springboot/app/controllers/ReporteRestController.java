package com.tcsbackend.springboot.app.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcsbackend.springboot.app.models.entity.Movimiento;
import com.tcsbackend.springboot.app.models.reports.ReporteEstadoDeCuenta;
import com.tcsbackend.springboot.app.models.services.IReporteServices;

@RestController
@RequestMapping("/api")
public class ReporteRestController {
	
	@Autowired
	private IReporteServices reporteService;
	
	@GetMapping("/reporte/estadoDeCuentaFechaCliente")
	public ResponseEntity<?> reporteFechaCliente(@RequestParam(name= "dateBefore", defaultValue="")String dateBefore, 
	        @RequestParam(name= "dateAfter", defaultValue="")String dateAfter,
	        @RequestParam(name= "idCliente", defaultValue="")Long idCliente) throws ParseException{
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date desde = formatter.parse(dateBefore);
		Date hasta = formatter.parse(dateAfter);
		
		List <Movimiento> movimientos = new ArrayList<Movimiento>();
		List <ReporteEstadoDeCuenta> reportes = new ArrayList<ReporteEstadoDeCuenta>();
		
		Map<String, Object> response = new HashMap<>();
		

		try {
			movimientos = reporteService.findMovimientosByDateBetweenAndClienteId(desde, hasta, idCliente);
		}catch(DataAccessException e){
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(movimientos.isEmpty()) {
			response.put("mensaje", "No se encontraro resultados para la consulta");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		} else {
			
			movimientos.forEach(m -> {
				ReporteEstadoDeCuenta reporte = new ReporteEstadoDeCuenta();
				String nombreCliente = m.getCuenta().getCliente().getNombre().concat(" ").concat(m.getCuenta().getCliente().getApellido());
				reporte.setCliente(nombreCliente);
				reporte.setEstado(m.getCuenta().getEstado());
				reporte.setFecha(m.getFecha());
				reporte.setMovimiento(m.getValor());
				reporte.setNroCuenta(m.getCuenta().getNro_cuenta());
				reporte.setSaldoDisponible(m.getSaldo());
				reporte.setSaldoInicial(m.getValor(), m.getSaldo());
				reporte.setTipoCuenta(m.getCuenta().getTipo());
				reporte.setTipoMovimiento(m.getTipo());
				reportes.add(reporte);
			});
		}
		response.put("movimientos", reportes);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	
		
	}
	
	

}
