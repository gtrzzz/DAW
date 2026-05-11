package ligaesports;

public class Evento {

    private String hora;
    private String nombre;
    private String descripcion;
    private CategoriaEvento categoria;
    private String protagonista;

    public Evento(String hora, String nombre, String descripcion, CategoriaEvento categoria, String protagonista) {
        this.hora = hora;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.protagonista = protagonista;
    }

    public void mostrarEvento() {
        System.out.println(hora + " - " + nombre);
        System.out.println("Categoría: " + categoria);
        System.out.println("Descripción: " + descripcion);

        if (protagonista != null && !protagonista.equals("")) {
            System.out.println("Protagonizado por: " + protagonista);
        }
    }

    public String getHora() {
        return hora;
    }

    public String getNombre() {
        return nombre;
    }

    public CategoriaEvento getCategoria() {
        return categoria;
    }

    public String getProtagonista() {
        return protagonista;
    }

    @Override
    public String toString() {
        return hora + " - " + nombre + " | " + categoria + " | " + descripcion;
    }
}