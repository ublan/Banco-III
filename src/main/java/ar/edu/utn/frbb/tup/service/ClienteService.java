package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.ClienteMenorEdadException;
import ar.edu.utn.frbb.tup.exception.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.utn.frbb.tup.presentation.modelDto.ClienteDto;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.MovimientosDao;
import ar.edu.utn.frbb.tup.persistence.TransferenciaDao;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteDao clienteDao;

    @Autowired
    private CuentaDao cuentaDao;

    @Autowired
    private MovimientosDao movimientoDao;

    @Autowired
    private TransferenciaDao transferenciaDao;


    public Cliente darDeAltaCliente(ClienteDto clientedto) throws ClienteAlreadyExistsException, ClienteMenorEdadException {
        Cliente cliente = new Cliente(clientedto);
        Cliente clienteExistente = clienteDao.findByDni(cliente.getDni());
        if (clienteExistente != null) {
            throw new ClienteAlreadyExistsException("Ya existe un cliente con DNI " + cliente.getDni());
        }
        if (cliente.getEdad() < 18) {
            throw new ClienteMenorEdadException("El cliente debe ser mayor de 18 anios");    
        }
        clienteDao.crearCliente(cliente);
        return cliente;
    }
    
    
    public Cliente borrarCliente(long dni) throws ClienteNoEncontradoException {
        Cliente cliente = clienteDao.borrarCliente(dni);
        
        if (cliente == null) {
            throw new ClienteNoEncontradoException("No se encontro el cliente con ese dni");   
        }

        List<Cuenta> cuentas = cuentaDao.obtonerCuentasDelCliente(dni);
        for (Cuenta cuenta : cuentas) {
            cuentaDao.borrarCuenta(cuenta.getCBU());
        }
        movimientoDao.borrarMovimiento(dni);
        transferenciaDao.borrarTransferencia(dni);

        return cliente;
    }

    public Cliente modificarCliente(ClienteDto clientedto) throws ClienteNoEncontradoException {
        Cliente cliente = new Cliente(clientedto);
        clienteDao.modificarCliente(cliente);

        return cliente;
    }

    public Cliente mostrarCliente(long dni) throws ClienteNoEncontradoException {
        Cliente cliente = clienteDao.mostrarCliente(dni);
        if (cliente == null) {
            throw new ClienteNoEncontradoException("No se encontro el cliente con dni: " + dni);
        }
        return cliente;
    }

    public List<Cliente> mostrarTodosLosClientes() throws ClienteNoEncontradoException {
        List<Cliente> todosLosClientes = clienteDao.mostrarTodosLosClientes();
        if (todosLosClientes.isEmpty()) {
            throw new ClienteNoEncontradoException("No se encontraron clientes");
        }
        return clienteDao.mostrarTodosLosClientes();
    }
}



