package com.tcsbackend.springboot.app.models.services;

import java.util.List;

import com.tcsbackend.springboot.app.models.entity.Cuenta;

public interface ICuentaServices {
	public List<Cuenta> findAll();
	public Cuenta save (Cuenta cuenta);
	public Cuenta findById(Long id);
	public void delete(Long id);
	public List<Cuenta> cuentaByClienet(Long clienteId);

}
