package ligaesports;

public class Patrocinador { //clase para guardar los patrocinadores de los equipos

    private String nombre; //nombre del patrocinador
    private double aportacion; //dinero que aporta el patrocinador al equipo

    public Patrocinador(String nombre, double aportacion) { //constructor con nombre y aportación
        if (nombre == null || nombre.trim().equals("")) { //compruebo que el nombre no esté vacío
            throw new IllegalArgumentException("El nombre del patrocinador no puede estar vacío.");
        }

        if (aportacion < 0) { //no dejo que la aportación sea negativa porque no tendría sentido
            throw new IllegalArgumentException("La aportación no puede ser negativa.");
        }

        this.nombre = nombre.trim(); //guardo el nombre quitando espacios innecesarios
        this.aportacion = aportacion; //guardo la aportación económica
    }

    public String getNombre() { //getter del nombre
        return nombre; //devuelve el nombre del patrocinador
    }

    public double getAportacion() { //getter de la aportación
        return aportacion; //devuelve el dinero que aporta
    }

    @Override //sobreescribo toString para mostrar el patrocinador con formato
    public String toString() { //devuelve un resumen del patrocinador
        return nombre + " | Aportación: " + aportacion + "€"; //muestra el nombre y el dinero aportado
    }
}
