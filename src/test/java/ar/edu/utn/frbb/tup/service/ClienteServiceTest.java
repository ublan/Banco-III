package ar.edu.utn.frbb.tup.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.utn.frbb.tup.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.ClienteMenorEdadException;
import ar.edu.utn.frbb.tup.exception.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.MovimientosDao;
import ar.edu.utn.frbb.tup.persistence.TransferenciaDao;
import ar.edu.utn.frbb.tup.presentation.modelDto.ClienteDto;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteServiceTest {
    
    @Mock
    CuentaDao cuentaDao;

    @Mock
    ClienteDao clienteDao;

    @Mock
    MovimientosDao movimientoDao;

    @Mock
    TransferenciaDao transferenciaDao;

    @InjectMocks
    ClienteService ClienteService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testDarDeAltaClienteSuccess() throws ClienteAlreadyExistsException , ClienteMenorEdadException {
        ClienteDto clienteDto = getClienteDto();
        Cliente cliente = new Cliente(clienteDto);
        
        when(clienteDao.findByDni(cliente.getDni())).thenReturn(null);

        Cliente clienteCreado =  ClienteService.darDeAltaCliente(clienteDto);

        verify(clienteDao, times(1)).findByDni(cliente.getDni());
        verify(clienteDao, times(1)).crearCliente(any(Cliente.class));

        assertNotNull(clienteCreado);
    }


    @Test
    public void testDarDeAltaClienteExistente() throws ClienteAlreadyExistsException {
        ClienteDto clienteDto = getClienteDto();
        Cliente cliente = new Cliente(clienteDto);
        
        when(clienteDao.findByDni(cliente.getDni())).thenReturn(cliente);

        assertThrows(ClienteAlreadyExistsException.class, () -> ClienteService.darDeAltaCliente(clienteDto));

        verify(clienteDao, times(1)).findByDni(cliente.getDni());
        verify(clienteDao, times(0)).crearCliente(any(Cliente.class));
    }

    @Test
    public void testDarDeAltaClienteMenorEdad() throws ClienteMenorEdadException {
        ClienteDto clienteDto = getClienteDto();
        clienteDto.setFechaNacimiento("2010-01-01");
        Cliente cliente = new Cliente(clienteDto);
        
        when(clienteDao.findByDni(cliente.getDni())).thenReturn(null);

        assertThrows(ClienteMenorEdadException.class, () -> ClienteService.darDeAltaCliente(clienteDto));

        verify(clienteDao, times(1)).findByDni(cliente.getDni());
        verify(clienteDao, times(0)).crearCliente(any(Cliente.class));
    }

    @Test
    public void testBorrarClienteSuccess() throws ClienteNoEncontradoException {
        ClienteDto clienteDto = getClienteDto();
        Cliente cliente = new Cliente(clienteDto);
        List<Cuenta> cuentas = List.of(new Cuenta());

        when(clienteDao.borrarCliente(cliente.getDni())).thenReturn(cliente);
        when(cuentaDao.obtonerCuentasDelCliente(cliente.getDni())).thenReturn(cuentas);

        Cliente borrado =  ClienteService.borrarCliente(cliente.getDni());

        verify(movimientoDao, times(1)).borrarMovimiento(cliente.getDni());
        verify(transferenciaDao , times(1)).borrarTransferencia(cliente.getDni());

        verify(clienteDao, times(1)).borrarCliente(cliente.getDni());
        verify(cuentaDao, times(1)).obtonerCuentasDelCliente(cliente.getDni());

        assertNotNull(borrado);
    } 
    

    @Test
    public void testBorrarClienteNoEncontrado() throws ClienteNoEncontradoException {
        ClienteDto clienteDto = getClienteDto();
        Cliente cliente = new Cliente(clienteDto);

        when(clienteDao.borrarCliente(cliente.getDni())).thenReturn(null);
        
        verify(clienteDao, times(0)).borrarCliente(cliente.getDni());
        verify(cuentaDao, times(0)).obtonerCuentasDelCliente(cliente.getDni());

        assertThrows(ClienteNoEncontradoException.class, () -> ClienteService.borrarCliente(cliente.getDni()));

        verify(movimientoDao, times(0)).borrarMovimiento(cliente.getDni());
        verify(transferenciaDao , times(0)).borrarTransferencia(cliente.getDni());

    }

    @Test
    public void testMostrarClienteSuccess() throws ClienteNoEncontradoException {
        ClienteDto clienteDto = getClienteDto();
        Cliente cliente = new Cliente(clienteDto);

        when(clienteDao.mostrarCliente(cliente.getDni())).thenReturn(cliente);

        assertNotNull(ClienteService.mostrarCliente(cliente.getDni()));

        verify(clienteDao, times(1)).mostrarCliente(cliente.getDni());
    }


    @Test
    public void testMostrarClienteNoEncontrado() throws ClienteNoEncontradoException {
        ClienteDto clienteDto = getClienteDto();
        Cliente cliente = new Cliente(clienteDto);

        when(clienteDao.mostrarCliente(cliente.getDni())).thenReturn(null);

        assertThrows(ClienteNoEncontradoException.class, () -> ClienteService.mostrarCliente(cliente.getDni()));

        verify(clienteDao, times(1)).mostrarCliente(cliente.getDni());
    }


    @Test
    public void testMostrarTodosLosClientesSuccess() throws ClienteNoEncontradoException {
        List<Cliente> clientes = List.of(new Cliente(getClienteDto()));

        when(clienteDao.mostrarTodosLosClientes()).thenReturn(clientes);

        Cliente mostrado =  ClienteService.mostrarTodosLosClientes().get(0);

        assertNotNull(mostrado);

        verify(clienteDao, times(2)).mostrarTodosLosClientes();
    }


    @Test
    public void testMostrarTodosLosClientesNoEncontrado() throws ClienteNoEncontradoException { 
        List<Cliente> clientesVacio = List.of();

        when(clienteDao.mostrarTodosLosClientes()).thenReturn(clientesVacio);

        assertThrows(ClienteNoEncontradoException.class, () -> ClienteService.mostrarTodosLosClientes());

        verify(clienteDao, times(1)).mostrarTodosLosClientes();
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
