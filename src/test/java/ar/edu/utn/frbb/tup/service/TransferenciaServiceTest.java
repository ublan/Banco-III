package ar.edu.utn.frbb.tup.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

import ar.edu.utn.frbb.tup.exception.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.CuentaSinSaldoException;
import ar.edu.utn.frbb.tup.exception.TipoMonedasInvalidasException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.model.TipoTransferencia;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.TransferenciaDao;
import ar.edu.utn.frbb.tup.presentation.modelDto.TransferenciaDto;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferenciaServiceTest {
    

    @Mock
    BanelcoService banelcoService;

    @Mock
    CuentaDao cuentaDao;

    @Mock
    TransferenciaDao transferenciaDao;

    @Mock
    ClienteDao clienteDao;

    @InjectMocks
    TransferenciaService transferenciaService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testRealizarTransferenciaSuccess() throws CuentaNoEncontradaException, CuentaSinSaldoException, TipoMonedasInvalidasException {
        Cliente clienteOrigen = getCliente();
        Cliente clienteDestino = getCliente();

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setCBU(123456789);
        cuentaOrigen.setBalance(2000);
        cuentaOrigen.setMoneda(TipoMoneda.ARS);
        cuentaOrigen.setDniTitular(12345678);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setCBU(987654321);
        cuentaDestino.setBalance(500);
        cuentaDestino.setMoneda(TipoMoneda.ARS);
        cuentaDestino.setDniTitular(87654321);

        when(cuentaDao.obtenerCuentaPorCBU(cuentaOrigen.getDniTitular())).thenReturn(cuentaOrigen);
        when(cuentaDao.obtenerCuentaPorCBU(cuentaDestino.getDniTitular())).thenReturn(cuentaDestino);

        when(clienteDao.findByDni(cuentaOrigen.getDniTitular())).thenReturn(clienteOrigen);
        when(clienteDao.findByDni(cuentaDestino.getDniTitular())).thenReturn(clienteDestino);

        TransferenciaDto transferenciaDto = getTransferenciaDto();

        Transferencia transferencia = transferenciaService.realizarTransferencia(transferenciaDto);

        verify(cuentaDao,times(2)).obtenerCuentaPorCBU(any(Long.class));
        verify(clienteDao,times(2)).findByDni(any(Long.class));
        verify(cuentaDao,times(2)).borrarCuenta(any(Long.class));
        verify(cuentaDao,times(2)).escribirEnArchivo(any(Cuenta.class));
        verify(transferenciaDao,times(1)).guardarTransferencia(any(Transferencia.class));
        

        assertNotNull(transferencia);
    }

    @Test
    public void testTransferenciaCuentaNoEncontradaOrigen() throws CuentaNoEncontradaException, CuentaSinSaldoException, TipoMonedasInvalidasException {

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setCBU(123456789);
        cuentaOrigen.setBalance(2000);
        cuentaOrigen.setMoneda(TipoMoneda.ARS);
        cuentaOrigen.setDniTitular(12345678);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setCBU(987654321);
        cuentaDestino.setBalance(500);
        cuentaDestino.setMoneda(TipoMoneda.ARS);
        cuentaDestino.setDniTitular(87654321);

        when(cuentaDao.obtenerCuentaPorCBU(cuentaOrigen.getDniTitular())).thenReturn(null);
        when(cuentaDao.obtenerCuentaPorCBU(cuentaDestino.getDniTitular())).thenReturn(cuentaDestino);

        assertThrows(CuentaNoEncontradaException.class, () -> transferenciaService.realizarTransferencia(getTransferenciaDto()));

        verify(cuentaDao,times(2)).obtenerCuentaPorCBU(any(Long.class));

    }

    @Test
    public void testTransferenciaCuentaNoEncontradaDestino() throws CuentaNoEncontradaException, CuentaSinSaldoException, TipoMonedasInvalidasException {

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setCBU(123456789);
        cuentaOrigen.setBalance(2000);
        cuentaOrigen.setMoneda(TipoMoneda.ARS);
        cuentaOrigen.setDniTitular(12345678);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setCBU(987654321);
        cuentaDestino.setBalance(500);
        cuentaDestino.setMoneda(TipoMoneda.ARS);
        cuentaDestino.setDniTitular(87654321);

        when(cuentaDao.obtenerCuentaPorCBU(cuentaOrigen.getDniTitular())).thenReturn(cuentaOrigen);
        when(cuentaDao.obtenerCuentaPorCBU(cuentaDestino.getDniTitular())).thenReturn(null);

        assertThrows(CuentaNoEncontradaException.class, () -> transferenciaService.realizarTransferencia(getTransferenciaDto()));

        verify(cuentaDao,times(2)).obtenerCuentaPorCBU(any(Long.class));
    }

    @Test
    public void testTransferenciaCuentaSinSaldo() throws CuentaNoEncontradaException, CuentaSinSaldoException, TipoMonedasInvalidasException {
        Cliente clienteOrigen = getCliente();
        Cliente clienteDestino = getCliente();

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setCBU(123456789);
        cuentaOrigen.setBalance(0);
        cuentaOrigen.setMoneda(TipoMoneda.ARS);
        cuentaOrigen.setDniTitular(12345678);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setCBU(987654321);
        cuentaDestino.setBalance(500);
        cuentaDestino.setMoneda(TipoMoneda.ARS);
        cuentaDestino.setDniTitular(87654321);

        when(cuentaDao.obtenerCuentaPorCBU(cuentaOrigen.getDniTitular())).thenReturn(cuentaOrigen);
        when(cuentaDao.obtenerCuentaPorCBU(cuentaDestino.getDniTitular())).thenReturn(cuentaDestino);

        when(clienteDao.findByDni(cuentaOrigen.getDniTitular())).thenReturn(clienteOrigen);
        when(clienteDao.findByDni(cuentaDestino.getDniTitular())).thenReturn(clienteDestino);

        assertThrows(CuentaSinSaldoException.class, () -> transferenciaService.realizarTransferencia(getTransferenciaDto()));

        verify(cuentaDao,times(2)).obtenerCuentaPorCBU(any(Long.class));
        verify(clienteDao,times(2)).findByDni(any(Long.class));

    }
    

    @Test
    public void testTransferenciaTipoMonedasInvalidas() throws CuentaNoEncontradaException, CuentaSinSaldoException, TipoMonedasInvalidasException {

        Cliente clienteOrigen = getCliente();
        Cliente clienteDestino = getCliente();

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setCBU(123456789);
        cuentaOrigen.setBalance(2000);
        cuentaOrigen.setMoneda(TipoMoneda.USD);
        cuentaOrigen.setDniTitular(12345678);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setCBU(987654321);
        cuentaDestino.setBalance(500);
        cuentaDestino.setMoneda(TipoMoneda.ARS);
        cuentaDestino.setDniTitular(87654321);

        when(cuentaDao.obtenerCuentaPorCBU(cuentaOrigen.getDniTitular())).thenReturn(cuentaOrigen);
        when(cuentaDao.obtenerCuentaPorCBU(cuentaDestino.getDniTitular())).thenReturn(cuentaDestino);

        when(clienteDao.findByDni(cuentaOrigen.getDniTitular())).thenReturn(clienteOrigen);
        when(clienteDao.findByDni(cuentaDestino.getDniTitular())).thenReturn(clienteDestino);

        assertThrows(TipoMonedasInvalidasException.class, () -> transferenciaService.realizarTransferencia(getTransferenciaDto()));

        verify(cuentaDao,times(2)).obtenerCuentaPorCBU(any(Long.class));
        verify(clienteDao,times(2)).findByDni(any(Long.class));
    }

    @Test
    public void testObtenerOperacionesPorCBU(){
        Transferencia transferencia = getTransferencia();
        List<Transferencia> transferencias = new ArrayList<>();

        transferencias.add(transferencia);
        when(transferenciaDao.obtenerTransferenciasPorCbu(any(Long.class))).thenReturn(transferencias);

        List<Transferencia> transferenciasObtenidas = transferenciaService.obtenerTransferenciasPorCbu(123456789);

        verify(transferenciaDao, times(1)).obtenerTransferenciasPorCbu(any(Long.class));

        assertEquals(transferencias, transferenciasObtenidas);
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

    public Transferencia getTransferencia() {
        Transferencia transferencia = new Transferencia();
        transferencia.setTipoTransferencia(TipoTransferencia.DEBITO);
        transferencia.setCuentaOrigen(12345678);
        transferencia.setCuentaDestino(87654321);
        transferencia.setMonto(1000.0);
        transferencia.setMoneda(TipoMoneda.ARS);
        transferencia.setDescripcionBreve("Test de transferencia");
        return transferencia;
    }

}
