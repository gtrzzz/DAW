package ligaesports;

public class Incidencia { //clase para guardar cualquier problema o situación especial que pase en la liga

    private String id; //identificador de la incidencia
    private TipoIncidencia tipo; //tipo de incidencia usando el enum TipoIncidencia
    private String descripcion; //explicación de lo que ha pasado
    private Equipo equipoRelacionado; //equipo relacionado, puede ser null si no afecta a un equipo concreto
    private Jugador jugadorRelacionado; //jugador relacionado, puede ser null si no afecta a un jugador concreto

    public Incidencia(String id, TipoIncidencia tipo, String descripcion, Equipo equipoRelacionado, Jugador jugadorRelacionado) { //constructor con todos los datos de la incidencia
        this.id = id; //guardo el id de la incidencia
        this.tipo = tipo; //guardo el tipo de incidencia
        this.descripcion = descripcion; //guardo la descripción
        this.equipoRelacionado = equipoRelacionado; //guardo el equipo relacionado si lo hay
        this.jugadorRelacionado = jugadorRelacionado; //guardo el jugador relacionado si lo hay
    }

    public void aplicarSancion() { //método que aplica sanción si la incidencia bloquea al jugador
        if ((tipo == TipoIncidencia.SANCION || tipo == TipoIncidencia.EXPULSION) && jugadorRelacionado != null) { //sanción y expulsión dejan al jugador sin poder jugar
            jugadorRelacionado.setSancionado(true); //marco al jugador como sancionado
        }
    }

    public void mostrarIncidencia() { //método para imprimir los datos de la incidencia
        System.out.println("Incidencia: " + id); //muestra el id
        System.out.println("Tipo: " + tipo); //muestra el tipo
        System.out.println("Descripción: " + descripcion); //muestra la descripción

        if (equipoRelacionado != null) { //si hay equipo relacionado, se muestra
            System.out.println("Equipo: " + equipoRelacionado.getNombre()); //muestra el nombre del equipo
        }

        if (jugadorRelacionado != null) { //si hay jugador relacionado, se muestra
            System.out.println("Jugador: " + jugadorRelacionado.getNickname()); //muestra el nickname del jugador
        }
    }

    public String getId() { //getter del id
        return id; //devuelve el id de la incidencia
    }

    public Equipo getEquipoRelacionado() { //getter del equipo relacionado
        return equipoRelacionado; //devuelve el equipo relacionado
    }

    public Jugador getJugadorRelacionado() { //getter del jugador relacionado
        return jugadorRelacionado; //devuelve el jugador relacionado
    }
}
