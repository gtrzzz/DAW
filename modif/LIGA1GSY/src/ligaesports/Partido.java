package ligaesports;

import java.util.Random;

public class Partido {

    private String id;
    private int jornada;
    private Equipo local;
    private Equipo visitante;
    private int puntosLocal;
    private int puntosVisitante;
    private Jugador mvp;
    private boolean disputado;

    private double dineroApostadoLocal;
    private double dineroApostadoVisitante;
    private String highlight;

    public Partido(String id, int jornada, Equipo local, Equipo visitante) throws PartidoInvalidoException {
        if (id == null || id.trim().equals("")) {
            throw new PartidoInvalidoException("El ID del partido no puede estar vacío.");
        }

        if (jornada <= 0) {
            throw new PartidoInvalidoException("La jornada debe ser mayor que 0.");
        }

        if (local == null || visitante == null) {
            throw new PartidoInvalidoException("Los equipos del partido no pueden estar vacíos.");
        }

        if (local == visitante) {
            throw new PartidoInvalidoException("Un equipo no puede jugar contra sí mismo.");
        }

        this.id = id.trim();
        this.jornada = jornada;
        this.local = local;
        this.visitante = visitante;
        this.puntosLocal = 0;
        this.puntosVisitante = 0;
        this.mvp = null;
        this.disputado = false;

        this.dineroApostadoLocal = generarDineroInicial();
        this.dineroApostadoVisitante = generarDineroInicial();
        this.highlight = "";
    }

    private double generarDineroInicial() {
        Random random = new Random();
        return 3000 + random.nextInt(47001);
    }

    public void mostrarApuestas() {
        System.out.println("Apuestas/apoyo de comunidad para el partido " + id);
        System.out.println("1. " + local.getNombre() + ": " + dineroApostadoLocal + "€");
        System.out.println("2. " + visitante.getNombre() + ": " + dineroApostadoVisitante + "€");
    }

    public void apostarLocal(double cantidad) {
        if (cantidad >= 0) {
            dineroApostadoLocal += cantidad;
        }
    }

    public void apostarVisitante(double cantidad) {
        if (cantidad >= 0) {
            dineroApostadoVisitante += cantidad;
        }
    }

    public void disputarAleatorio() throws JugadorSancionadoException, RolNoDisponibleException {
        if (disputado) {
            System.out.println("Este partido ya ha sido disputado.");
            return;
        }

        Jugador[] convocatoriaLocal = local.generarConvocatoriaValida();
        Jugador[] convocatoriaVisitante = visitante.generarConvocatoriaValida();

        Random random = new Random();

        double fuerzaLocal = calcularFuerzaConvocatoria(convocatoriaLocal)
                + (local.calcularValorPatrocinadores() / 10000)
                + (dineroApostadoLocal / 50000);

        double fuerzaVisitante = calcularFuerzaConvocatoria(convocatoriaVisitante)
                + (visitante.calcularValorPatrocinadores() / 10000)
                + (dineroApostadoVisitante / 50000);

        puntosLocal = 10 + random.nextInt(21) + (int) (fuerzaLocal / 10);
        puntosVisitante = 10 + random.nextInt(21) + (int) (fuerzaVisitante / 10);

        if (puntosLocal == puntosVisitante) {
            if (fuerzaLocal >= fuerzaVisitante) {
                puntosLocal++;
            } else {
                puntosVisitante++;
            }
        }

        finalizarPartido(convocatoriaLocal, convocatoriaVisitante);
    }

    public void registrarResultadoManual(int puntosLocal, int puntosVisitante) throws PartidoInvalidoException, JugadorSancionadoException, RolNoDisponibleException {
        if (disputado) {
            throw new PartidoInvalidoException("Este partido ya ha sido disputado.");
        }

        if (puntosLocal < 0 || puntosVisitante < 0) {
            throw new PartidoInvalidoException("Los puntos no pueden ser negativos.");
        }

        if (puntosLocal == puntosVisitante) {
            throw new PartidoInvalidoException("El resultado no puede quedar empatado.");
        }

        Jugador[] convocatoriaLocal = local.generarConvocatoriaValida();
        Jugador[] convocatoriaVisitante = visitante.generarConvocatoriaValida();

        this.puntosLocal = puntosLocal;
        this.puntosVisitante = puntosVisitante;

        finalizarPartido(convocatoriaLocal, convocatoriaVisitante);
    }

    private void finalizarPartido(Jugador[] convocatoriaLocal, Jugador[] convocatoriaVisitante) {
        actualizarEstadisticas(convocatoriaLocal, convocatoriaVisitante);
        elegirMvpAleatorio(convocatoriaLocal, convocatoriaVisitante);
        generarHighlight();

        disputado = true;
    }

    private double calcularFuerzaConvocatoria(Jugador[] convocatoria) {
        double total = 0;

        for (Jugador jugador : convocatoria) {
            if (jugador != null) {
                total += jugador.calcularRendimiento();
            }
        }

        return total / convocatoria.length;
    }

    private void actualizarEstadisticas(Jugador[] convocatoriaLocal, Jugador[] convocatoriaVisitante) {
        local.sumarPuntosFavor(puntosLocal);
        local.sumarPuntosContra(puntosVisitante);

        visitante.sumarPuntosFavor(puntosVisitante);
        visitante.sumarPuntosContra(puntosLocal);

        if (puntosLocal > puntosVisitante) {
            local.sumarVictoria();
            visitante.sumarDerrota();
        } else {
            visitante.sumarVictoria();
            local.sumarDerrota();
        }

        for (Jugador jugador : convocatoriaLocal) {
            if (jugador != null) {
                jugador.sumarPartidaJugada();
            }
        }

        for (Jugador jugador : convocatoriaVisitante) {
            if (jugador != null) {
                jugador.sumarPartidaJugada();
            }
        }
    }

    private void elegirMvpAleatorio(Jugador[] convocatoriaLocal, Jugador[] convocatoriaVisitante) {
        Random random = new Random();

        Jugador[] convocatoriaGanadora;

        if (puntosLocal > puntosVisitante) {
            convocatoriaGanadora = convocatoriaLocal;
        } else {
            convocatoriaGanadora = convocatoriaVisitante;
        }

        int posicion = random.nextInt(convocatoriaGanadora.length);
        mvp = convocatoriaGanadora[posicion];

        if (mvp != null) {
            mvp.sumarMvp();
        }
    }

    private void generarHighlight() {
        String[] frases = {
                "realizó una triple eliminación decisiva",
                "lideró una remontada en los últimos minutos",
                "salvó una jugada clave para su equipo",
                "dominó el tramo final del partido",
                "consiguió una jugada espectacular frente al público",
                "forzó una jugada imposible que cambió el ritmo del partido",
                "coordinó una pelea de equipo perfecta"
        };

        Random random = new Random();
        String frase = frases[random.nextInt(frases.length)];

        if (mvp != null) {
            highlight = mvp.getNickname() + " " + frase + ".";
            mvp.registrarHighlight();
        } else {
            highlight = "El partido tuvo una jugada destacada, pero no se registró MVP.";
        }
    }

    public Equipo calcularGanador() {
        if (!disputado) {
            return null;
        }

        if (puntosLocal > puntosVisitante) {
            return local;
        }

        return visitante;
    }

    public void mostrarPartido() {
        System.out.println("Partido " + id + " | Jornada " + jornada);
        System.out.println(local.getNombre() + " vs " + visitante.getNombre());

        mostrarApuestas();

        if (disputado) {
            System.out.println("Resultado: " + puntosLocal + " - " + puntosVisitante);
            System.out.println("Ganador: " + calcularGanador().getNombre());

            if (mvp != null) {
                System.out.println("MVP del partido: " + mvp.getNickname());
            }

            System.out.println("Highlight: " + highlight);
        } else {
            System.out.println("Estado: pendiente");
        }
    }

    public String getId() {
        return id;
    }

    public int getJornada() {
        return jornada;
    }

    public boolean isDisputado() {
        return disputado;
    }

    public Equipo getLocal() {
        return local;
    }

    public Equipo getVisitante() {
        return visitante;
    }

    public Jugador getMvp() {
        return mvp;
    }

    public String getHighlight() {
        return highlight;
    }

    @Override
    public String toString() {
        return "Partido " + id + " | Jornada " + jornada + " | " +
                local.getNombre() + " vs " + visitante.getNombre() +
                " | Disputado: " + (disputado ? "Sí" : "No");
    }
}
