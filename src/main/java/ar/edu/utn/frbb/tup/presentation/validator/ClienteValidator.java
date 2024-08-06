package ar.edu.utn.frbb.tup.presentation.validator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Component;
import ar.edu.utn.frbb.tup.presentation.modelDto.ClienteDto;

@Component
public class ClienteValidator {

    public void validarCliente(ClienteDto clientedto) {

        validarDNI(clientedto.getDni());

        if (Long.parseLong(clientedto.getDni()) == 0) {
            throw new IllegalArgumentException("El dni del titular de la cuenta es obligatorio");
        }

        if (Integer.parseInt(clientedto.getDni()) <= 0) {
            throw new IllegalArgumentException("El dni del titular de la cuenta no puede ser negativo");
        }
    
        if (Long.parseLong(clientedto.getDni().toString()) < 10000000 || Long.parseLong(clientedto.getDni().toString()) > 99999999) {
            throw new IllegalArgumentException("El dni del titular de la cuenta debe ser de 8 digitos");
        }

        if (clientedto.getNombre().isEmpty() || clientedto.getNombre() == null) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio");
        }
    
        if (clientedto.getApellido().isEmpty() || clientedto.getApellido() == null) {
            throw new IllegalArgumentException("El apellido del cliente es obligatorio");
        }

        if (clientedto.getDireccion().isEmpty() || clientedto.getDireccion() == null) {
            throw new IllegalArgumentException("La dirección del cliente es obligatoria");    
        }

        if (clientedto.getBanco().isEmpty() || clientedto.getBanco() == null) {
            throw new IllegalArgumentException("El banco del cliente es obligatorio");     
        }

        if (clientedto.getFechaNacimiento() == null) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula");
        }

        validarFechaNacimiento(clientedto.getFechaNacimiento());

        if (clientedto.getTipoPersona() == null) {
            throw new IllegalArgumentException("El tipo de persona no puede ser nulo");
            
        }

    }

    private void validarFechaNacimiento(String fechaNacimiento) {
        try {
            LocalDate.parse(fechaNacimiento);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("La fecha de nacimiento no tiene el formato correcto");
        }
    }

    private void validarDNI(String dni) {
        try {
            Long.parseLong(dni);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El dni no tiene el formato correcto");
        }
    }

}
