package ligaesports;

public class Incidencia {

    private String id;
    private TipoIncidencia tipo;
    private String descripcion;
    private Equipo equipoRelacionado;
    private Jugador jugadorRelacionado;

    public Incidencia(String id, TipoIncidencia tipo, String descripcion, Equipo equipoRelacionado, Jugador jugadorRelacionado) {
        this.id = id;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.equipoRelacionado = equipoRelacionado;
        this.jugadorRelacionado = jugadorRelacionado;
    }

    public void aplicarSancion() {
        if (tipo == TipoIncidencia.SANCION && jugadorRelacionado != null) {
            jugadorRelacionado.setSancionado(true);
        }
    }

    public void mostrarIncidencia() {
        System.out.println("Incidencia: " + id);
        System.out.println("Tipo: " + tipo);
        System.out.println("Descripción: " + descripcion);

        if (equipoRelacionado != null) {
            System.out.println("Equipo: " + equipoRelacionado.getNombre());
        }

        if (jugadorRelacionado != null) {
            System.out.println("Jugador: " + jugadorRelacionado.getNickname());
        }
    }

    public String getId() {
        return id;
    }

    public Equipo getEquipoRelacionado() {
        return equipoRelacionado;
    }

    public Jugador getJugadorRelacionado() {
        return jugadorRelacionado;
    }
}