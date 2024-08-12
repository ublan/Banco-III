package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.TipoOperacion;

import org.springframework.stereotype.Repository;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MovimientosDao {

    private static final String MOVIMIENTOSTXT = "src\\main\\java\\ar\\edu\\utn\\frbb\\tup\\persistence\\database\\Movimientos.txt";

    public void guardarMovimiento(Movimiento movimiento) {
        boolean archivoNuevo = !(new File(MOVIMIENTOSTXT).exists());

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(MOVIMIENTOSTXT, true))) {
            if (archivoNuevo) {
                escritor.write("CBU,fechaOperacion,tipoOperacion,monto");
                escritor.newLine();
            }

            escritor.write(movimiento.getCBU() + ",");
            escritor.write(movimiento.getFechaOperacion().toString() + ",");
            escritor.write(movimiento.getTipoOperacion().name() + ",");
            escritor.write(Double.toString(movimiento.getMonto()));
            escritor.newLine();

            System.out.println("Datos del movimiento guardados correctamente.");
        } catch (IOException ex) {
            System.err.println("Error al escribir en el archivo: " + ex.getMessage());
        }
    }

    public List<Movimiento> obtenerOperacionesPorCBU(long cbu) {
        List<Movimiento> movimientos = leerMovimientosDeArchivo(MOVIMIENTOSTXT);
        List<Movimiento> resultado = new ArrayList<>();

        for (Movimiento movimiento : movimientos) {
            if (movimiento.getCBU() == cbu) {
                resultado.add(movimiento);
            }
        }
        return resultado;
    }

    private List<Movimiento> leerMovimientosDeArchivo(String rutaArchivo) {
        List<Movimiento> movimientos = new ArrayList<>();

        try (BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            lector.readLine();

            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(",");

                if (datos.length < 4) {
                    System.err.println("Línea mal formada: " + linea);
                    continue;
                }

                long cbu = Long.parseLong(datos[0]);
                LocalDate fechaOperacion = LocalDate.parse(datos[1]);
                TipoOperacion tipoOperacion = TipoOperacion.valueOf(datos[2]);
                double monto = Double.parseDouble(datos[3]);

                Movimiento movimiento = new Movimiento();
                movimiento.setCBU(cbu);
                movimiento.setFechaOperacion(fechaOperacion);
                movimiento.setTipoOperacion(tipoOperacion);
                movimiento.setMonto(monto);

                movimientos.add(movimiento);
            }
        } catch (IOException ex) {
            System.err.println("Error al leer el archivo: " + ex.getMessage());
        }
        return movimientos;
    }

    public void borrarMovimiento(long CBU) {
        List<String> movimientosStr = new ArrayList<>();

        try (BufferedReader lector = new BufferedReader(new FileReader(MOVIMIENTOSTXT))) {
            String linea = lector.readLine();
            movimientosStr.add(linea);
            while ((linea = lector.readLine()) != null) {
                String[] campos = linea.split(",");
                if (Long.parseLong(campos[0]) != CBU) {
                    movimientosStr.add(linea);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo acceder a la base de datos");
        }

        if (!movimientosStr.isEmpty()) {
            try (BufferedWriter escritor = new BufferedWriter(new FileWriter(MOVIMIENTOSTXT))) {
                for (String movimientoStr : movimientosStr) {
                    escritor.write(movimientoStr);
                    escritor.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("No se pudo escribir en el archivo");
            }
        } 
    }


}




