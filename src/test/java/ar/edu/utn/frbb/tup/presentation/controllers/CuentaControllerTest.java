package ar.edu.utn.frbb.tup.presentation.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import ar.edu.utn.frbb.tup.exception.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.presentation.modelDto.CuentaDto;
import ar.edu.utn.frbb.tup.presentation.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.service.CuentaService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaControllerTest {
    

    @Mock
    CuentaService cuentaService;

    @Mock
    CuentaValidator cuentaValidator;

    @InjectMocks
    CuentaController cuentaController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearCuentaSuccess() throws CuentaNoEncontradaException, ClienteNoEncontradoException {

        CuentaDto cuentaDto = getCuentaDto();

        when(cuentaService.darDeAltaCuenta(cuentaDto)).thenReturn(new Cuenta());

        ResponseEntity<Cuenta> response = cuentaController.crearCuenta(cuentaDto);

        verify(cuentaService, times(1)).darDeAltaCuenta(cuentaDto);

        assertEquals(201, response.getStatusCodeValue());
    }


    @Test
    public void testCrearCuentaFail() throws ClienteNoEncontradoException {

        CuentaDto cuentaDto = getCuentaDto();

        doThrow(ClienteNoEncontradoException.class).when(cuentaService).darDeAltaCuenta(cuentaDto);

        assertThrows(ClienteNoEncontradoException.class, () -> cuentaController.crearCuenta(cuentaDto));

        verify(cuentaService, times(1)).darDeAltaCuenta(cuentaDto);
    }

    @Test
    public void testBorrarCuentaSuccess() throws CuentaNoEncontradaException {

        when(cuentaService.borrarCuenta(anyLong())).thenReturn(new Cuenta());

        ResponseEntity<Cuenta> response = cuentaController.eliminarCuentaPorCBU(anyLong());

        verify(cuentaService, times(1)).borrarCuenta(anyLong());

        assertEquals(200, response.getStatusCodeValue());
    }


    @Test
    public void testBorrarCuentaFail() throws CuentaNoEncontradaException {

        doThrow(CuentaNoEncontradaException.class).when(cuentaService).borrarCuenta(anyLong());

        assertThrows(CuentaNoEncontradaException.class, () -> cuentaController.eliminarCuentaPorCBU(anyLong()));

        verify(cuentaService, times(1)).borrarCuenta(anyLong());

    }

    @Test
    public void testMostrarTodasCuentasSuccess() throws CuentaNoEncontradaException {
        
        List<Cuenta> cuentas = new ArrayList<>();

        when(cuentaService.obtenerTodasLasCuentas()).thenReturn(cuentas);

        ResponseEntity<List<Cuenta>> response = cuentaController.mostrarTodasLasCuentas();

        verify(cuentaService, times(1)).obtenerTodasLasCuentas();

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testMostrarTodasCuentasFail() throws CuentaNoEncontradaException {

        doThrow(CuentaNoEncontradaException.class).when(cuentaService).obtenerTodasLasCuentas();

        assertThrows(CuentaNoEncontradaException.class, () -> cuentaController.mostrarTodasLasCuentas());

        verify(cuentaService, times(1)).obtenerTodasLasCuentas();

    }

    @Test
    public void testMostrarCuentaPorDniSuccess() throws CuentaNoEncontradaException, ClienteNoEncontradoException {

        List<Cuenta> cuentas = new ArrayList<>();

        when(cuentaService.mostrarCuenta(anyLong())).thenReturn(cuentas);

        ResponseEntity<List<Cuenta>> response = cuentaController.mostrarCuentasPorDni(anyLong());

        verify(cuentaService, times(1)).mostrarCuenta(anyLong());

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testMostrarCuentaPorDniFail() throws CuentaNoEncontradaException, ClienteNoEncontradoException {

        doThrow(CuentaNoEncontradaException.class).when(cuentaService).mostrarCuenta(anyLong());

        assertThrows(CuentaNoEncontradaException.class, () -> cuentaController.mostrarCuentasPorDni(anyLong()));

        verify(cuentaService, times(1)).mostrarCuenta(anyLong());
    }


    public CuentaDto getCuentaDto() {
        CuentaDto cuentadto = new CuentaDto();
        cuentadto.setNombre("Uriel");
        cuentadto.setDniTitular("12345678");
        cuentadto.setTipoCuenta("AHORRO");
        cuentadto.setTipoMoneda("ARS");
        return cuentadto;
    }

    public Cuenta getCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setDniTitular(12345678);
        cuenta.setNombre("Uriel");
        cuenta.setBalance(1000.0);
        cuenta.setTipoCuenta(TipoCuenta.AHORRO);
        cuenta.setMoneda(TipoMoneda.USD);
        cuenta.setCBU(12345678);
        return cuenta;
    }


}
