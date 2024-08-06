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
import ar.edu.utn.frbb.tup.exception.MomivientosVaciosException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.TipoOperacion;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.MovimientosDao;
import ar.edu.utn.frbb.tup.presentation.modelDto.CuentaDto;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MovimientoServiceTest {

    @Mock
    MovimientosDao movimientosDao;

    @Mock
    CuentaDao cuentaDao;

    @InjectMocks
    MovimientoService movimientoService;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testRealizarDepositoSuccess() throws CuentaNoEncontradaException {
        CuentaDto cuentaDto = getCuentaDto();
        Cuenta cuenta = new Cuenta(cuentaDto);

        when(cuentaDao.obtenerCuentaPorCBU(cuenta.getCBU())).thenReturn(cuenta);

        Movimiento movimiento = movimientoService.realizarDeposito(cuenta.getCBU(), 10);

        verify(cuentaDao, times(1)).obtenerCuentaPorCBU(cuenta.getCBU());
        verify(cuentaDao,times(1)).actualizarBalanceCuenta(cuenta.getCBU(), 10);
        verify(movimientosDao, times(1)).guardarMovimiento(any(Movimiento.class));
        

        assertNotNull(movimiento);
    }


    @Test
    public void testRealizarDepositoFail() throws CuentaNoEncontradaException{

        CuentaDto cuentaDto = getCuentaDto();
        Cuenta cuenta = new Cuenta(cuentaDto);

        when(cuentaDao.obtenerCuentaPorCBU(cuenta.getCBU())).thenReturn(null);

        assertThrows(CuentaNoEncontradaException.class, () -> movimientoService.realizarDeposito(cuenta.getCBU(), 10));
        
        verify(cuentaDao, times(1)).obtenerCuentaPorCBU(cuenta.getCBU());
        verify(cuentaDao,times(0)).actualizarBalanceCuenta(cuenta.getCBU(), 10);
        verify(movimientosDao, times(0)).guardarMovimiento(any(Movimiento.class));
    }

    @Test
    public void testRealizarRetiroSuccess() throws CuentaNoEncontradaException, CuentaSinSaldoException{
        CuentaDto cuentaDto = getCuentaDto();
        Cuenta cuenta = new Cuenta(cuentaDto);

        cuenta.setBalance(100);

        when(cuentaDao.obtenerCuentaPorCBU(cuenta.getCBU())).thenReturn(cuenta);

        Movimiento movimiento = movimientoService.realizarRetiro(cuenta.getCBU(), 10);

        verify(cuentaDao, times(1)).obtenerCuentaPorCBU(cuenta.getCBU());
        verify(cuentaDao,times(1)).actualizarBalanceCuenta(cuenta.getCBU(), 90);
        verify(movimientosDao, times(1)).guardarMovimiento(any(Movimiento.class));

        assertNotNull(movimiento);
    }

    @Test
    public void testRealizarRetiroCuantaNoEncontrada() throws CuentaNoEncontradaException, CuentaSinSaldoException{
        CuentaDto cuentaDto = getCuentaDto();
        Cuenta cuenta = new Cuenta(cuentaDto);

        when(cuentaDao.obtenerCuentaPorCBU(cuenta.getCBU())).thenReturn(null);

        assertThrows(CuentaNoEncontradaException.class, () -> movimientoService.realizarRetiro(cuenta.getCBU(), 10));
        
        verify(cuentaDao, times(1)).obtenerCuentaPorCBU(cuenta.getCBU());
        verify(cuentaDao,times(0)).actualizarBalanceCuenta(cuenta.getCBU(), 10);
        verify(movimientosDao, times(0)).guardarMovimiento(any(Movimiento.class));

    }

    @Test
    public void testRealizarRetiroCuentaSinSaldo() throws CuentaNoEncontradaException, CuentaSinSaldoException{
        CuentaDto cuentaDto = getCuentaDto();
        Cuenta cuenta = new Cuenta(cuentaDto);

        cuenta.setBalance(100);

        when(cuentaDao.obtenerCuentaPorCBU(cuenta.getCBU())).thenReturn(cuenta);

        assertThrows(CuentaSinSaldoException.class, () -> movimientoService.realizarRetiro(cuenta.getCBU(), 110));
        
        verify(cuentaDao, times(1)).obtenerCuentaPorCBU(cuenta.getCBU());
        verify(cuentaDao,times(0)).actualizarBalanceCuenta(cuenta.getCBU(), 10);
        verify(movimientosDao, times(0)).guardarMovimiento(any(Movimiento.class));

    }

    @Test
    public void testObtenerOperacionesPorCBU() throws CuentaNoEncontradaException, MomivientosVaciosException {
        Movimiento movimiento = getMovimiento();
        List<Movimiento> operaciones = new ArrayList<>();

        operaciones.add(movimiento);

        when(movimientosDao.obtenerOperacionesPorCBU(any(Long.class))).thenReturn(operaciones);
        
        List<Movimiento> operacionesObtenidas = movimientoService.obtenerOperacionesPorCBU(any(Long.class));

        verify(movimientosDao, times(1)).obtenerOperacionesPorCBU(any(Long.class));

        assertEquals(operaciones, operacionesObtenidas);
    }

    @Test
    public void testObtenerOperacionesPorCBUEmpty() throws CuentaNoEncontradaException, MomivientosVaciosException {
        List<Movimiento> operaciones = new ArrayList<>();

        when(movimientosDao.obtenerOperacionesPorCBU(any(Long.class))).thenReturn(operaciones);
        
        assertThrows(MomivientosVaciosException.class, () -> movimientoService.obtenerOperacionesPorCBU(any(Long.class)));

        verify(movimientosDao, times(1)).obtenerOperacionesPorCBU(any(Long.class));

    }

    public Movimiento getMovimiento() {
        Movimiento movimiento = new Movimiento();
        movimiento.setCBU(12345678);
        movimiento.setFechaOperacion(LocalDate.now());
        movimiento.setTipoOperacion(TipoOperacion.DEPOSITO);
        movimiento.setMonto(10);
        return movimiento;
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
