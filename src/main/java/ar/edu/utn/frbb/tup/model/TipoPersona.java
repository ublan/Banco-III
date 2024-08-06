package ar.edu.utn.frbb.tup.model;
public enum TipoPersona {

    PERSONA_FISICA("F"),
    PERSONA_JURIDICA("J");

    private final String codigo;

    TipoPersona(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public static TipoPersona fromString(String text) {
        if (text != null) {
            for (TipoPersona tipo : TipoPersona.values()) {
                if (tipo.name().equalsIgnoreCase(text) || tipo.codigo.equalsIgnoreCase(text)) {
                    return tipo;
                }
            }
        }
        throw new IllegalArgumentException("No se pudo encontrar un TipoPersona con la descripción: " + text);
    }
}
