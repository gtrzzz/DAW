package ligaesports;

public class Jugador extends PersonaLiga implements Entrenable { //clase jugador, hereda de PersonaLiga e implementa Entrenable porque puede entrenar

    private Rol rol; //rol del jugador dentro del equipo
    private int nivelMecanico; //nivel de habilidad mecánica del jugador
    private int nivelEstrategico; //nivel de estrategia y toma de decisiones
    private int partidasJugadas; //partidas que ha jugado
    private int mvpTotales; //veces que ha sido mvp de un partido
    private boolean sancionado; //indica si el jugador está sancionado o no

    private int puntosMvp; //puntos extra para elegir el mvp del torneo
    private int highlightsRealizados; //jugadas destacadas que ha hecho

    public Jugador() { //constructor vacío
        super(); //llama al constructor vacío de PersonaLiga
    }

    public Jugador(String id, String nombre, String nickname, int edad, double salarioBase,
                   Rol rol, int nivelMecanico, int nivelEstrategico) { //constructor con todos los datos del jugador

        super(id, nombre, nickname, edad, salarioBase); //mando los datos comunes al constructor de PersonaLiga
        setRol(rol); //guardo el rol usando el setter para validar que no sea null
        setNivelMecanico(nivelMecanico); //guardo el nivel mecánico validado
        setNivelEstrategico(nivelEstrategico); //guardo el nivel estratégico validado
        this.partidasJugadas = 0; //empieza sin partidas jugadas
        this.mvpTotales = 0; //empieza sin mvps
        this.sancionado = false; //por defecto no está sancionado
        this.puntosMvp = 0; //empieza con 0 puntos mvp
        this.highlightsRealizados = 0; //empieza sin highlights
    }

    @Override //implemento el método de la interfaz Entrenable
    public void entrenar() { //método para mejorar los niveles del jugador
        if (nivelMecanico >= 100 && nivelEstrategico >= 100) { //si ya tiene los dos niveles al máximo no hace falta tocar nada
            return; //salgo del método porque no se puede mejorar más
        }

        nivelMecanico += 2; //subo un poco más el nivel mecánico
        nivelEstrategico += 1; //subo también el nivel estratégico

        if (nivelMecanico > 100) { //si se pasa de 100 lo dejo justo en 100
            nivelMecanico = 100; //máximo permitido
        }

        if (nivelEstrategico > 100) { //lo mismo con el nivel estratégico
            nivelEstrategico = 100; //máximo permitido
        }

    }

    @Override //implemento calcularRendimiento de Entrenable
    public double calcularRendimiento() { //calcula un rendimiento aproximado del jugador
        return (nivelMecanico * 0.6) + (nivelEstrategico * 0.4) + (mvpTotales * 2) + puntosMvp; //doy más peso al nivel mecánico y sumo extras por mvps
    }

    @Override //sobreescribo el método abstracto de PersonaLiga
    public double calcularCosteMensual() { //calcula el coste mensual del jugador
        return getSalarioBase() + (calcularRendimiento() * 10) + (mvpTotales * 100); //cuanto mejor rendimiento y más mvps, más cuesta
    }

    @Override
    public void mostrarResumen() { //muestra los datos importantes del jugador
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

    public void sumarPartidaJugada() { //método para sumar una partida jugada
        partidasJugadas++; //aumento el contador de partidas
    }

    public void sumarMvp() { //método que se llama cuando el jugador es mvp
        mvpTotales++; //sumo un mvp total
        puntosMvp += 3; //también le sumo puntos para el mvp del torneo
    }

    public void registrarHighlight() { //método para guardar una jugada destacada
        highlightsRealizados++; //sumo un highlight
        puntosMvp += 1; //los highlights también cuentan un poco para el mvp del torneo
    }

    public Rol getRol() { //getter del rol
        return rol; //devuelve el rol del jugador
    }

    public void setRol(Rol rol) { //setter del rol
        if (rol == null) { //el rol no puede estar vacío porque todos los jugadores deben tener uno
            throw new IllegalArgumentException("El rol no puede estar vacío.");
        }

        this.rol = rol; //guardo el rol
    }

    public int getNivelMecanico() { //getter del nivel mecánico
        return nivelMecanico; //devuelve el nivel mecánico
    }

    public void setNivelMecanico(int nivelMecanico) { //setter del nivel mecánico
        if (nivelMecanico < 0 || nivelMecanico > 100) { //compruebo que esté entre 0 y 100
            throw new IllegalArgumentException("El nivel mecánico debe estar entre 0 y 100.");
        }

        this.nivelMecanico = nivelMecanico; //guardo el nivel si es válido
    }

    public int getNivelEstrategico() { //getter del nivel estratégico
        return nivelEstrategico; //devuelve el nivel estratégico
    }

    public void setNivelEstrategico(int nivelEstrategico) { //setter del nivel estratégico
        if (nivelEstrategico < 0 || nivelEstrategico > 100) { //compruebo que esté entre 0 y 100
            throw new IllegalArgumentException("El nivel estratégico debe estar entre 0 y 100.");
        }

        this.nivelEstrategico = nivelEstrategico; //guardo el nivel si es válido
    }

    public int getPartidasJugadas() { //getter de partidas jugadas
        return partidasJugadas; //devuelve las partidas jugadas
    }

    public int getMvpTotales() { //getter de mvps
        return mvpTotales; //devuelve los mvps totales
    }

    public boolean isSancionado() { //getter booleano para saber si está sancionado
        return sancionado; //devuelve true si está sancionado y false si no
    }

    public void setSancionado(boolean sancionado) { //setter de sancionado
        this.sancionado = sancionado; //cambia el estado de sanción del jugador
    }

    public int getPuntosMvp() { //getter de puntos mvp
        return puntosMvp; //devuelve los puntos mvp del torneo
    }

    public int getHighlightsRealizados() { //getter de highlights
        return highlightsRealizados; //devuelve los highlights realizados
    }

    @Override //sobreescribo toString para mostrar el jugador con datos útiles
    public String toString() { //devuelve un resumen del jugador en una línea
        return super.toString() +
                " | Rol: " + rol +
                " | Rendimiento: " + calcularRendimiento() +
                " | Puntos MVP: " + puntosMvp +
                " | Sancionado: " + (sancionado ? "Sí" : "No");
    }
}
