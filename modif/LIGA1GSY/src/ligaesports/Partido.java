package ligaesports;

import java.util.Random;

public class Partido { //clase partido, aquí guardo los datos de cada enfrentamiento entre dos equipos

    private String id; //identificador del partido
    private int jornada; //jornada a la que pertenece el partido
    private Equipo local; //equipo local
    private Equipo visitante; //equipo visitante
    private int puntosLocal; //puntuación del equipo local
    private int puntosVisitante; //puntuación del equipo visitante
    private Jugador mvp; //jugador mvp del partido
    private boolean disputado; //indica si el partido ya se ha jugado o no

    private double dineroApostadoLocal; //apoyo económico o apuestas del equipo local
    private double dineroApostadoVisitante; //apoyo económico o apuestas del equipo visitante
    private String highlight; //jugada destacada del partido

    public Partido(String id, int jornada, Equipo local, Equipo visitante) throws PartidoInvalidoException { //constructor del partido con validaciones
        if (id == null || id.trim().equals("")) { //el id no puede estar vacío
            throw new PartidoInvalidoException("El ID del partido no puede estar vacío.");
        }

        if (jornada <= 0) { //la jornada tiene que ser mayor que 0
            throw new PartidoInvalidoException("La jornada debe ser mayor que 0.");
        }

        if (local == null || visitante == null) { //los dos equipos tienen que existir
            throw new PartidoInvalidoException("Los equipos del partido no pueden estar vacíos.");
        }

        if (local == visitante) { //no dejo que un equipo juegue contra sí mismo
            throw new PartidoInvalidoException("Un equipo no puede jugar contra sí mismo.");
        }

        this.id = id.trim(); //guardo el id sin espacios sobrantes
        this.jornada = jornada; //guardo la jornada
        this.local = local; //guardo el equipo local
        this.visitante = visitante; //guardo el equipo visitante
        this.puntosLocal = 0; //empieza sin puntos
        this.puntosVisitante = 0; //empieza sin puntos
        this.mvp = null; //al principio no hay mvp porque el partido no se ha jugado
        this.disputado = false; //el partido empieza como pendiente

        this.dineroApostadoLocal = generarDineroInicial(); //genero apoyo económico inicial para el local
        this.dineroApostadoVisitante = generarDineroInicial(); //genero apoyo económico inicial para el visitante
        this.highlight = ""; //el highlight empieza vacío
    }

    private double generarDineroInicial() { //genera una cantidad inicial aleatoria de apoyo económico
        Random random = new Random(); //creo random para sacar un número aleatorio
        return 3000 + random.nextInt(47001); //devuelvo una cantidad entre 3000 y 50000 aproximadamente
    }

    public void mostrarApuestas() { //muestra el apoyo económico que tiene cada equipo en este partido
        System.out.println("Apuestas/apoyo de comunidad para el partido " + id);
        System.out.println("1. " + local.getNombre() + ": " + dineroApostadoLocal + "€");
        System.out.println("2. " + visitante.getNombre() + ": " + dineroApostadoVisitante + "€");
    }

    public void apostarLocal(double cantidad) { //suma apoyo económico al equipo local
        if (cantidad >= 0) { //solo sumo si la cantidad no es negativa
            dineroApostadoLocal += cantidad; //sumo la cantidad al local
        }
    }

    public void apostarVisitante(double cantidad) { //suma apoyo económico al equipo visitante
        if (cantidad >= 0) { //solo sumo si no es negativa
            dineroApostadoVisitante += cantidad; //sumo la cantidad al visitante
        }
    }

    public void disputarAleatorio() throws JugadorSancionadoException, RolNoDisponibleException { //disputa el partido generando un resultado automático
        if (disputado) { //si ya se ha jugado, no lo vuelvo a disputar
            System.out.println("Este partido ya ha sido disputado.");
            return; //salgo del método para evitar registrar dos veces el partido
        }

        Jugador[] convocatoriaLocal = local.generarConvocatoriaValida(); //genero la convocatoria del local
        Jugador[] convocatoriaVisitante = visitante.generarConvocatoriaValida(); //genero la convocatoria del visitante

        Random random = new Random(); //random para los puntos del partido

        double fuerzaLocal = calcularFuerzaConvocatoria(convocatoriaLocal)
                + (local.calcularValorPatrocinadores() / 10000)
                + (dineroApostadoLocal / 50000);

        double fuerzaVisitante = calcularFuerzaConvocatoria(convocatoriaVisitante)
                + (visitante.calcularValorPatrocinadores() / 10000)
                + (dineroApostadoVisitante / 50000);

        puntosLocal = 10 + random.nextInt(21) + (int) (fuerzaLocal / 10); //calculo los puntos del local mezclando azar y fuerza
        puntosVisitante = 10 + random.nextInt(21) + (int) (fuerzaVisitante / 10); //calculo los puntos del visitante

        if (puntosLocal == puntosVisitante) { //evito empates porque necesito ganador
            if (fuerzaLocal >= fuerzaVisitante) { //si el local tiene más fuerza, le doy un punto extra
                puntosLocal++;
            } else {
                puntosVisitante++; //si no, se lo doy al visitante
            }
        }

        finalizarPartido(convocatoriaLocal, convocatoriaVisitante); //actualizo estadísticas, mvp, highlight y estado
    }

    public void registrarResultadoManual(int puntosLocal, int puntosVisitante) throws PartidoInvalidoException, JugadorSancionadoException, RolNoDisponibleException { //registra un resultado escrito por teclado
        if (disputado) { //no dejo registrar dos veces el mismo partido
            throw new PartidoInvalidoException("Este partido ya ha sido disputado.");
        }

        if (puntosLocal < 0 || puntosVisitante < 0) { //los puntos no pueden ser negativos
            throw new PartidoInvalidoException("Los puntos no pueden ser negativos.");
        }

        if (puntosLocal == puntosVisitante) { //tampoco permito empate porque hace falta ganador
            throw new PartidoInvalidoException("El resultado no puede quedar empatado.");
        }

        Jugador[] convocatoriaLocal = local.generarConvocatoriaValida(); //compruebo que el local pueda jugar
        Jugador[] convocatoriaVisitante = visitante.generarConvocatoriaValida(); //compruebo que el visitante pueda jugar

        this.puntosLocal = puntosLocal; //guardo los puntos introducidos del local
        this.puntosVisitante = puntosVisitante; //guardo los puntos introducidos del visitante

        finalizarPartido(convocatoriaLocal, convocatoriaVisitante); //termino el partido actualizando todo
    }

    private void finalizarPartido(Jugador[] convocatoriaLocal, Jugador[] convocatoriaVisitante) { //método común para cerrar un partido, sea manual o aleatorio
        actualizarEstadisticas(convocatoriaLocal, convocatoriaVisitante); //actualizo equipos y jugadores
        elegirMvpAleatorio(convocatoriaLocal, convocatoriaVisitante); //elijo el mvp entre los jugadores del ganador
        generarHighlight(); //genero una jugada destacada

        disputado = true; //marco el partido como disputado
    }

    private double calcularFuerzaConvocatoria(Jugador[] convocatoria) { //calcula la fuerza media de los jugadores convocados
        double total = 0; //acumulo el rendimiento de los jugadores

        for (Jugador jugador : convocatoria) {
            if (jugador != null) {
                total += jugador.calcularRendimiento(); //sumo el rendimiento individual
            }
        }

        return total / convocatoria.length; //devuelvo la media de la convocatoria
    }

    private void actualizarEstadisticas(Jugador[] convocatoriaLocal, Jugador[] convocatoriaVisitante) { //actualiza puntos, victorias y partidas jugadas
        local.sumarPuntosFavor(puntosLocal); //sumo puntos a favor del local
        local.sumarPuntosContra(puntosVisitante); //sumo puntos recibidos por el local

        visitante.sumarPuntosFavor(puntosVisitante); //sumo puntos a favor del visitante
        visitante.sumarPuntosContra(puntosLocal); //sumo puntos recibidos por el visitante

        if (puntosLocal > puntosVisitante) { //si gana el local, actualizo victorias y derrotas
            local.sumarVictoria();
            visitante.sumarDerrota();
        } else {
            visitante.sumarVictoria(); //si no, gana el visitante
            local.sumarDerrota();
        }

        for (Jugador jugador : convocatoriaLocal) { //a todos los convocados del local les sumo una partida
            if (jugador != null) {
                jugador.sumarPartidaJugada();
            }
        }

        for (Jugador jugador : convocatoriaVisitante) { //a todos los convocados del visitante les sumo una partida
            if (jugador != null) {
                jugador.sumarPartidaJugada();
            }
        }
    }

    private void elegirMvpAleatorio(Jugador[] convocatoriaLocal, Jugador[] convocatoriaVisitante) { //elige el mvp entre los jugadores del equipo ganador
        Random random = new Random(); //random para elegir una posición

        Jugador[] convocatoriaGanadora; //aquí guardo la convocatoria del equipo que ganó

        if (puntosLocal > puntosVisitante) { //si ganó el local, uso su convocatoria
            convocatoriaGanadora = convocatoriaLocal;
        } else {
            convocatoriaGanadora = convocatoriaVisitante; //si no, uso la del visitante
        }

        int posicion = random.nextInt(convocatoriaGanadora.length); //elijo una posición aleatoria
        mvp = convocatoriaGanadora[posicion]; //guardo el jugador como mvp

        if (mvp != null) { //si hay jugador válido, le sumo un mvp
            mvp.sumarMvp();
        }
    }

    private void generarHighlight() { //genera una frase destacada para el partido
        String[] frases = { //array de frases posibles para que no siempre salga lo mismo
                "realizó una triple eliminación decisiva",
                "lideró una remontada en los últimos minutos",
                "salvó una jugada clave para su equipo",
                "dominó el tramo final del partido",
                "consiguió una jugada espectacular frente al público",
                "forzó una jugada imposible que cambió el ritmo del partido",
                "coordinó una pelea de equipo perfecta"
        };

        Random random = new Random(); //random para elegir una frase
        String frase = frases[random.nextInt(frases.length)]; //guardo una frase aleatoria

        if (mvp != null) { //si hay mvp, el highlight queda asociado a ese jugador
            highlight = mvp.getNickname() + " " + frase + ".";
            mvp.registrarHighlight(); //sumo un highlight al jugador
        } else {
            highlight = "El partido tuvo una jugada destacada, pero no se registró MVP.";
        }
    }

    public Equipo calcularGanador() { //devuelve el equipo ganador del partido
        if (!disputado) { //si todavía no se ha jugado, no hay ganador
            return null; //devuelvo null porque no se puede calcular
        }

        if (puntosLocal > puntosVisitante) { //si el local tiene más puntos
            return local; //gana el local
        }

        return visitante; //si no, gana el visitante
    }

    public void mostrarPartido() { //muestra toda la información visible del partido
        System.out.println("Partido " + id + " | Jornada " + jornada);
        System.out.println(local.getNombre() + " vs " + visitante.getNombre());

        mostrarApuestas();

        if (disputado) { //si ya se jugó, muestro resultado, ganador, mvp y highlight
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

    public String getId() { //getter del id del partido
        return id;
    }

    public int getJornada() { //getter de la jornada
        return jornada;
    }

    public boolean isDisputado() { //getter que indica si ya se jugó
        return disputado;
    }

    public Equipo getLocal() { //getter del equipo local
        return local;
    }

    public Equipo getVisitante() { //getter del equipo visitante
        return visitante;
    }

    public Jugador getMvp() { //getter del mvp del partido
        return mvp;
    }

    public String getHighlight() { //getter de la jugada destacada
        return highlight;
    }

    @Override //sobreescribo toString para mostrar el partido resumido
    public String toString() { //devuelve el partido en una línea
        return "Partido " + id + " | Jornada " + jornada + " | " +
                local.getNombre() + " vs " + visitante.getNombre() +
                " | Disputado: " + (disputado ? "Sí" : "No");
    }
}
