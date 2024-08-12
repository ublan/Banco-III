package ar.edu.utn.frbb.tup.persistence;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frbb.tup.exception.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import org.springframework.stereotype.Repository;

@Repository
public class ClienteDao {

    private static final String CLIENTESTXT = "src\\main\\java\\ar\\edu\\utn\\frbb\\tup\\persistence\\database\\Clientes.txt";
    
    
    public void crearCliente(Cliente cliente) {
        boolean archivoNuevo = !(new File(CLIENTESTXT).exists());
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(CLIENTESTXT, true))) {
            if (archivoNuevo) {
                escritor.write("dni,nombre,apellido,direccion,fechaNacimiento,tipoPersona,banco,fechaAlta");
                escritor.newLine();
            }
            escritor.write(clienteToCsv(cliente));
            escritor.newLine();
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String clienteToCsv(Cliente cliente) {
        return  cliente.getDni() + "," +
                cliente.getNombre() + "," +
                cliente.getApellido() + "," +
                cliente.getDireccion() + "," +
                cliente.getFechaNacimiento() + "," +
                cliente.getTipoPersona() + "," +
                cliente.getBanco() + "," +
                cliente.getFechaAlta();
    }

    public Cliente findByDni(long dni) {
        try (BufferedReader lector = new BufferedReader(new FileReader(CLIENTESTXT))) {
            String linea;
            lector.readLine();
            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(",");
                if (Long.parseLong(datos[0]) == dni) {
                    Cliente cliente = new Cliente();
                    cliente.setDni(Long.parseLong(datos[0]));
                    cliente.setNombre(datos[1]);
                    cliente.setApellido(datos[2]);
                    cliente.setDireccion(datos[3]);
                    cliente.setFechaNacimiento(LocalDate.parse(datos[4]));
                    cliente.setTipoPersona(TipoPersona.valueOf(datos[5]));
                    cliente.setBanco(datos[6]);
                    cliente.setFechaAlta(datos[7].equals("null") ? null : LocalDate.parse(datos[7]));
                    return cliente;
                }
            }
        } catch (IOException ex) {
            System.err.println("Error al leer el archivo: " + ex.getMessage());
        }
        return null;
    }
    
    
    public Cliente parseDatosToObjet(String[] datos){
        Cliente cliente = new Cliente();

        cliente.setDni(Long.parseLong(datos[0]));
        cliente.setNombre(datos[1]);
        cliente.setApellido(datos[2]);
        cliente.setDireccion(datos[3]);
        cliente.setFechaNacimiento(LocalDate.parse(datos[4]));
        cliente.setTipoPersona(TipoPersona.valueOf(datos[5]));
        cliente.setBanco(datos[6]);
        cliente.setFechaAlta(LocalDate.parse(datos[7]));

        return cliente;
    }

    public Cliente borrarCliente(long dni) {
        List<Cliente> clientes = new ArrayList<>();
        List<String> clientesStr = new ArrayList<>();
        Cliente cliente = null;
        try (BufferedReader lector = new BufferedReader(new FileReader(CLIENTESTXT))) {
            String linea;
            linea = lector.readLine();
            clientesStr.add(linea);
            while ((linea = lector.readLine()) != null) {
                String[] campos = linea.split(",");
                if (Long.parseLong(campos[0]) != dni) {
                    clientes.add(parseDatosToObjet(campos));
                    clientesStr.add(linea);
                } else {
                    cliente = parseDatosToObjet(campos);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo acceder a la base de datos");
        }

        if (cliente != null) {
            try (BufferedWriter escritor = new BufferedWriter(new FileWriter(CLIENTESTXT))) {
                for (String clienteStr : clientesStr) {
                    escritor.write(clienteStr);
                    escritor.newLine();
                }
                return cliente;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("No se pudo escribir en el archivo");
            }
        } else {
            return cliente;
        }
    }

    public void modificarCliente(Cliente cliente) throws ClienteNoEncontradoException {
        List<String> nuevosDatos = new ArrayList<>();
        boolean clienteEncontrado = false;

        try (BufferedReader lector = new BufferedReader(new FileReader(CLIENTESTXT))) {
            String linea = lector.readLine(); 
            nuevosDatos.add(linea);         
            while ((linea = lector.readLine()) != null) {               
                String[] campos = linea.split(",");
                if (Long.parseLong(campos[0]) == cliente.getDni()) {
                    clienteEncontrado = true;
                    campos[1] = cliente.getNombre();
                    campos[2] = cliente.getApellido();
                    campos[3] = cliente.getDireccion();
                    campos[4] = cliente.getFechaNacimiento().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    campos[5] = cliente.getTipoPersona().toString();
                    campos[6] = cliente.getBanco();
                    campos[7] = cliente.getFechaAlta().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    nuevosDatos.add(String.join(",", campos));
                } else {
                    nuevosDatos.add(linea);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!clienteEncontrado) {
            throw new ClienteNoEncontradoException("Cliente no encontrado con DNI: " + cliente.getDni());
        }

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(CLIENTESTXT))) {
            for (String datos : nuevosDatos) {
                escritor.write(datos);
                escritor.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Cliente mostrarCliente(long dni) {
        try (BufferedReader lector = new BufferedReader(new FileReader(CLIENTESTXT))) {
            String linea;
            linea = lector.readLine();
            while ((linea = lector.readLine()) != null) {
                String[] campos = linea.split(",");
                if (Long.parseLong(campos[0]) == dni) {
                    Cliente cliente = new Cliente();
                    cliente.setDni(Long.parseLong(campos[0]));
                    cliente.setNombre(campos[1]);
                    cliente.setApellido(campos[2]);
                    cliente.setDireccion(campos[3]);
                    cliente.setFechaNacimiento(LocalDate.parse(campos[4]));
                    cliente.setTipoPersona(TipoPersona.fromString(campos[5]));
                    cliente.setBanco(campos[6]);
                    cliente.setFechaAlta(LocalDate.parse(campos[7].replace(".", "")));
                    return cliente;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Cliente> mostrarTodosLosClientes() {
        List<Cliente> clientes = new ArrayList<>();

        try (BufferedReader lector = new BufferedReader(new FileReader(CLIENTESTXT))) {
            String linea;
            linea = lector.readLine();
            while ((linea = lector.readLine()) != null) {
                
                String[] datos = linea.split(",");

                try {
                    Cliente cliente = new Cliente();
                    cliente.setDni(Long.parseLong(datos[0]));
                    cliente.setNombre(datos[1]);
                    cliente.setApellido(datos[2]);
                    cliente.setDireccion(datos[3]);
                    cliente.setFechaNacimiento(LocalDate.parse(datos[4]));
                    cliente.setTipoPersona(TipoPersona.fromString(datos[5]));
                    cliente.setBanco(datos[6]);
                    cliente.setFechaAlta(LocalDate.parse(datos[7].replace(".", "")));

                    clientes.add(cliente);
                } catch (DateTimeParseException e) {
                    System.err.println("Error al parsear la fecha en la línea: " + linea);
                }
            }
        } catch (IOException ex) {
            System.err.println("Error al leer el archivo: " + ex.getMessage());
        }

        return clientes;
    }

}
    
    


