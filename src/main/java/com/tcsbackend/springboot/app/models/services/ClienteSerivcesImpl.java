package com.tcsbackend.springboot.app.models.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.tcsbackend.springboot.app.models.dao.IClienteDao;
import com.tcsbackend.springboot.app.models.entity.Cliente;
import com.tcsbackend.springboot.app.models.entity.Cuenta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ClienteSerivcesImpl implements IClienteServices {

    private static final Logger logger = LoggerFactory.getLogger(ClienteSerivcesImpl.class);

    String cuentaServiceUrl = "http://localhost:8080/api/cuentas"; // URL del microservicio de Cuenta
    private final Random random = new Random();

    @Autowired
    private IClienteDao clienteDao;
    
    @Autowired
    private RestTemplate restTemplate; // Para hacer llamadas REST

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return (List<Cliente>) clienteDao.findAll();
    }

    @Override
    @Transactional
    public Cliente save(Cliente cliente) {
    	// Verificar si el clienteId ya existe
        Optional<Cliente> clienteExistentePorId = clienteDao.findClienteByclienteId(cliente.getClienteId());
        if (clienteExistentePorId.isPresent()) {
            throw new IllegalArgumentException("El cliente con el ID: " + cliente.getClienteId() + " ya existe.");
        }

        // Verificar si el email ya existe
        Optional<Cliente> clienteExistentePorEmail = clienteDao.findByEmail(cliente.getEmail());
        if (clienteExistentePorEmail.isPresent()) {
            throw new IllegalArgumentException("El cliente con el email: " + cliente.getEmail() + " ya existe.");
        }
      
        Cliente savedCliente = clienteDao.save(cliente);
        
        logger.info("Saving cliente with ID: {}", cliente.getId());
        
        crearNuevaoCuenta(savedCliente);
                
        return savedCliente;
    }

    
    @Override
    @Transactional(readOnly = true)
    public Cliente findById(Long id) {
        logger.info("Finding cliente with ID: {}", id);
        return clienteDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        logger.info("Deleting cliente with ID: {}", id);
        clienteDao.deleteById(id);
    }

    @Override
    public Optional<Cliente> findByClienteId(String clienteID) {
        logger.info("Finding cliente by clienteID: {}", clienteID);
        return clienteDao.findClienteByclienteId(clienteID);
    }
    
    @Async
    private CompletableFuture<Void> crearNuevaoCuenta(Cliente cliente) {
    	try {
        	// Generar un nuevo número de cuenta
        	Long numeroCuenta = generarNumeroCuenta();
        	
        	// Crear la solicitud para crear una nueva cuenta
            Cuenta nuevaCuenta = new Cuenta();
            nuevaCuenta.setNro_cuenta(numeroCuenta);
            nuevaCuenta.setCliente(cliente);
            nuevaCuenta.setEstado(true);
            nuevaCuenta.setTipo("Ahorros");
            nuevaCuenta.setSaldo(0.0);
            
            // Llamar al servicio de cuentas para crear la cuenta
            String urlCuentaSave = "http://localhost:8080/api/clientes/" + cliente.getId() + "/cuentas";
            restTemplate.postForEntity(urlCuentaSave, nuevaCuenta, Cuenta.class);    		
            
    	} catch (Exception e) {
            logger.error("Error al crear la cuenta para el cliente ID: {}: {}", cliente.getId(), e.getMessage());
        }
    	
    	return CompletableFuture.completedFuture(null);
    }

    
    private Long generarNumeroCuenta() {
        Long numeroCuenta;

        // Generar un número de cuenta único
        do {
            // Generar un número aleatorio de hasta 10 dígitos
            numeroCuenta = (long) (random.nextInt(1_000_000_000)); // Limita a 10 dígitos
        } while (numeroCuentaYaExiste(numeroCuenta)); // Verifica en el servicio de cuentas

        return numeroCuenta;
    }

    // verificar si el número de cuenta ya existe a través de otro microservicio
    private boolean numeroCuentaYaExiste(Long numeroCuenta) {
        try  {
        	String url = String.format("%s/%d", cuentaServiceUrl, numeroCuenta);
            ResponseEntity<?> response = restTemplate.getForEntity(url, Object.class);

            // Devuelve true si la cuenta existe (código de estado 200)
            return response.getStatusCode().is2xxSuccessful();	
        } catch (HttpClientErrorException e) {
        // Si la respuesta es 404, significa que la cuenta no existe
        	return false; // No existe
        } catch (Exception e) {
        	// Manejo de errores adicional según sea necesario
        	return true; // Si ocurre un error, asumimos que la cuenta puede existir
        }
    }
}
