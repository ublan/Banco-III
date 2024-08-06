package ar.edu.utn.frbb.tup.presentation.controllers;

import ar.edu.utn.frbb.tup.exception.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.model.Cuenta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ar.edu.utn.frbb.tup.presentation.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.service.CuentaService;
import ar.edu.utn.frbb.tup.presentation.modelDto.CuentaDto;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private CuentaValidator cuentaValidator;

    @PostMapping("/crearCuenta")
    public ResponseEntity<Cuenta> crearCuenta(@RequestBody CuentaDto cuentaDto) throws ClienteNoEncontradoException{
        cuentaValidator.validarCuenta(cuentaDto);      
        return new ResponseEntity<>(cuentaService.darDeAltaCuenta(cuentaDto) , HttpStatus.CREATED);
    }

    @GetMapping("/mostrar/{dni}")
    public ResponseEntity<List<Cuenta>> mostrarCuentasPorDni(@PathVariable long dni) throws CuentaNoEncontradaException, ClienteNoEncontradoException {
        List<Cuenta> cuentas = cuentaService.mostrarCuenta(dni);
        return new ResponseEntity<>(cuentas, HttpStatus.OK);
    }

    @GetMapping("/mostrar")
    public ResponseEntity<List<Cuenta>> mostrarTodasLasCuentas() throws CuentaNoEncontradaException {
        List<Cuenta> cuentas = cuentaService.obtenerTodasLasCuentas();
        return new ResponseEntity<>(cuentas, HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{cbu}")
    public ResponseEntity<Cuenta> eliminarCuentaPorCBU(@PathVariable long cbu) throws CuentaNoEncontradaException { 
        return new ResponseEntity<>(cuentaService.borrarCuenta(cbu), HttpStatus.OK);
    }
}

