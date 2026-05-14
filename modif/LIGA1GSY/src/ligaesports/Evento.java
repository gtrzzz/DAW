package ligaesports;

public class Evento { //clase que uso para guardar cada evento del día del torneo

    private String hora; //hora a la que ocurre el evento
    private String nombre; //nombre corto del evento
    private String descripcion; //explicación de lo que pasa en ese evento
    private CategoriaEvento categoria; //tipo de evento, por ejemplo música, comida, show, premios, etc
    private String protagonista; //persona, marca u organización que aparece como protagonista del evento

    public Evento(String hora, String nombre, String descripcion, CategoriaEvento categoria, String protagonista) { //constructor con todos los datos del evento
        this.hora = hora; //guardo la hora que se le pasa al constructor
        this.nombre = nombre; //guardo el nombre del evento
        this.descripcion = descripcion; //guardo la descripción del evento
        this.categoria = categoria; //guardo la categoría usando el enum CategoriaEvento
        this.protagonista = protagonista; //guardo quién protagoniza el evento, si lo hay
    }

    public void mostrarEvento() { //método para mostrar por pantalla la información del evento
        System.out.println(hora + " - " + nombre); //imprime la hora y el nombre en la misma línea
        System.out.println("Categoría: " + categoria); //muestra la categoría del evento
        System.out.println("Descripción: " + descripcion); //muestra la descripción

        if (protagonista != null && !protagonista.equals("")) { //si hay protagonista, también lo enseño
            System.out.println("Protagonizado por: " + protagonista); //imprime el protagonista del evento
        }
    }

    public String getHora() { //getter de la hora
        return hora; //devuelve la hora del evento
    }

    public String getNombre() { //getter del nombre
        return nombre; //devuelve el nombre del evento
    }

    public CategoriaEvento getCategoria() { //getter de la categoría
        return categoria; //devuelve la categoría del evento
    }

    public String getProtagonista() { //getter del protagonista
        return protagonista; //devuelve el protagonista del evento
    }

    @Override //sobreescribo toString para que al imprimir un evento salga información útil
    public String toString() { //devuelve el evento resumido en una línea
        return hora + " - " + nombre + " | " + categoria + " | " + descripcion; //junto hora, nombre, categoría y descripción
    }
}
