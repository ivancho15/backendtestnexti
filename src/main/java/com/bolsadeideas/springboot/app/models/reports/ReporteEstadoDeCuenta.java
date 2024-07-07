package com.bolsadeideas.springboot.app.models.reports;

import java.util.Date;

public class ReporteEstadoDeCuenta {

	private Date fecha;
	private String cliente;
	private Long nroCuenta;
	private String tipoCuenta;
	private Double saldoInicial;
	private Boolean estado;
	private String tipoMovimiento;
	private Double movimiento;
	private Double saldoDisponible;
	
	public ReporteEstadoDeCuenta () {
		
	}
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public Long getNroCuenta() {
		return nroCuenta;
	}

	public void setNroCuenta(Long nroCuenta) {
		this.nroCuenta = nroCuenta;
	}

	public String getTipoCuenta() {
		return tipoCuenta;
	}

	public void setTipoCuenta(String tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}

	public Double getSaldoInicial() {
		return saldoInicial;
	}

	public void setSaldoInicial(Double saldoDisponible, Double movimiento) {
		Double inicial;
		if (movimiento > 0) {
			inicial = saldoDisponible - movimiento; 
		} else {
			inicial = saldoDisponible + Math.abs(movimiento);
		}
		
		this.saldoInicial = Math.abs(inicial);
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public String getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(String tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public Double getMovimiento() {
		return movimiento;
	}

	public void setMovimiento(Double movimiento) {
		this.movimiento = movimiento;
	}

	public Double getSaldoDisponible() {
		return saldoDisponible;
	}

	public void setSaldoDisponible(Double saldoDisponible) {
		this.saldoDisponible = saldoDisponible;
	}
	
	
}
