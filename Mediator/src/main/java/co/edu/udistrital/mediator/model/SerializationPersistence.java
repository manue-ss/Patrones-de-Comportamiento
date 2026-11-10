// Archivo: SerializationPersistence.java | Paquete: co.edu.udistrital.mediator.model
package co.edu.udistrital.mediator.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación concreta del servicio de persistencia utilizando serialización nativa de objetos en Java.
 */
public class SerializationPersistence implements ITurnPersistence {

    private final String filePath;

    /**
     * Inicializa el gestor de serialización validando la ruta del archivo destino.
     *
     * @param filePath Ruta del archivo binario (.ser / .dat).
     */
    public SerializationPersistence(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("La ruta del archivo de persistencia no puede estar vacía.");
        }
        this.filePath = filePath.trim();
    }

    @Override
    public synchronized boolean saveAssignedTurns(List<TurnDTO> turns) {
        if (turns == null) {
            throw new IllegalArgumentException("La lista de turnos a serializar no puede ser nula.");
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(new ArrayList<>(turns));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized List<TurnDTO> loadAssignedTurns() {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object readObject = ois.readObject();
            if (readObject instanceof List<?>) {
                return (List<TurnDTO>) readObject;
            }
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public String getFilePath() {
        return filePath;
    }
}