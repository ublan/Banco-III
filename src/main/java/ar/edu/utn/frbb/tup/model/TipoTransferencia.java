package ar.edu.utn.frbb.tup.model;

public enum TipoTransferencia {

    DEBITO("DEBITO"),
    CREDITO("CREDITO");

    private final String descripcion;

    TipoTransferencia(String descripcion){
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static TipoTransferencia fromString(String text) throws IllegalArgumentException {
        for (TipoTransferencia tipo : TipoTransferencia.values()) {
            if (tipo.descripcion.equalsIgnoreCase(text)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("No se pudo encontrar un TipoTransferencia con la descripción: " + text);
    }
}
