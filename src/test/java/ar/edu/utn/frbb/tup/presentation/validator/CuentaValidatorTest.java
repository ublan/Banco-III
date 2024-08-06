package ar.edu.utn.frbb.tup.presentation.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import ar.edu.utn.frbb.tup.presentation.modelDto.CuentaDto;
import ar.edu.utn.frbb.tup.presentation.validator.CuentaValidator;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaValidatorTest {

    CuentaValidator cuentaValidator;

    @BeforeEach
    public void setUp() {
        cuentaValidator = new CuentaValidator();
    }

    @Test
    public void testCuentaValidatorSuccess(){
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setNombre("Uriel");
        cuentaDto.setDniTitular("12345678");
        cuentaDto.setTipoCuenta("AHORRO");
        cuentaDto.setMoneda("USD");

        assertDoesNotThrow(() -> cuentaValidator.validarCuenta(cuentaDto));
    }

    @Test
    public void testCuentaValidatorErrorDniString(){
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setNombre("Uriel");
        cuentaDto.setDniTitular("A");
        cuentaDto.setTipoCuenta("AHORRO");
        cuentaDto.setMoneda("USD");

        assertThrows(IllegalArgumentException.class, () -> cuentaValidator.validarCuenta(cuentaDto));
    }

    @Test
    public void testCuentaValidatorDnienCero(){
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setDniTitular("0");
        cuentaDto.setNombre("Uriel");
        cuentaDto.setTipoCuenta("AHORRO");
        cuentaDto.setMoneda("USD");

        assertThrows(IllegalArgumentException.class, () -> cuentaValidator.validarCuenta(cuentaDto));
    }

    @Test
    public void testCuentaValidatorSinDni(){
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setDniTitular("");
        cuentaDto.setNombre("Uriel");
        cuentaDto.setTipoCuenta("AHORRO");
        cuentaDto.setMoneda("USD");

        assertThrows(IllegalArgumentException.class, () -> cuentaValidator.validarCuenta(cuentaDto));
    }

    @Test
    public void testCuentaValidatorSinNombre(){
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setDniTitular("12345678");
        cuentaDto.setNombre("");
        cuentaDto.setTipoCuenta("AHORRO");
        cuentaDto.setMoneda("USD");

        assertThrows(IllegalArgumentException.class, () -> cuentaValidator.validarCuenta(cuentaDto));
    }

    @Test
    public void testCuentaValidatorSinTipoCuenta(){
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setDniTitular("12345678");
        cuentaDto.setNombre("Uriel");
        cuentaDto.setTipoCuenta("");
        cuentaDto.setMoneda("USD");

        assertThrows(IllegalArgumentException.class, () -> cuentaValidator.validarCuenta(cuentaDto));
    }

    @Test
    public void testCuentaValidatorSinMoneda(){
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setDniTitular("12345678");
        cuentaDto.setNombre("Uriel");
        cuentaDto.setTipoCuenta("AHORRO");
        cuentaDto.setMoneda("");

        assertThrows(IllegalArgumentException.class, () -> cuentaValidator.validarCuenta(cuentaDto));
    }
}
