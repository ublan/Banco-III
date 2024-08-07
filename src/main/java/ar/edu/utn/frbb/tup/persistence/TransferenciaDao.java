package ar.edu.utn.frbb.tup.persistence;

import org.springframework.stereotype.Repository;

import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.TipoTransferencia;
import ar.edu.utn.frbb.tup.model.Transferencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransferenciaDao {

    private static final String TRANSFERENCIASTXT = "src\\main\\java\\ar\\edu\\utn\\frbb\\tup\\persistence\\database\\Transferencias.txt";

    public List<Transferencia> obtenerTransferenciasPorCbu(long cbu) {
        List<Transferencia> transacciones = new ArrayList<>();
        try (BufferedReader lector = new BufferedReader(new FileReader(TRANSFERENCIASTXT))) {
            String linea;
            lector.readLine();
            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(","); 

                if (Long.parseLong(datos[0]) == cbu || Long.parseLong(datos[1]) == cbu) {
                    Transferencia transaccion = parseTrnsferenciaToObjet(datos);                   
                    transacciones.add(transaccion);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return transacciones;
    }

    public void guardarTransferencia(Transferencia transferencia) {
        boolean archivoNuevo = !(new File(TRANSFERENCIASTXT).exists());
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(TRANSFERENCIASTXT, true))) {
            if (archivoNuevo) {
                escritor.write("cuentaOrigen,cuentaDestino,fecha,monto,moneda,tipoTransferencia,descripcionBreve");
                escritor.newLine();
            }
            String transf = transferencia.getCuentaOrigen() + "," +
                            transferencia.getCuentaDestino() + "," +
                            transferencia.getFecha() + "," +
                            transferencia.getMonto() + "," +
                            transferencia.getMoneda() + "," +
                            transferencia.getTipoTransferencia() + "," +
                            transferencia.getDescripcionBreve();

            escritor.write(transf);
            escritor.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void borrarTransferencia(long CBU) {
        List<String> transferenciasStr = new ArrayList<>();

        try (BufferedReader lector = new BufferedReader(new FileReader(TRANSFERENCIASTXT))) {
            String linea = lector.readLine();
            transferenciasStr.add(linea);
            while ((linea = lector.readLine()) != null) {
                String[] campos = linea.split(",");
                if (Long.parseLong(campos[0]) != CBU) {
                    transferenciasStr.add(linea);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo acceder a la base de datos");
        }

        if (!transferenciasStr.isEmpty()) {
            try (BufferedWriter escritor = new BufferedWriter(new FileWriter(TRANSFERENCIASTXT))) {
                for (String transferenciaStr : transferenciasStr) {
                    escritor.write(transferenciaStr);
                    escritor.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("No se pudo escribir en el archivo");
            }
        } 
    }

    public Transferencia parseTrnsferenciaToObjet(String[] datos){
        Transferencia Transferencia = new Transferencia();
        Transferencia.setCuentaOrigen(Long.parseLong(datos[0]));
        Transferencia.setCuentaDestino(Long.parseLong(datos[1]));
        Transferencia.setFecha(LocalDate.parse(datos[2]));
        Transferencia.setMonto(Double.parseDouble(datos[3]));
        Transferencia.setMoneda(TipoMoneda.valueOf(datos[4]));
        Transferencia.setTipoTransferencia(TipoTransferencia.valueOf(datos[5]));
        Transferencia.setDescripcionBreve(datos[6]);

        return Transferencia;
    }
}


