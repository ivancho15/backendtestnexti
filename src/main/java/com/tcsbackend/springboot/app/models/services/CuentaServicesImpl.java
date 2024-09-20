package com.tcsbackend.springboot.app.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcsbackend.springboot.app.models.dao.ICuentaDao;
import com.tcsbackend.springboot.app.models.entity.Cuenta;

@Service
public class CuentaServicesImpl implements ICuentaServices {

    @Autowired 
    private ICuentaDao cuentaDao;

    @Override
    @Transactional(readOnly= true)
    public List<Cuenta> findAll() {
        return (List<Cuenta>)cuentaDao.findAll();
    }

    @Override
    @Transactional
    public Cuenta save(Cuenta cuenta) {
        return cuentaDao.save(cuenta);
    }

    @Override
    @Transactional(readOnly= true)
    public Cuenta findById(Long id) {
        return cuentaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        cuentaDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly= true)
    public List<Cuenta> cuentaByClienet(Long clienteID) {
        return cuentaDao.findByClienteId(clienteID);
    }
}
