package ar.edu.utn.frbb.tup.presentation.validator;

import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.presentation.modelDto.TransferenciaDto;

@Component
public class TransferenciaValidator {

    public void validarTransferencia(TransferenciaDto transferenciaDto) {

        validarCBUs(transferenciaDto.getCuentaOrigen());
        validarCBUs(transferenciaDto.getCuentaDestino());

        if (transferenciaDto.getCuentaOrigen() == transferenciaDto.getCuentaDestino()) {
            throw new IllegalArgumentException("El CBU destino no puede ser el mismo que el CBU origen");
        }

        if (Long.parseLong(transferenciaDto.getCuentaDestino()) == 0) {
            throw new IllegalArgumentException("El CBU destino no puede ser nulo");
        }

        if (Long.parseLong(transferenciaDto.getCuentaOrigen()) == 0) {
            throw new IllegalArgumentException("El CBU Origen no puede estar vacio");
        }

        if (transferenciaDto.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }

        if (transferenciaDto.getTipoTransferencia() == null) {
            throw new IllegalArgumentException("El tipo de transferencia no puede estar vacio");
        }

        if (transferenciaDto.getMoneda() == null) {
            throw new IllegalArgumentException("La moneda no puede ser estada vacia");
        }

        if (transferenciaDto.getDescripcionBreve() == null) {
            throw new IllegalArgumentException("La descripcion no puede estar vacia");
        }
    }

    private void validarCBUs(String dni) {
        try {
            Long.parseLong(dni);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El dni no tiene el formato correcto");
        }
    }
}





