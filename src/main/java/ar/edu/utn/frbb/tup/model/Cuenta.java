package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.presentation.modelDto.CuentaDto;

import java.time.LocalDateTime;
import java.util.Random;

public class Cuenta {
    private long dniTitular;
    private String nombre;
    private LocalDateTime fechaCreacion;
    private double balance;
    private TipoCuenta tipoCuenta;
    private long CBU; 
    private TipoMoneda moneda;

    public Cuenta() {
        // Constructor vacío
    }

    public Cuenta(CuentaDto cuentaDto) {
        this.dniTitular = Long.parseLong(cuentaDto.getDniTitular());
        this.nombre = cuentaDto.getNombre();
        this.balance = 0;
        this.tipoCuenta = TipoCuenta.fromString(cuentaDto.getTipoCuenta()); 
        this.CBU = Cuenta.generarCBU();
        this.moneda = TipoMoneda.fromString(cuentaDto.getMoneda());
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters y setters

    public long getDniTitular() {
        return dniTitular;
    }

    public Cuenta setDniTitular(long dniTitular) {
        this.dniTitular = dniTitular;
        return this;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public Cuenta setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
        return this;
    }

    public String getNombre() {
        return nombre;
    }

    public Cuenta setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public Cuenta setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        return this;
    }

    public double getBalance() {
        return balance;
    }

    public Cuenta setBalance(double balance) {
        this.balance = balance;
        return this;
    }

    public long getCBU() {
        return CBU;
    }

    public void setCBU(long CBU) {
        this.CBU = CBU;
    }

    public static long generarCBU() {
        Random random = new Random();
        StringBuilder cbuBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            cbuBuilder.append(random.nextInt(10)); 
        }
        return Long.parseLong(cbuBuilder.toString());
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public Cuenta setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
        return this;
    }


}

