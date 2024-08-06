package ar.edu.utn.frbb.tup.presentation.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
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
import org.springframework.http.ResponseEntity;

import ar.edu.utn.frbb.tup.exception.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.CuentaSinSaldoException;
import ar.edu.utn.frbb.tup.exception.MomivientosVaciosException;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.service.MovimientoService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MovimientoControllerTest {

    @Mock
    MovimientoService movimientoService;
    
    @InjectMocks
    MovimientosController movimientoController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRealizarDeposito() throws CuentaNoEncontradaException {

        long cbu = 12345678;
        double monto = 100;
        Movimiento movimiento = new Movimiento();

        when(movimientoService.realizarDeposito(cbu, monto)).thenReturn(movimiento);

        ResponseEntity<Movimiento> response = movimientoController.realizarDeposito(cbu, monto);

        verify(movimientoService, times(1)).realizarDeposito(cbu, monto);

        assertEquals(200, response.getStatusCodeValue());
     
    }

    @Test
    public void testRealizarDepositoError() throws CuentaNoEncontradaException {

        long cbu = 12345678;
        double monto = 100;

        doThrow(CuentaNoEncontradaException.class).when(movimientoService).realizarDeposito(cbu, monto);

        assertThrows(CuentaNoEncontradaException.class, () -> movimientoController.realizarDeposito(cbu, monto));

        verify(movimientoService, times(1)).realizarDeposito(cbu, monto);
    }

    @Test
    public void testRealizarRetiro() throws CuentaNoEncontradaException, CuentaSinSaldoException {

        long cbu = 12345678;
        double monto = 100;
        Movimiento movimiento = new Movimiento();

        when(movimientoService.realizarRetiro(cbu, monto)).thenReturn(movimiento);

        ResponseEntity<Movimiento> response = movimientoController.realizarRetiro(cbu, monto);

        verify(movimientoService, times(1)).realizarRetiro(cbu, monto);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testRealizarRetiroError() throws CuentaNoEncontradaException, CuentaSinSaldoException {

        long cbu = 12345678;
        double monto = 100;

        doThrow(CuentaNoEncontradaException.class).when(movimientoService).realizarRetiro(cbu, monto);

        assertThrows(CuentaNoEncontradaException.class, () -> movimientoController.realizarRetiro(cbu, monto));

        verify(movimientoService, times(1)).realizarRetiro(cbu, monto);
    }

    @Test
    public void testObtenerOperacionesPorCBU() throws MomivientosVaciosException{
        List<Movimiento> movimientos = List.of(new Movimiento());
        
        when(movimientoService.obtenerOperacionesPorCBU(12345678)).thenReturn(movimientos);

        ResponseEntity<List<Movimiento>> operaciones = movimientoController.obtenerOperacionesPorCBU(12345678);

        verify(movimientoService, times(1)).obtenerOperacionesPorCBU(12345678);

        assertEquals(200, operaciones.getStatusCodeValue());
    }

    @Test
    public void testObtenerOperacionesPorCBUEmpty() throws MomivientosVaciosException {
        
        doThrow(MomivientosVaciosException.class).when(movimientoService).obtenerOperacionesPorCBU(anyLong());

        assertThrows(MomivientosVaciosException.class, () -> movimientoController.obtenerOperacionesPorCBU(anyLong()));

        verify(movimientoService, times(1)).obtenerOperacionesPorCBU(anyLong());
    }
}
