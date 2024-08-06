package ar.edu.utn.frbb.tup.presentation.modelDto;

public class CuentaDto {
    private String nombre;
    private String dniTitular;
    private String tipoCuenta;
    private String tipoMoneda;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDniTitular() {
        return dniTitular;
    }

    public void setDniTitular(String dniTitular) {
        this.dniTitular = dniTitular;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public CuentaDto setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
        return this;
    }

    public String getMoneda() {
        return tipoMoneda;
    }

    public CuentaDto setMoneda(String moneda) {
        this.tipoMoneda = moneda;
        return this;
    }

    public void setTipoMoneda(String tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }
}
