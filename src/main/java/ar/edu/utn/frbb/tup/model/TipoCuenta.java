package ar.edu.utn.frbb.tup.model;
public enum TipoCuenta {

    CORRIENTE("CORRIENTE"),
    AHORRO("AHORRO");

    private final String descripcion;

    TipoCuenta(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static TipoCuenta fromString(String text) {
        if (text != null) {
            for (TipoCuenta tipo : TipoCuenta.values()) {
                if (tipo.name().equalsIgnoreCase(text) || tipo.getDescripcion().equalsIgnoreCase(text)) {
                    return tipo;
                }
            }
        }
        throw new IllegalArgumentException("El tipo de cuenta debe ser CORRIENTE o AHORRO");
    }
}
