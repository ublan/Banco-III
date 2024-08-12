package ar.edu.utn.frbb.tup.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import ar.edu.utn.frbb.tup.model.*;

@Repository
public class CuentaDao {
    private static final String CUENTASTXT = "src\\main\\java\\ar\\edu\\utn\\frbb\\tup\\persistence\\DataBase\\Cuentas.txt"; //cambiar aca 

    public Cuenta obtenerCuentaPorCBU(long cbu) {
        List<Cuenta> cuentas = leerCuentasDeArchivo();
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getCBU() == cbu) {
                return cuenta;
            }
        }
        return null;
    }

    public void actualizarBalanceCuenta(long cbu, double nuevoBalance) {
        List<Cuenta> cuentas = leerCuentasDeArchivo();
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(CUENTASTXT))) {
            escritor.write("CBU,nombre,tipoCuenta,balance,moneda,fechaCreacion,titularDni");
            escritor.newLine();
            
            for (Cuenta cuenta : cuentas) {
                if (cuenta.getCBU() == cbu) {
                    cuenta.setBalance(nuevoBalance);
                }
                escritor.write(cuenta.getCBU() + ",");
                escritor.write(cuenta.getNombre() + ",");
                escritor.write(cuenta.getTipoCuenta() + ",");
                escritor.write(cuenta.getBalance() + ",");
                escritor.write(cuenta.getMoneda() + ",");
                escritor.write(cuenta.getFechaCreacion().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ",");
                escritor.write(cuenta.getDniTitular() + "");
                escritor.newLine();
            }
        } catch (IOException ex) {
            System.err.println("Error al actualizar el balance en el archivo: " + ex.getMessage());
        }
    }
    
    public void escribirEnArchivo(Cuenta cuenta) {
        boolean archivoNuevo = !(new File(CUENTASTXT).exists());

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(CUENTASTXT, true))) {
        
            if (archivoNuevo) {
                escritor.write("CBU,nombre,tipoCuenta,balance,moneda,fechaCreacion,titularDni");
                escritor.newLine();
            }

            escritor.write(cuenta.getCBU() + ",");
            escritor.write(cuenta.getNombre() + ",");
            escritor.write(cuenta.getTipoCuenta() + ",");
            escritor.write(cuenta.getBalance() + ",");
            escritor.write(cuenta.getMoneda() + ",");
            escritor.write(cuenta.getFechaCreacion().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ",");
            escritor.write(cuenta.getDniTitular()+"");
            escritor.newLine();

            System.out.println("Datos de la cuenta guardados en " + CUENTASTXT + " correctamente.");
        } catch (IOException ex) {
            System.err.println("Error al escribir en el archivo: " + ex.getMessage());
        }
    }


    private List<Cuenta> leerCuentasDeArchivo() {
        List<Cuenta> cuentas = new ArrayList<>();
        try (BufferedReader lector = new BufferedReader(new FileReader(CUENTASTXT))) {
            String linea;
            lector.readLine(); // Leer la cabecera
    
            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length < 7) {
                    System.err.println("Línea mal formada: " + linea);
                    continue;
                }
                
                Cuenta cuenta = new Cuenta();
                cuenta.setCBU(Long.parseLong(datos[0]));
                cuenta.setNombre(datos[1]);
                cuenta.setTipoCuenta(TipoCuenta.valueOf(datos[2]));
                cuenta.setBalance(Double.parseDouble(datos[3])); 
                cuenta.setMoneda(TipoMoneda.valueOf(datos[4]));
                cuenta.setFechaCreacion(LocalDateTime.parse(datos[5], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                cuenta.setDniTitular(Long.parseLong(datos[6]));
    
                cuentas.add(cuenta);
            }
        } catch (IOException ex) {
            System.err.println("Error al leer el archivo de cuentas: " + ex.getMessage());
        }
        return cuentas;
    }
    
    public Cuenta parseCuentaToObjet(String[] datos){
        Cuenta cuenta = new Cuenta();
        cuenta.setCBU(Long.parseLong(datos[0]));
        cuenta.setNombre(datos[1]);
        cuenta.setTipoCuenta(TipoCuenta.valueOf(datos[2]));
        cuenta.setBalance(Double.parseDouble(datos[3])); 
        cuenta.setMoneda(TipoMoneda.valueOf(datos[4]));
        cuenta.setFechaCreacion(LocalDateTime.parse(datos[5], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        cuenta.setDniTitular(Long.parseLong(datos[6]));
        return cuenta;
    }

    public Cuenta borrarCuenta(long CBU) {
        List<Cuenta> cuentas = new ArrayList<>();
        List<String> cuentasStr = new ArrayList<>();
        Cuenta cuenta = null; 
        CuentaDao cuentaDao = new CuentaDao();
        try (BufferedReader lector = new BufferedReader(new FileReader(CUENTASTXT))) {
            String linea = lector.readLine();
            cuentasStr.add(linea);
            while ((linea = lector.readLine()) != null) {
                String[] campos = linea.split(",");
                if (Long.parseLong(campos[0]) != CBU) {
                    cuentas.add(cuentaDao.parseCuentaToObjet(campos));
                    cuentasStr.add(linea);
                } else {
                    cuenta = cuentaDao.parseCuentaToObjet(campos);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo acceder a la base de datos");
        }

        if (cuenta != null) {
            try (BufferedWriter escritor = new BufferedWriter(new FileWriter(CUENTASTXT))) {
                for (String cuentaStr : cuentasStr) {
                    escritor.write(cuentaStr);
                    escritor.newLine();
                }
                return cuenta;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("No se pudo escribir en el archivo");
            }
        } else {
            return cuenta;
        }
    }

    public List<Cuenta> obtonerCuentasDelCliente(long dni) {
        List<Cuenta> cuentasEncontradas = new ArrayList<>();
        CuentaDao cuentaDao = new CuentaDao();

        try (BufferedReader lector = new BufferedReader(new FileReader(CUENTASTXT))) {
            String linea = lector.readLine();
            while ((linea = lector.readLine()) != null) {
                String[] campos = linea.split(",");
                if (Long.parseLong(campos[6]) == dni) {
                    cuentasEncontradas.add(cuentaDao.parseCuentaToObjet(campos));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cuentasEncontradas;
    }

    public List<Cuenta> mostrarTodasLasCuentas() {
        List<Cuenta> cuentas = new ArrayList<>();
        CuentaDao cuentaDao = new CuentaDao();

        try (BufferedReader lector = new BufferedReader(new FileReader(CUENTASTXT))) {
            String linea = lector.readLine();
            while ((linea = lector.readLine()) != null) {
                String[] campos = linea.split(",");
                cuentas.add(cuentaDao.parseCuentaToObjet(campos));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cuentas;
    }

}
