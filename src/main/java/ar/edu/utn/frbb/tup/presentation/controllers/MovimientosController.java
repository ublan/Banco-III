package ar.edu.utn.frbb.tup.presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ar.edu.utn.frbb.tup.exception.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.CuentaSinSaldoException;
import ar.edu.utn.frbb.tup.exception.MomivientosVaciosException;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.service.MovimientoService;

import java.util.List;

@RestController
@RequestMapping("/movimientos")
public class MovimientosController {

    @Autowired
    private MovimientoService movimientoService;

    @PostMapping("/deposito/{cbu}")
    public ResponseEntity<Movimiento> realizarDeposito(@PathVariable long cbu, @RequestParam double monto) throws CuentaNoEncontradaException {
        
        return new ResponseEntity<>(movimientoService.realizarDeposito(cbu, monto), HttpStatus.OK);
    }

    @PostMapping("/retiro/{cbu}")
    public ResponseEntity<Movimiento> realizarRetiro(@PathVariable long cbu, @RequestParam double monto) throws CuentaNoEncontradaException, CuentaSinSaldoException {
        
        return new ResponseEntity<>(movimientoService.realizarRetiro(cbu, monto), HttpStatus.OK);
    }

    @GetMapping("/operaciones/{cbu}")
    public ResponseEntity<List<Movimiento>> obtenerOperacionesPorCBU(@PathVariable long cbu) throws MomivientosVaciosException {

        return new ResponseEntity<>(movimientoService.obtenerOperacionesPorCBU(cbu), HttpStatus.OK);
    }
}
