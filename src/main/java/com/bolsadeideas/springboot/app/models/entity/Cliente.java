package com.bolsadeideas.springboot.app.models.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="clientes")
public class Cliente extends Persona{
	
	private static final long serialVersionUID = -6829950559022648312L;

	@NotEmpty(message="no puede ser nulo")
	@NotBlank(message="no puede estar vacio")
	@Size(min=7, max=12, message="El tamaño tiene que estar entre 2 y 12 caracteres")
	private String clienteId;
	
	@NotEmpty(message="no puede ser nulo")
	@NotBlank(message="no puede estar vacio")
	@Size(min=4, max=12, message="El tamaño tiene que estar entre  4 y 12 caracteres")
	private String password;
	
	@NotNull(message="no puede ser nulo")
	private Boolean estado;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente")
	private List<Cuenta> cuentas;
	
	public Cliente() {
		
	}
	
	public String getClienteId() {
		return clienteId;
	}

	public void setClienteId(String clienteId) {
		this.clienteId = clienteId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public List<Cuenta> getCuentas() {
		return cuentas;
	}

	public void setCuentas(List<Cuenta> cuentas) {
		this.cuentas = cuentas;
	}
	

}
