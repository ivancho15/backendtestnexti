package com.tcsbackend.springboot.app.models.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@MappedSuperclass
public abstract class Persona implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Long id;
	
	@NotEmpty(message = "no puede ser vacio")
	@NotBlank(message="no puede estar vacio")
	@Size(min=2, max=12, message="El tamaño tiene que estar entre 2 y 12 caracteres")
	@Column(nullable=false)
	protected String nombre;
	
	@NotEmpty(message = "no puede ser vacio")
	@NotBlank(message="no puede estar vacio")
	@Size(min=2, max=30, message="El tamaño tiene que estar entre 2 y 30 caracteres")
	@Column(nullable=false)
	protected String apellido;
	
	@NotEmpty(message = "no puede ser vacio")
	@NotBlank(message="no puede estar vacio")
	@Email(message = "no es una dirección de correo bien formada")
	@Column(nullable=false, unique=true)
	protected String email;
	
	@NotEmpty(message = "no puede ser vacio")
	@NotBlank(message="no puede estar vacio")
	@Size(min=10, max=30, message="El tamaño tiene que estar entre 10 y 12 caracteres")
	@Column(nullable=false, unique=true)
	protected String direccion;
	
	@NotNull(message = "no puede ser vacio")
	@Column(nullable=false)
	protected Integer edad;
	
	@NotEmpty(message = "no puede ser vacio")
	@NotBlank(message="no puede estar vacio")
	@Column(nullable=false)
	protected String telefono;
	
	@Column(name="created_at")
	@Temporal(TemporalType.DATE)
	protected Date createAt;
	
	@PrePersist
	public void prePersist() {
		createAt = new Date();
	}

	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateAt() {
		return createAt;
	}

	
	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direcccion) {
		this.direccion = direcccion;
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}


	private static final long serialVersionUID = 1L;
}
