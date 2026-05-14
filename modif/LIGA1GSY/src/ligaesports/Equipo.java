package ligaesports;

import java.util.ArrayList;

public class Equipo { //clase equipo, donde guardo entrenador, titulares, suplentes, patrocinadores y estadísticas

    private String nombre; //nombre del equipo
    private String ciudad; //ciudad del equipo
    private Entrenador entrenador; //entrenador asignado al equipo
    private double presupuesto; //presupuesto que tiene el equipo

    private int victorias; //número de partidos ganados
    private int derrotas; //número de partidos perdidos
    private int puntosFavor; //puntos que ha conseguido el equipo en los partidos
    private int puntosContra; //puntos que le han hecho los rivales

    private Jugador[] titulares; //array fijo de 5 titulares, uno por cada rol
    private ArrayList<Jugador> suplentes; //lista dinámica de suplentes
    private ArrayList<Patrocinador> patrocinadores; //lista dinámica de patrocinadores del equipo

    public Equipo(String nombre, String ciudad, double presupuesto) { //constructor del equipo con los datos principales
        if (nombre == null || nombre.trim().equals("")) { //compruebo que el nombre no esté vacío
            throw new IllegalArgumentException("El nombre del equipo no puede estar vacío.");
        }

        if (ciudad == null || ciudad.trim().equals("")) { //compruebo que la ciudad no esté vacía
            throw new IllegalArgumentException("La ciudad del equipo no puede estar vacía.");
        }

        if (presupuesto < 0) { //el presupuesto no puede ser negativo
            throw new IllegalArgumentException("El presupuesto no puede ser negativo.");
        }

        this.nombre = nombre.trim(); //guardo el nombre sin espacios sobrantes
        this.ciudad = ciudad.trim(); //guardo la ciudad sin espacios sobrantes
        this.presupuesto = presupuesto; //guardo el presupuesto
        this.victorias = 0; //empieza con 0 victorias
        this.derrotas = 0; //empieza con 0 derrotas
        this.puntosFavor = 0; //empieza sin puntos a favor
        this.puntosContra = 0; //empieza sin puntos en contra
        this.titulares = new Jugador[5]; //creo el array fijo de titulares que pide el proyecto
        this.suplentes = new ArrayList<Jugador>(); //creo la lista dinámica de suplentes
        this.patrocinadores = new ArrayList<Patrocinador>(); //creo la lista de patrocinadores
    }

    public void asignarEntrenador(Entrenador entrenador) { //método para asignar un entrenador al equipo
        this.entrenador = entrenador; //guardo el entrenador recibido
    }

    public void ficharTitular(Jugador jugador) throws RolNoDisponibleException { //método para fichar un jugador como titular
        if (jugador == null) { //compruebo que el jugador no sea null
            throw new IllegalArgumentException("El jugador titular no puede estar vacío.");
        }

        int posicion = jugador.getRol().ordinal(); //uso la posición del enum para colocar cada rol en una posición del array

        if (titulares[posicion] != null) { //si ya hay alguien en esa posición, ese rol ya está ocupado
            throw new RolNoDisponibleException("Ya existe un titular con el rol " + jugador.getRol());
        }

        titulares[posicion] = jugador; //guardo el jugador como titular en su posición
    }

    public void ficharSuplente(Jugador jugador) { //método para añadir un suplente al equipo
        if (jugador == null) { //compruebo que el jugador exista
            throw new IllegalArgumentException("El jugador suplente no puede estar vacío.");
        }

        if (contieneJugador(jugador)) { //si ya está en el equipo, no lo añado dos veces
            System.out.println("Ese jugador ya pertenece a este equipo.");
            return; //salgo del método porque no hay que hacer nada más
        }

        suplentes.add(jugador); //lo añado a la lista de suplentes
    }

    public boolean esTitular(Jugador jugador) { //comprueba si un jugador está en el array de titulares
        for (Jugador titular : titulares) { //recorro todos los titulares
            if (titular == jugador) { //comparo la referencia del jugador
                return true; //si coincide, es titular
            }
        }

        return false; //si no lo he encontrado, no es titular
    }

    public boolean eliminarJugador(Jugador jugador) { //elimina un jugador del equipo si está como titular o suplente
        for (int i = 0; i < titulares.length; i++) { //primero lo busco entre los titulares
            if (titulares[i] == jugador) { //si coincide con el titular de esa posición
                titulares[i] = null; //dejo ese hueco vacío
                return true; //devuelvo true porque se ha eliminado
            }
        }

        return suplentes.remove(jugador); //si no era titular, intento eliminarlo de suplentes
    }

    public void sustituirTitularPorSuplente(Rol rol, Jugador suplente) throws RolNoDisponibleException, JugadorSancionadoException { //cambia un titular por un suplente del mismo rol
        if (rol == null) { //compruebo que se haya indicado un rol
            throw new IllegalArgumentException("El rol no puede estar vacío.");
        }

        if (suplente == null) { //compruebo que el suplente exista
            throw new IllegalArgumentException("El suplente no puede estar vacío.");
        }

        if (suplente.getRol() != rol) { //el suplente debe tener el mismo rol que se quiere sustituir
            throw new RolNoDisponibleException("El suplente no tiene el rol " + rol + ".");
        }

        if (suplente.isSancionado()) { //no dejo entrar a un suplente sancionado
            throw new JugadorSancionadoException("El suplente " + suplente.getNickname() + " está sancionado.");
        }

        if (!suplentes.contains(suplente)) { //compruebo que el jugador esté realmente en la lista de suplentes del equipo
            throw new RolNoDisponibleException("Ese jugador no está en la lista de suplentes del equipo.");
        }

        int posicion = rol.ordinal(); //calculo la posición del rol en el array de titulares
        Jugador titularAnterior = titulares[posicion]; //guardo el titular anterior para pasarlo a suplente
        titulares[posicion] = suplente; //el suplente pasa a ser titular
        suplentes.remove(suplente); //quito al suplente de la lista porque ya no es suplente

        if (titularAnterior != null) { //si había titular antes, lo paso a suplente
            suplentes.add(titularAnterior); //añado el titular anterior a la lista de suplentes
        }
    }

    public boolean contieneJugador(Jugador jugador) { //comprueba si un jugador pertenece a este equipo
        for (Jugador titular : titulares) { //primero lo busco entre titulares
            if (titular == jugador) { //comparo por referencia porque es el mismo objeto jugador
                return true;
            }
        }

        return suplentes.contains(jugador); //si no está de titular, miro en suplentes
    }

    public Jugador buscarSuplenteCompatible(Rol rol) { //busca un suplente del mismo rol que pueda jugar
        for (Jugador jugador : suplentes) { //recorro la lista de suplentes
            if (jugador.getRol() == rol && !jugador.isSancionado()) { //tiene que tener el rol correcto y no estar sancionado
                return jugador; //devuelvo el primero válido
            }
        }

        return null; //si no encuentro ninguno, devuelvo null
    }

    public Jugador[] generarConvocatoriaValida() throws JugadorSancionadoException, RolNoDisponibleException { //genera los 5 jugadores que van a jugar un partido
        Jugador[] convocatoria = new Jugador[5]; //array fijo con exactamente 5 jugadores

        for (int i = 0; i < titulares.length; i++) { //recorro cada rol titular
            Rol rolNecesario = Rol.values()[i]; //obtengo el rol que toca en esa posición
            Jugador titular = titulares[i]; //cojo el titular de ese rol

            if (titular != null && !titular.isSancionado()) { //si hay titular y no está sancionado, juega él
                convocatoria[i] = titular; //lo meto en la convocatoria
            } else {
                Jugador suplente = buscarSuplenteCompatible(rolNecesario); //si no puede jugar el titular, busco suplente del mismo rol

                if (suplente != null) { //si encuentro suplente compatible
                    convocatoria[i] = suplente; //lo meto en la convocatoria
                } else {
                    if (titular != null && titular.isSancionado()) { //si el problema es que el titular está sancionado y no hay suplente
                        throw new JugadorSancionadoException("El titular " + titular.getNickname() +
                                " está sancionado y no hay suplente disponible para el rol " + rolNecesario);
                    } else {
                        //si directamente falta jugador para ese rol, lanzo excepción de rol no disponible
                        throw new RolNoDisponibleException("Falta jugador para el rol " + rolNecesario);
                    }
                }
            }
        }

        return convocatoria; //devuelvo la convocatoria ya validada
    }

    public void validarConvocatoria() throws JugadorSancionadoException, RolNoDisponibleException { //comprueba si el equipo puede jugar
        generarConvocatoriaValida(); //si hay algún problema, este método lanza la excepción
    }

    public void agregarPatrocinador(Patrocinador patrocinador) { //añade un patrocinador al equipo
        if (patrocinador == null) { //compruebo que no venga vacío
            throw new IllegalArgumentException("El patrocinador no puede estar vacío.");
        }

        patrocinadores.add(patrocinador); //lo guardo en la lista de patrocinadores
    }

    public Patrocinador obtenerPrimerPatrocinador() { //devuelve el primer patrocinador del equipo si existe
        if (patrocinadores.isEmpty()) { //si no hay patrocinadores, no puedo devolver ninguno
            return null;
        }

        return patrocinadores.get(0); //devuelvo el primero de la lista
    }

    public double calcularCosteTotalEquipo() { //calcula cuánto cuesta mantener la plantilla al mes
        double total = 0; //acumulo salarios y costes

        if (entrenador != null) { //si hay entrenador, también cuenta su coste
            total += entrenador.calcularCosteMensual();
        }

        for (Jugador jugador : titulares) { //sumo el coste de titulares
            if (jugador != null) {
                total += jugador.calcularCosteMensual();
            }
        }

        for (Jugador jugador : suplentes) { //sumo el coste de suplentes
            total += jugador.calcularCosteMensual();
        }

        return total; //devuelvo el coste total
    }

    public double calcularValorPatrocinadores() { //suma el dinero que aportan los patrocinadores
        double total = 0; //acumulo las aportaciones

        for (Patrocinador patrocinador : patrocinadores) { //recorro cada patrocinador
            total += patrocinador.getAportacion(); //sumo su aportación
        }

        return total; //devuelvo el total de patrocinio
    }

    public double calcularRendimientoEquipo() { //calcula el rendimiento medio de titulares disponibles
        double total = 0; //acumulo rendimientos
        int contador = 0; //cuento cuántos jugadores válidos hay

        for (Jugador jugador : titulares) { //recorro los titulares
            if (jugador != null && !jugador.isSancionado()) { //solo cuentan los que pueden jugar
                total += jugador.calcularRendimiento();
                contador++;
            }
        }

        if (contador == 0) { //evito dividir entre 0 si no hay jugadores disponibles
            return 0;
        }

        return total / contador; //devuelvo la media
    }

    public void mostrarPlantilla() { //muestra entrenador, titulares, suplentes y patrocinadores del equipo
        System.out.println("Equipo: " + nombre + " (" + ciudad + ")");
        System.out.println("Presupuesto: " + presupuesto + "€");

        System.out.println("Entrenador:");
        if (entrenador != null) {
            System.out.println(entrenador);
        } else {
            System.out.println("Sin entrenador asignado.");
        }

        System.out.println("Titulares:");
        for (int i = 0; i < titulares.length; i++) {
            System.out.println(Rol.values()[i] + ": " + titulares[i]);
        }

        System.out.println("Suplentes:");
        if (suplentes.isEmpty()) {
            System.out.println("No hay suplentes.");
        } else {
            for (Jugador jugador : suplentes) {
                System.out.println(jugador);
            }
        }

        System.out.println("Patrocinadores:");
        for (Patrocinador patrocinador : patrocinadores) {
            System.out.println(patrocinador);
        }

        System.out.println("Coste total mensual: " + calcularCosteTotalEquipo() + "€");
    }

    public void sumarVictoria() { //suma una victoria al equipo
        victorias++; //aumento el contador de victorias

        if (entrenador != null) { //si hay entrenador, también se le suma la victoria
            entrenador.sumarVictoria();
        }
    }

    public void sumarDerrota() { //suma una derrota al equipo
        derrotas++; //aumento el contador de derrotas
    }

    public void sumarPuntosFavor(int puntos) { //suma puntos conseguidos por el equipo
        puntosFavor += puntos;
    }

    public void sumarPuntosContra(int puntos) { //suma puntos recibidos por el equipo
        puntosContra += puntos;
    }

    public int calcularDiferenciaPuntos() { //calcula la diferencia entre puntos a favor y en contra
        return puntosFavor - puntosContra;
    }

    public int getPuntosClasificacion() { //devuelve los puntos de liga, 3 por victoria
        return victorias * 3;
    }

    public String getNombre() { //getter del nombre
        return nombre;
    }

    public Entrenador getEntrenador() { //getter del entrenador
        return entrenador;
    }

    public int getVictorias() { //getter de victorias
        return victorias;
    }

    public int getDerrotas() { //getter de derrotas
        return derrotas;
    }

    public int getPuntosFavor() { //getter de puntos a favor
        return puntosFavor;
    }

    public int getPuntosContra() { //getter de puntos en contra
        return puntosContra;
    }

    public Jugador[] getTitulares() { //getter del array de titulares
        return titulares;
    }

    public ArrayList<Jugador> getSuplentes() { //getter de la lista de suplentes
        return suplentes;
    }

    public ArrayList<Patrocinador> getPatrocinadores() { //getter de la lista de patrocinadores
        return patrocinadores;
    }

    @Override //sobreescribo toString para mostrar el equipo resumido
    public String toString() { //devuelve una línea con datos principales del equipo
        return nombre +
                " | Ciudad: " + ciudad +
                " | Puntos: " + getPuntosClasificacion() +
                " | Victorias: " + victorias +
                " | Derrotas: " + derrotas +
                " | Dif. puntos: " + calcularDiferenciaPuntos() +
                " | Patrocinio: " + calcularValorPatrocinadores() + "€";
    }
}
