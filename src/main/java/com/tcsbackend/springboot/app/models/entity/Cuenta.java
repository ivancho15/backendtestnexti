package com.tcsbackend.springboot.app.models.entity;


import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="cuentas")
public class Cuenta {
	
	@Id
	@Digits(integer=6, fraction=0)
	private Long nro_cuenta;
	
	@NotEmpty(message = "no puede ser vacio")
	private String tipo;
	
	@NotNull(message = "no puede ser vacio")
	private Double saldo;
	
	@NotNull(message = "no puede ser vacio")
	private  Boolean estado;
	
	@JsonIgnore
	@ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="id_cliente", referencedColumnName="id", nullable=false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Cliente cliente;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy ="cuenta")
	private List<Movimiento> movimientos;

	public Long getNro_cuenta() {
		return nro_cuenta;
	}

	public void setNro_cuenta(Long nro_cuenta) {
		this.nro_cuenta = nro_cuenta;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
}
