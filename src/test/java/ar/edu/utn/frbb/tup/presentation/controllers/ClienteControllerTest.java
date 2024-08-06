package ar.edu.utn.frbb.tup.presentation.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import ar.edu.utn.frbb.tup.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.ClienteMenorEdadException;
import ar.edu.utn.frbb.tup.exception.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.presentation.modelDto.ClienteDto;
import ar.edu.utn.frbb.tup.presentation.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.service.ClienteService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteControllerTest {

    @Mock
    ClienteService clienteService;

    @Mock
    ClienteValidator clienteValidator;

    @InjectMocks
    ClienteController clienteController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testMostrarTodosLosClientesSuccess () throws ClienteNoEncontradoException{
        List<Cliente> clientes = new ArrayList<>();

        when(clienteService.mostrarTodosLosClientes()).thenReturn(clientes);

        ResponseEntity<List<Cliente>> mostrados = clienteController.getTodosLosClientes();

        verify(clienteService, times(1)).mostrarTodosLosClientes();

        assertEquals(200, mostrados.getStatusCodeValue());
    }

    @Test
    public void testMostrarTodosLosClientesError () throws ClienteNoEncontradoException{

        doThrow(new ClienteNoEncontradoException("No se encontraron clientes")).when(clienteService).mostrarTodosLosClientes();

        assertThrows(ClienteNoEncontradoException.class, () -> clienteController.getTodosLosClientes());

        verify(clienteService, times(1)).mostrarTodosLosClientes();
    }

    @Test
    public void testMostrarClienteSuccess() throws ClienteNoEncontradoException{
        Cliente cliente = getCliente();

        when(clienteService.mostrarCliente(cliente.getDni())).thenReturn(cliente);

        ResponseEntity<Cliente> clientemostrado = clienteController.mostrarCliente(cliente.getDni());

        verify(clienteService, times(1)).mostrarCliente(cliente.getDni());

        assertEquals(200, clientemostrado.getStatusCodeValue());
        assertEquals(clientemostrado.getBody(), cliente);
    }

    @Test
    public void testMostrarClienteError() throws ClienteNoEncontradoException{

        doThrow(new ClienteNoEncontradoException("No se encontro el cliente")).when(clienteService).mostrarCliente(anyLong());

        assertThrows(ClienteNoEncontradoException.class, () -> clienteController.mostrarCliente(12345678));

        verify(clienteService, times(1)).mostrarCliente(anyLong());

    }

    @Test
    public void testClienteModificarSuccess() throws ClienteNoEncontradoException {

        ClienteDto clienteDto = getClienteDto();
        when(clienteService.modificarCliente(clienteDto)).thenReturn(new Cliente());

        ResponseEntity<Cliente> modificado = clienteController.modificarCliente(12345678,clienteDto);

        verify(clienteService, times(1)).modificarCliente(clienteDto);
        verify(clienteValidator, times(1)).validarCliente(clienteDto);

        assertEquals(200, modificado.getStatusCodeValue());
    }

    @Test
    public void testClienteModificarFail() throws ClienteNoEncontradoException {

        ClienteDto clienteDto = getClienteDto();

        doThrow(new ClienteNoEncontradoException("No se encontro el cliente")).when(clienteService).modificarCliente(clienteDto);

        assertThrows(ClienteNoEncontradoException.class, () -> clienteController.modificarCliente(12345678, clienteDto));

        verify(clienteService, times(1)).modificarCliente(clienteDto);
        verify(clienteValidator, times(1)).validarCliente(clienteDto);
    }

    @Test
    public void testBorrarClienteSuccess() throws ClienteNoEncontradoException {

        when(clienteService.borrarCliente(12345678)).thenReturn(new Cliente());

        ResponseEntity<Cliente> borrado = clienteController.borrarCliente(12345678);

        verify(clienteService, times(1)).borrarCliente(12345678);

        assertEquals(200, borrado.getStatusCodeValue());
    }

    @Test
    public void testBorrarClienteFail() throws ClienteNoEncontradoException {

        doThrow(new ClienteNoEncontradoException("No se encontro el cliente")).when(clienteService).borrarCliente(anyLong());

        assertThrows(ClienteNoEncontradoException.class, () -> clienteController.borrarCliente(12345678));

        verify(clienteService, times(1)).borrarCliente(anyLong());
    }

    @Test
    public void testDarDeAltaClienteSuccess() throws ClienteAlreadyExistsException, ClienteMenorEdadException {

        ClienteDto clienteDto = getClienteDto();

        when(clienteService.darDeAltaCliente(clienteDto)).thenReturn(new Cliente());

        ResponseEntity<Cliente> alta = clienteController.darDeAltaCliente(clienteDto);

        verify(clienteService, times(1)).darDeAltaCliente(clienteDto);
        verify(clienteValidator, times(1)).validarCliente(clienteDto);

        assertEquals(201, alta.getStatusCodeValue());
    }

    @Test
    public void testDarDeAltaClienteFail() throws ClienteAlreadyExistsException, ClienteMenorEdadException {

        ClienteDto clienteDto = getClienteDto();

        doThrow(new ClienteAlreadyExistsException("El cliente ya existe")).when(clienteService).darDeAltaCliente(clienteDto);

        assertThrows(ClienteAlreadyExistsException.class, () -> clienteController.darDeAltaCliente(clienteDto));

        verify(clienteService, times(1)).darDeAltaCliente(clienteDto);
        verify(clienteValidator, times(1)).validarCliente(clienteDto);
    }


    public Cliente getCliente() {
        Cliente cliente = new Cliente();
        cliente.setDni(12345678);
        cliente.setNombre("Juan");
        cliente.setApellido("Perez");
        cliente.setDireccion("Calle Falsa 123");
        cliente.setBanco("Banco Nacion");
        cliente.setFechaNacimiento(LocalDate.of(2001, 1, 1));
        cliente.setTipoPersona(TipoPersona.PERSONA_FISICA);
        return cliente;
    }

    public ClienteDto getClienteDto() {
        ClienteDto clientedto = new ClienteDto();
        clientedto.setDni("12345678");
        clientedto.setNombre("Juan");
        clientedto.setApellido("Perez");
        clientedto.setDireccion("Calle Falsa 123");
        clientedto.setBanco("Banco Nacion");
        clientedto.setFechaNacimiento("2001-01-01");
        clientedto.setTipoPersona("PERSONA_FISICA");
        return clientedto;
    }
}
