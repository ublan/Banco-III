package ar.edu.utn.frbb.tup.presentation.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ar.edu.utn.frbb.tup.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.ClienteMenorEdadException;
import ar.edu.utn.frbb.tup.exception.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import ar.edu.utn.frbb.tup.presentation.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.service.ClienteService;
import ar.edu.utn.frbb.tup.presentation.modelDto.ClienteDto;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteValidator clienteValidator;
    
    @PostMapping
    public ResponseEntity<Cliente> darDeAltaCliente(@RequestBody ClienteDto clientedto) throws ClienteAlreadyExistsException, ClienteMenorEdadException {
        clienteValidator.validarCliente(clientedto);
        return new ResponseEntity<>(clienteService.darDeAltaCliente(clientedto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Cliente> borrarCliente(@PathVariable long dni) throws ClienteNoEncontradoException {   
        return new ResponseEntity<>(clienteService.borrarCliente(dni), HttpStatus.OK);
    }

    @PutMapping("/{dni}")
    public ResponseEntity<Cliente> modificarCliente(@PathVariable long dni, @RequestBody ClienteDto clientedto) throws ClienteNoEncontradoException {
        clienteValidator.validarCliente(clientedto);
        return new ResponseEntity<>(clienteService.modificarCliente(clientedto), HttpStatus.OK);
    }

    @GetMapping("/{dni}")
    public ResponseEntity<Cliente> mostrarCliente(@PathVariable long dni) throws ClienteNoEncontradoException {
        return new ResponseEntity<>(clienteService.mostrarCliente(dni), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> getTodosLosClientes() throws ClienteNoEncontradoException {
        return new ResponseEntity<>(clienteService.mostrarTodosLosClientes(), HttpStatus.OK);   
    }

}
