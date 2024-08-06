package ar.edu.utn.frbb.tup.presentation.validator;

import org.springframework.stereotype.Component;
import ar.edu.utn.frbb.tup.presentation.modelDto.CuentaDto;

@Component
public class CuentaValidator {

    public void validarCuenta(CuentaDto cuentadto) {

        validarDNI(cuentadto.getDniTitular());

        if (Long.parseLong(cuentadto.getDniTitular()) == 0) {
            throw new IllegalArgumentException("El dni del titular de la cuenta es obligatorio");
        }

        if (Integer.parseInt(cuentadto.getDniTitular()) < 0) {
            throw new IllegalArgumentException("El dni del titular de la cuenta no puede ser negativo");
        }
    
        if (Long.parseLong(cuentadto.getDniTitular().toString()) < 10000000 || Long.parseLong(cuentadto.getDniTitular().toString()) > 99999999) {
            throw new IllegalArgumentException("El dni del titular de la cuenta debe ser de 8 digitos");
        }

        if (cuentadto.getNombre() == null || cuentadto.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la cuenta es obligatorio");
        }

        if (cuentadto.getTipoCuenta() == null || cuentadto.getTipoCuenta().isEmpty()) {
            throw new IllegalArgumentException("El tipo de cuenta es obligatorio");
        } 

        if (cuentadto.getMoneda() == null || cuentadto.getMoneda().isEmpty()) {
            throw new IllegalArgumentException("La moneda de la cuenta es obligatoria");
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
