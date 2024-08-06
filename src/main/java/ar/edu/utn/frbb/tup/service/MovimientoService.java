package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.exception.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.CuentaSinSaldoException;
import ar.edu.utn.frbb.tup.exception.MomivientosVaciosException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.MovimientosDao;
import ar.edu.utn.frbb.tup.model.TipoOperacion;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovimientoService {

    @Autowired
    private MovimientosDao movimientosDao;

    @Autowired
    private CuentaDao cuentaDao;

    public Movimiento realizarDeposito(long cbu, double monto) throws CuentaNoEncontradaException{

        Cuenta cuenta = cuentaDao.obtenerCuentaPorCBU(cbu);

        if (cuenta == null) {
            throw new CuentaNoEncontradaException("El CBU no existe");
        }
        
        double nuevoBalance = cuenta.getBalance() + monto;
        cuentaDao.actualizarBalanceCuenta(cbu, nuevoBalance);

        Movimiento movimiento = creaMovimiento(cbu, TipoOperacion.DEPOSITO, monto);

        movimientosDao.guardarMovimiento(movimiento);

        return movimiento;
    }

    public Movimiento realizarRetiro(long cbu, double monto) throws CuentaNoEncontradaException, CuentaSinSaldoException {
        Cuenta cuenta = cuentaDao.obtenerCuentaPorCBU(cbu);
        if (cuenta == null) {
            throw new CuentaNoEncontradaException("Cuenta no encontrada");
        }
        if (cuenta.getBalance() < monto) {
            throw new CuentaSinSaldoException("Saldo insuficiente");
        }

        Movimiento movimiento = creaMovimiento(cbu, TipoOperacion.RETIRO, monto);

        movimientosDao.guardarMovimiento(movimiento);

        double nuevoBalance = cuenta.getBalance() - monto;
        cuentaDao.actualizarBalanceCuenta(cbu, nuevoBalance);

        return movimiento;
    }

    public List<Movimiento> obtenerOperacionesPorCBU(long cbu) throws MomivientosVaciosException {

        List<Movimiento> movimientos =  movimientosDao.obtenerOperacionesPorCBU(cbu);
        if (movimientos.isEmpty()) {
            throw new MomivientosVaciosException("No se encontraron movimientos");  
        }
        return movimientos;
    }

    private static Movimiento creaMovimiento( long cbu, TipoOperacion tipoOperacion, double monto) {

        Movimiento movimiento = new Movimiento();
        movimiento.setCBU(cbu);
        movimiento.setFechaOperacion(LocalDate.now());
        movimiento.setTipoOperacion(tipoOperacion);
        movimiento.setMonto(monto);
        return movimiento;
    }

}

