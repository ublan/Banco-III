package ar.edu.utn.frbb.tup.presentation.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import ar.edu.utn.frbb.tup.exception.TipoMonedasInvalidasException;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.presentation.modelDto.TransferenciaDto;
import ar.edu.utn.frbb.tup.presentation.validator.TransferenciaValidator;
import ar.edu.utn.frbb.tup.service.TransferenciaService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferenciaControllerTest {

    @Mock
    TransferenciaValidator transferenciaValidator;

    @Mock
    TransferenciaService transferenciaService;

    @InjectMocks
    TransferenciaController transferenciaController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRealizarTransferencia() throws CuentaNoEncontradaException, CuentaSinSaldoException, TipoMonedasInvalidasException {
        TransferenciaDto transferenciaDto = getTransferenciaDto();
        Transferencia transferencia = new Transferencia();

        when(transferenciaService.realizarTransferencia(transferenciaDto)).thenReturn(transferencia);

        ResponseEntity<Transferencia> response = transferenciaController.realizarTransferencia(transferenciaDto);

        verify(transferenciaService, times(1)).realizarTransferencia(transferenciaDto);
        verify(transferenciaValidator, times(1)).validarTransferencia(transferenciaDto);
        assertEquals(200, response.getStatusCodeValue());

    }

    @Test
    public void testRealizarTransferenciaError() throws CuentaNoEncontradaException, CuentaSinSaldoException, TipoMonedasInvalidasException {
        TransferenciaDto transferenciaDto = getTransferenciaDto();

        doThrow(CuentaNoEncontradaException.class).when(transferenciaService).realizarTransferencia(transferenciaDto);
        assertThrows(CuentaNoEncontradaException.class, () -> transferenciaController.realizarTransferencia(transferenciaDto));

        verify(transferenciaService, times(1)).realizarTransferencia(transferenciaDto);
        verify(transferenciaValidator, times(1)).validarTransferencia(transferenciaDto);
    }

    @Test
    public void testObtenerTransacciones() {
        long cbu = 12345678;

        List<Transferencia> transferencias = List.of(new Transferencia());

        when(transferenciaService.obtenerTransferenciasPorCbu(cbu)).thenReturn(transferencias);

        ResponseEntity<List<Transferencia>> response = transferenciaController.obtenerTransacciones(cbu);

        verify(transferenciaService, times(1)).obtenerTransferenciasPorCbu(cbu);
        
        assertEquals(200, response.getStatusCodeValue());
    }

    public TransferenciaDto getTransferenciaDto() {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setTipoTransferencia("DEBITO");
        transferenciaDto.setCuentaOrigen("12345678");
        transferenciaDto.setCuentaDestino("87654321");
        transferenciaDto.setMonto(1000.0);
        transferenciaDto.setMoneda("ARS");
        transferenciaDto.setDescripcionBreve("Test de transferencia");
        return transferenciaDto;
    }
}
