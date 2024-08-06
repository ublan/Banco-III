package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.exception.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.MovimientosDao;
import ar.edu.utn.frbb.tup.persistence.TransferenciaDao;
import ar.edu.utn.frbb.tup.presentation.modelDto.CuentaDto;


import java.util.List;

@Service
public class CuentaService {

    @Autowired
    private CuentaDao cuentaDao;

    @Autowired
    private ClienteDao clienteDao;

    @Autowired
    private MovimientosDao movimientoDao;

    @Autowired
    private TransferenciaDao transferenciaDao;


    public Cuenta darDeAltaCuenta(CuentaDto cuentaDto) throws ClienteNoEncontradoException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        Cliente clienteExistente = clienteDao.findByDni(cuenta.getDniTitular());
        if (clienteExistente == null) {
            throw new ClienteNoEncontradoException("El titular de la cuenta no existe");
        }
        cuentaDao.escribirEnArchivo(cuenta);
        return cuenta;
    }

    public Cuenta borrarCuenta(long cbu) throws CuentaNoEncontradaException {
        Cuenta cuenta = cuentaDao.borrarCuenta(cbu);
        if (cuenta == null) {
            throw new  CuentaNoEncontradaException("No se encontro la cuenta con cbu: " + cbu); 
        }
        movimientoDao.borrarMovimiento(cuenta.getDniTitular());
        transferenciaDao.borrarTransferencia(cuenta.getDniTitular());
        return cuenta;
    }

    public List<Cuenta> mostrarCuenta(long dni)throws CuentaNoEncontradaException, ClienteNoEncontradoException {
        Cliente clienteExistente = clienteDao.findByDni(dni);
        if (clienteExistente == null) {
            throw new ClienteNoEncontradoException("El titular de la cuenta no existe");
        }
        List<Cuenta> cuentas = cuentaDao.obtonerCuentasDelCliente(dni);
        if (cuentas.isEmpty()) {
            throw new  CuentaNoEncontradaException("No se encontro el cliente con dni: " + dni); 
        }
        return cuentas;
    }

    public List<Cuenta> obtenerTodasLasCuentas() throws CuentaNoEncontradaException {
        List<Cuenta> cuentas = cuentaDao.mostrarTodasLasCuentas();
        if (cuentas.isEmpty()) {
            throw new  CuentaNoEncontradaException("No se encontraron cuentas"); 
        }
        return cuentas;
    }
}





