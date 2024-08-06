package ar.edu.utn.frbb.tup.presentation.controllers;

import ar.edu.utn.frbb.tup.exception.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.CuentaSinSaldoException;
import ar.edu.utn.frbb.tup.exception.TipoMonedasInvalidasException;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.presentation.modelDto.TransferenciaDto;
import ar.edu.utn.frbb.tup.presentation.validator.TransferenciaValidator;
import ar.edu.utn.frbb.tup.service.TransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransferenciaController {

    @Autowired
    private TransferenciaValidator transferenciaValidator;

    @Autowired
    private TransferenciaService transferenciaService;

    @GetMapping("/cuenta/{cbu}/transacciones")
    public ResponseEntity<List<Transferencia>> obtenerTransacciones(@PathVariable long cbu) {
        
        return new ResponseEntity<>(transferenciaService.obtenerTransferenciasPorCbu(cbu), HttpStatus.OK);
    }

    @PostMapping("/transferencia")
    public ResponseEntity<Transferencia> realizarTransferencia(@RequestBody TransferenciaDto transferenciaDto) throws CuentaNoEncontradaException, CuentaSinSaldoException, TipoMonedasInvalidasException {
        transferenciaValidator.validarTransferencia(transferenciaDto);
        return new ResponseEntity<>(transferenciaService.realizarTransferencia(transferenciaDto), HttpStatus.OK);
    }

}

