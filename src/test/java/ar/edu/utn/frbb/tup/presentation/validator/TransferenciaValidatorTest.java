package ar.edu.utn.frbb.tup.presentation.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import ar.edu.utn.frbb.tup.presentation.modelDto.TransferenciaDto;
import ar.edu.utn.frbb.tup.presentation.validator.TransferenciaValidator;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferenciaValidatorTest {
    
    TransferenciaValidator transferenciaValidator;

    @BeforeEach
    public void setUp() {
        transferenciaValidator = new TransferenciaValidator();
    }

    @Test
    public void testTransferenciaValidatorSuccess(){
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setTipoTransferencia("DEBITO");
        transferenciaDto.setCuentaOrigen("12345678");
        transferenciaDto.setCuentaDestino("87654321");
        transferenciaDto.setMonto(1000.0);
        transferenciaDto.setDescripcionBreve("Test de transferencia");
        transferenciaDto.setMoneda("ARS");

        assertDoesNotThrow(() -> transferenciaValidator.validarTransferencia(transferenciaDto));
    }

    @Test
    public void testTransferenciaValidatorErrorCuentaOrigenString(){
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setTipoTransferencia("DEBITO");
        transferenciaDto.setCuentaOrigen("A");
        transferenciaDto.setCuentaDestino("87654321");
        transferenciaDto.setMonto(1000.0);
        transferenciaDto.setDescripcionBreve("Test de transferencia");
        transferenciaDto.setMoneda("ARS");

        assertThrows(IllegalArgumentException.class, () -> transferenciaValidator.validarTransferencia(transferenciaDto));
    }

    @Test
    public void testTransferenciaValidatorErrorCuentaDestinoString(){
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setTipoTransferencia("DEBITO");
        transferenciaDto.setCuentaOrigen("12345678");
        transferenciaDto.setCuentaDestino("A");
        transferenciaDto.setMonto(1000.0);
        transferenciaDto.setDescripcionBreve("Test de transferencia");
        transferenciaDto.setMoneda("ARS");

        assertThrows(IllegalArgumentException.class, () -> transferenciaValidator.validarTransferencia(transferenciaDto));
    }

    @Test
    public void testTransferenciaValidatorMismoCBU(){
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setTipoTransferencia("DEBITO");
        transferenciaDto.setCuentaOrigen("12345678");
        transferenciaDto.setCuentaDestino("12345678");
        transferenciaDto.setMonto(10.0);
        transferenciaDto.setDescripcionBreve("Test de transferencia");
        transferenciaDto.setMoneda("ARS");

        assertThrows(IllegalArgumentException.class, () -> transferenciaValidator.validarTransferencia(transferenciaDto));
    }

    @Test
    public void testTransferenciaValidatorMontoMenorCero(){
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setTipoTransferencia("DEBITO");
        transferenciaDto.setCuentaOrigen("12345678");
        transferenciaDto.setCuentaDestino("87654321");
        transferenciaDto.setMonto(-10.0);
        transferenciaDto.setDescripcionBreve("Test de transferencia");
        transferenciaDto.setMoneda("ARS");

        assertThrows(IllegalArgumentException.class, () -> transferenciaValidator.validarTransferencia(transferenciaDto));
    }

    @Test
    public void testTransferenciaValidatorTipoTransferenciaNull(){
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setTipoTransferencia(null);
        transferenciaDto.setCuentaOrigen("12345678");
        transferenciaDto.setCuentaDestino("87654321");
        transferenciaDto.setMonto(1000.0);
        transferenciaDto.setDescripcionBreve("Test de transferencia");
        transferenciaDto.setMoneda("ARS");

        assertThrows(IllegalArgumentException.class, () -> transferenciaValidator.validarTransferencia(transferenciaDto));
    }

    @Test
    public void testTransferenciaValidatorMonedaNull(){
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setTipoTransferencia("DEBITO");
        transferenciaDto.setCuentaOrigen("12345678");
        transferenciaDto.setCuentaDestino("87654321");
        transferenciaDto.setMonto(1000.0);
        transferenciaDto.setDescripcionBreve("Test de transferencia");
        transferenciaDto.setMoneda(null);

        assertThrows(IllegalArgumentException.class, () -> transferenciaValidator.validarTransferencia(transferenciaDto));
    }

    @Test
    public void testTransferenciaValidatorCDescripcionNull(){
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setTipoTransferencia("DEBITO");
        transferenciaDto.setCuentaOrigen("12345678");
        transferenciaDto.setCuentaDestino("87654321");
        transferenciaDto.setMonto(1000.0);
        transferenciaDto.setDescripcionBreve(null);
        transferenciaDto.setMoneda("ARS");

        assertThrows(IllegalArgumentException.class, () -> transferenciaValidator.validarTransferencia(transferenciaDto));
    }
}


