package ligaesports;

public class Patrocinador {

    private String nombre;
    private double aportacion;

    public Patrocinador(String nombre, double aportacion) {
        if (nombre == null || nombre.trim().equals("")) {
            throw new IllegalArgumentException("El nombre del patrocinador no puede estar vacío.");
        }

        if (aportacion < 0) {
            throw new IllegalArgumentException("La aportación no puede ser negativa.");
        }

        this.nombre = nombre.trim();
        this.aportacion = aportacion;
    }

    public String getNombre() {
        return nombre;
    }

    public double getAportacion() {
        return aportacion;
    }

    @Override
    public String toString() {
        return nombre + " | Aportación: " + aportacion + "€";
    }
}
