package ar.edu.utn.frbb.tup.presentation.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import ar.edu.utn.frbb.tup.presentation.modelDto.ClienteDto;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteValidatorTest {

    ClienteValidator clienteValidator;

    @BeforeEach
    public void setUp() {
        clienteValidator = new ClienteValidator();
    }

    @Test
    public void testClienteValidatorSuccess(){
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni("12345678");
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Peperino");
        clienteDto.setDireccion("Calle Falsa 123");
        clienteDto.setFechaNacimiento("2001-01-01");
        clienteDto.setBanco("Banco Nacion");
        clienteDto.setTipoPersona("PERSONA_FISICA");

        assertDoesNotThrow(() -> clienteValidator.validarCliente(clienteDto));
    }

    @Test
    public void testClienteValidatorErrorDniString(){
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni("A");
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Peperino");
        clienteDto.setDireccion("Calle Falsa 123");
        clienteDto.setFechaNacimiento("2001-01-01");
        clienteDto.setBanco("Banco Nacion");
        clienteDto.setTipoPersona("PERSONA_FISICA");
    
        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validarCliente(clienteDto));
    }

    @Test
    public void testClienteValidatorSinDni(){
        ClienteDto clienteDto = new ClienteDto();
        //clienteDto.setDni("12345678");
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Peperino");
        clienteDto.setDireccion("Calle Falsa 123");
        clienteDto.setFechaNacimiento("2001-01-01");
        clienteDto.setBanco("Banco Nacion");
        clienteDto.setTipoPersona("PERSONA_FISICA");

        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validarCliente(clienteDto));
    }

    @Test
    public void testClienteValidatorDniMenoraCero(){
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni("-12345678");
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Peperino");
        clienteDto.setDireccion("Calle Falsa 123");
        clienteDto.setFechaNacimiento("2001-01-01");
        clienteDto.setBanco("Banco Nacion");
        clienteDto.setTipoPersona("PERSONA_FISICA");
    
        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validarCliente(clienteDto));
    }

    @Test
    public void testClienteValidatorSinNombre(){
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni("12345678");
        clienteDto.setNombre("");
        clienteDto.setApellido("Peperino");
        clienteDto.setDireccion("Calle Falsa 123");
        clienteDto.setFechaNacimiento("2001-01-01");
        clienteDto.setBanco("Banco Nacion");
        clienteDto.setTipoPersona("PERSONA_FISICA");

        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validarCliente(clienteDto));
    }

    @Test
    public void testClienteValidatorSinApellido(){
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni("12345678");
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("");
        clienteDto.setDireccion("Calle Falsa 123");
        clienteDto.setFechaNacimiento("2001-01-01");
        clienteDto.setBanco("Banco Nacion");
        clienteDto.setTipoPersona("PERSONA_FISICA");

        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validarCliente(clienteDto));
    }

    @Test
    public void testClienteValidatorSinDireccion(){
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni("12345678");
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Peperino");
        clienteDto.setDireccion("");
        clienteDto.setFechaNacimiento("2001-01-01");
        clienteDto.setBanco("Banco Nacion");
        clienteDto.setTipoPersona("PERSONA_FISICA");

        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validarCliente(clienteDto));
    }

    @Test
    public void testClienteValidatorSinFechaNacimiento(){
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni("12345678");
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Peperino");
        clienteDto.setDireccion("Calle Falsa 123");
        //clienteDto.setFechaNacimiento("");
        clienteDto.setBanco("Banco Nacion");
        clienteDto.setTipoPersona("PERSONA_FISICA");

        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validarCliente(clienteDto));
    }

    @Test
    public void testClienteValidatorSinBanco(){
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni("12345678");
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Peperino");
        clienteDto.setDireccion("Calle Falsa 123");
        clienteDto.setFechaNacimiento("2001-01-01");
        clienteDto.setBanco("");
        clienteDto.setTipoPersona("PERSONA_FISICA");

        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validarCliente(clienteDto));
    }

    @Test
    public void testClienteValidatorSinTipoPersona(){
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni("12345678");
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Peperino");
        clienteDto.setDireccion("Calle Falsa 123");
        clienteDto.setFechaNacimiento("2001-01-01");
        clienteDto.setBanco("Banco Nacion");
        //clienteDto.setTipoPersona("PERSONA_FISICA");

        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validarCliente(clienteDto));
    }
}
