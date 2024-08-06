package ar.edu.utn.frbb.tup.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import ar.edu.utn.frbb.tup.exception.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.MovimientosDao;
import ar.edu.utn.frbb.tup.persistence.TransferenciaDao;
import ar.edu.utn.frbb.tup.presentation.modelDto.ClienteDto;
import ar.edu.utn.frbb.tup.presentation.modelDto.CuentaDto;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaServiceTest {

    @Mock
    CuentaDao cuentaDao;

    @Mock
    ClienteDao clienteDao;

    @Mock
    MovimientosDao movimientoDao;

    @Mock
    TransferenciaDao transferenciaDao;

    @InjectMocks
    CuentaService CuentaService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDarDeAltaCuentaSuccess() throws ClienteNoEncontradoException {
        CuentaDto cuentaDto = getCuentaDto();
        Cuenta cuenta = new Cuenta(cuentaDto);
        Cliente clienteExistente = new Cliente();

        when(clienteDao.findByDni(cuenta.getDniTitular())).thenReturn(clienteExistente);

        Cuenta cuentaGuardada = CuentaService.darDeAltaCuenta(cuentaDto);

        verify(clienteDao, times(1)).findByDni(cuenta.getDniTitular());
        verify(cuentaDao, times(1)).escribirEnArchivo(any(Cuenta.class));

        assertNotNull(cuentaGuardada);
    }

    @Test
    public void testDarDeAltaCuentaFail() throws ClienteNoEncontradoException {
        CuentaDto cuentaDto = getCuentaDto();
        Cuenta cuenta = new Cuenta(cuentaDto);

        when(clienteDao.findByDni(cuenta.getDniTitular())).thenReturn(null);

        assertThrows(ClienteNoEncontradoException.class, () -> CuentaService.darDeAltaCuenta(cuentaDto));

        verify(clienteDao, times(1)).findByDni(cuenta.getDniTitular());
        verify(cuentaDao, times(0)).escribirEnArchivo(any(Cuenta.class));
    }

    @Test
    public void testBorrarCuentaSuccess() throws CuentaNoEncontradaException {  
        CuentaDto cuentaDto = getCuentaDto();
        Cuenta cuenta = new Cuenta(cuentaDto);

        when(cuentaDao.borrarCuenta(cuenta.getCBU())).thenReturn(cuenta);

        Cuenta cuentaBorrada = CuentaService.borrarCuenta(cuenta.getCBU());

        verify(cuentaDao, times(1)).borrarCuenta(cuenta.getCBU());

        verify(movimientoDao, times(1)).borrarMovimiento(cuenta.getDniTitular());  
        verify(transferenciaDao, times(1)).borrarTransferencia(cuenta.getDniTitular());

        assertNotNull(cuentaBorrada);
    }

    @Test
    public void testBorrarCuentaFail() throws CuentaNoEncontradaException {
        CuentaDto cuentaDto = getCuentaDto();
        Cuenta cuenta = new Cuenta(cuentaDto);

        when(cuentaDao.borrarCuenta(cuenta.getCBU())).thenReturn(null);

        assertThrows(CuentaNoEncontradaException.class, () -> CuentaService.borrarCuenta(cuenta.getCBU()));

        verify(movimientoDao, times(0)).borrarMovimiento(cuenta.getDniTitular());  
        verify(transferenciaDao, times(0)).borrarTransferencia(cuenta.getDniTitular());

        verify(cuentaDao, times(1)).borrarCuenta(cuenta.getCBU());
    }

    @Test
    public void testMostrarCuentaSuccess() throws CuentaNoEncontradaException, ClienteNoEncontradoException {
        ClienteDto clienteDto = getClienteDto();
        Cliente cliente = new Cliente(clienteDto);

        when(clienteDao.findByDni(cliente.getDni())).thenReturn(cliente);

        Cuenta cuenta = new Cuenta();
        List<Cuenta> cuentas = List.of(cuenta);

        when(cuentaDao.obtonerCuentasDelCliente(cliente.getDni())).thenReturn(cuentas);

        List<Cuenta> cuentasMostradas = CuentaService.mostrarCuenta(cliente.getDni());
        
        verify(cuentaDao, times(1)).obtonerCuentasDelCliente(cliente.getDni());
        verify(clienteDao, times(1)).findByDni(cliente.getDni());

        assertEquals(cuentasMostradas,cuentas);
    }


    @Test
    public void testMostrarCuentaClienteNoEncontrado() throws CuentaNoEncontradaException, ClienteNoEncontradoException {
        ClienteDto clienteDto = getClienteDto();
        Cliente cliente = new Cliente(clienteDto);
        
        when(clienteDao.findByDni(cliente.getDni())).thenReturn(null);

        assertThrows(ClienteNoEncontradoException.class, () -> CuentaService.mostrarCuenta(cliente.getDni()));

        verify(clienteDao, times(1)).findByDni(cliente.getDni());
        verify(cuentaDao, times(0)).obtonerCuentasDelCliente(cliente.getDni());
    }

    @Test
    public void testMostrarCuentaCuentaNoEncontrada() throws CuentaNoEncontradaException, ClienteNoEncontradoException {
        ClienteDto clienteDto = getClienteDto();
        Cliente cliente = new Cliente(clienteDto);

        when(clienteDao.findByDni(cliente.getDni())).thenReturn(cliente);
        List<Cuenta> cuentas = List.of();
        when(cuentaDao.obtonerCuentasDelCliente(cliente.getDni())).thenReturn(cuentas);

        assertThrows(CuentaNoEncontradaException.class, () -> CuentaService.mostrarCuenta(cliente.getDni()));

        verify(clienteDao, times(1)).findByDni(cliente.getDni());
        verify(cuentaDao, times(1)).obtonerCuentasDelCliente(cliente.getDni());  
    }

    @Test
    public void testObtenerTodasLasCuentasSuccess() throws CuentaNoEncontradaException {
        CuentaDto cuentaDto = getCuentaDto();
        Cuenta cuenta = new Cuenta(cuentaDto);
        List<Cuenta> cuentas = List.of(cuenta);

        when(cuentaDao.mostrarTodasLasCuentas()).thenReturn(cuentas);

        List<Cuenta> cuentasMostradas = CuentaService.obtenerTodasLasCuentas();

        verify(cuentaDao, times(1)).mostrarTodasLasCuentas();

        assertEquals(cuentas, cuentasMostradas);
    }

    @Test
    public void testObtenerTodasLasCuentasFail() throws CuentaNoEncontradaException {
        List<Cuenta> cuentas = List.of();

        when(cuentaDao.mostrarTodasLasCuentas()).thenReturn(cuentas);

        assertThrows(CuentaNoEncontradaException.class, () -> CuentaService.obtenerTodasLasCuentas());

        verify(cuentaDao, times(1)).mostrarTodasLasCuentas();
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

    public CuentaDto getCuentaDto() {
        CuentaDto cuentadto = new CuentaDto();
        cuentadto.setNombre("Uriel");
        cuentadto.setDniTitular("12345678");
        cuentadto.setTipoCuenta("AHORRO");
        cuentadto.setTipoMoneda("ARS");
        return cuentadto;
    }
    
}
