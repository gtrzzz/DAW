package ligaesports;

public class Jugador extends PersonaLiga implements Entrenable {

    private Rol rol;
    private int nivelMecanico;
    private int nivelEstrategico;
    private int partidasJugadas;
    private int mvpTotales;
    private boolean sancionado;

    private int puntosMvp;
    private int highlightsRealizados;

    public Jugador() {
        super();
    }

    public Jugador(String id, String nombre, String nickname, int edad, double salarioBase,
                   Rol rol, int nivelMecanico, int nivelEstrategico) {

        super(id, nombre, nickname, edad, salarioBase);
        setRol(rol);
        setNivelMecanico(nivelMecanico);
        setNivelEstrategico(nivelEstrategico);
        this.partidasJugadas = 0;
        this.mvpTotales = 0;
        this.sancionado = false;
        this.puntosMvp = 0;
        this.highlightsRealizados = 0;
    }

    @Override
    public void entrenar() {
        if (nivelMecanico >= 100 && nivelEstrategico >= 100) {
            return;
        }

        nivelMecanico += 2;
        nivelEstrategico += 1;

        if (nivelMecanico > 100) {
            nivelMecanico = 100;
        }

        if (nivelEstrategico > 100) {
            nivelEstrategico = 100;
        }

    }

    @Override
    public double calcularRendimiento() {
        return (nivelMecanico * 0.6) + (nivelEstrategico * 0.4) + (mvpTotales * 2) + puntosMvp;
    }

    @Override
    public double calcularCosteMensual() {
        return getSalarioBase() + (calcularRendimiento() * 10) + (mvpTotales * 100);
    }

    @Override
    public void mostrarResumen() {
        System.out.println("Jugador: " + getNickname());
        System.out.println("Nombre real: " + getNombre());
        System.out.println("Rol: " + rol);
        System.out.println("Nivel mecánico: " + nivelMecanico);
        System.out.println("Nivel estratégico: " + nivelEstrategico);
        System.out.println("Partidas jugadas: " + partidasJugadas);
        System.out.println("MVP de partido: " + mvpTotales);
        System.out.println("Highlights realizados: " + highlightsRealizados);
        System.out.println("Puntos MVP torneo: " + puntosMvp);
        System.out.println("Rendimiento: " + calcularRendimiento());
        System.out.println("Sancionado: " + (sancionado ? "Sí" : "No"));
    }

    public void sumarPartidaJugada() {
        partidasJugadas++;
    }

    public void sumarMvp() {
        mvpTotales++;
        puntosMvp += 3;
    }

    public void registrarHighlight() {
        highlightsRealizados++;
        puntosMvp += 1;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        if (rol == null) {
            throw new IllegalArgumentException("El rol no puede estar vacío.");
        }

        this.rol = rol;
    }

    public int getNivelMecanico() {
        return nivelMecanico;
    }

    public void setNivelMecanico(int nivelMecanico) {
        if (nivelMecanico < 0 || nivelMecanico > 100) {
            throw new IllegalArgumentException("El nivel mecánico debe estar entre 0 y 100.");
        }

        this.nivelMecanico = nivelMecanico;
    }

    public int getNivelEstrategico() {
        return nivelEstrategico;
    }

    public void setNivelEstrategico(int nivelEstrategico) {
        if (nivelEstrategico < 0 || nivelEstrategico > 100) {
            throw new IllegalArgumentException("El nivel estratégico debe estar entre 0 y 100.");
        }

        this.nivelEstrategico = nivelEstrategico;
    }

    public int getPartidasJugadas() {
        return partidasJugadas;
    }

    public int getMvpTotales() {
        return mvpTotales;
    }

    public boolean isSancionado() {
        return sancionado;
    }

    public void setSancionado(boolean sancionado) {
        this.sancionado = sancionado;
    }

    public int getPuntosMvp() {
        return puntosMvp;
    }

    public int getHighlightsRealizados() {
        return highlightsRealizados;
    }

    @Override
    public String toString() {
        return super.toString() +
                " | Rol: " + rol +
                " | Rendimiento: " + calcularRendimiento() +
                " | Puntos MVP: " + puntosMvp +
                " | Sancionado: " + (sancionado ? "Sí" : "No");
    }
}
