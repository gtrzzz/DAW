import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RepositorioAtletas {

    private final Path archivoDatos;
    private final List<Atleta> atletas;

    public RepositorioAtletas(Path archivoDatos) {
        this.archivoDatos = archivoDatos;
        this.atletas = cargarAtletas();
    }

    public synchronized List<Atleta> listarAtletas() {
        return Collections.unmodifiableList(atletas);
    }

    public synchronized Atleta buscarAtleta(int id) {
        for (Atleta atleta : atletas) {
            if (atleta.getId() == id) {
                return atleta;
            }
        }
        return null;
    }

    public synchronized void guardarAtleta(Atleta atleta) {
        atletas.add(atleta);
        persistirAtletas();
    }

    public synchronized void actualizarAtletas() {
        persistirAtletas();
    }

    public synchronized int obtenerSiguienteId() {
        int maximo = 0;
        for (Atleta atleta : atletas) {
            maximo = Math.max(maximo, atleta.getId());
        }
        return maximo + 1;
    }

    @SuppressWarnings("unchecked")
    private List<Atleta> cargarAtletas() {
        if (!Files.exists(archivoDatos)) {
            return new ArrayList<>();
        }

        try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(archivoDatos))) {
            return (List<Atleta>) entrada.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("No se pudieron cargar los atletas guardados: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void persistirAtletas() {
        try {
            Files.createDirectories(archivoDatos.getParent());
            try (ObjectOutputStream salida = new ObjectOutputStream(Files.newOutputStream(archivoDatos))) {
                salida.writeObject(atletas);
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudieron guardar los atletas", e);
        }
    }
}
