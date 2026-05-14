package ligaesports;

import java.util.ArrayList;

public class Equipo {

    private String nombre;
    private String ciudad;
    private Entrenador entrenador;
    private double presupuesto;

    private int victorias;
    private int derrotas;
    private int puntosFavor;
    private int puntosContra;

    private Jugador[] titulares;
    private ArrayList<Jugador> suplentes;
    private ArrayList<Patrocinador> patrocinadores;

    public Equipo(String nombre, String ciudad, double presupuesto) {
        if (nombre == null || nombre.trim().equals("")) {
            throw new IllegalArgumentException("El nombre del equipo no puede estar vacío.");
        }

        if (ciudad == null || ciudad.trim().equals("")) {
            throw new IllegalArgumentException("La ciudad del equipo no puede estar vacía.");
        }

        if (presupuesto < 0) {
            throw new IllegalArgumentException("El presupuesto no puede ser negativo.");
        }

        this.nombre = nombre.trim();
        this.ciudad = ciudad.trim();
        this.presupuesto = presupuesto;
        this.victorias = 0;
        this.derrotas = 0;
        this.puntosFavor = 0;
        this.puntosContra = 0;
        this.titulares = new Jugador[5];
        this.suplentes = new ArrayList<Jugador>();
        this.patrocinadores = new ArrayList<Patrocinador>();
    }

    public void asignarEntrenador(Entrenador entrenador) {
        this.entrenador = entrenador;
    }

    public void ficharTitular(Jugador jugador) throws RolNoDisponibleException {
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador titular no puede estar vacío.");
        }

        int posicion = jugador.getRol().ordinal();

        if (titulares[posicion] != null) {
            throw new RolNoDisponibleException("Ya existe un titular con el rol " + jugador.getRol());
        }

        titulares[posicion] = jugador;
    }

    public void ficharSuplente(Jugador jugador) {
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador suplente no puede estar vacío.");
        }

        if (contieneJugador(jugador)) {
            System.out.println("Ese jugador ya pertenece a este equipo.");
            return;
        }

        suplentes.add(jugador);
    }

    public boolean esTitular(Jugador jugador) {
        for (Jugador titular : titulares) {
            if (titular == jugador) {
                return true;
            }
        }

        return false;
    }

    public boolean eliminarJugador(Jugador jugador) {
        for (int i = 0; i < titulares.length; i++) {
            if (titulares[i] == jugador) {
                titulares[i] = null;
                return true;
            }
        }

        return suplentes.remove(jugador);
    }

    public void sustituirTitularPorSuplente(Rol rol, Jugador suplente) throws RolNoDisponibleException, JugadorSancionadoException {
        if (rol == null) {
            throw new IllegalArgumentException("El rol no puede estar vacío.");
        }

        if (suplente == null) {
            throw new IllegalArgumentException("El suplente no puede estar vacío.");
        }

        if (suplente.getRol() != rol) {
            throw new RolNoDisponibleException("El suplente no tiene el rol " + rol + ".");
        }

        if (suplente.isSancionado()) {
            throw new JugadorSancionadoException("El suplente " + suplente.getNickname() + " está sancionado.");
        }

        if (!suplentes.contains(suplente)) {
            throw new RolNoDisponibleException("Ese jugador no está en la lista de suplentes del equipo.");
        }

        int posicion = rol.ordinal();
        Jugador titularAnterior = titulares[posicion];
        titulares[posicion] = suplente;
        suplentes.remove(suplente);

        if (titularAnterior != null) {
            suplentes.add(titularAnterior);
        }
    }

    public boolean contieneJugador(Jugador jugador) {
        for (Jugador titular : titulares) {
            if (titular == jugador) {
                return true;
            }
        }

        return suplentes.contains(jugador);
    }

    public Jugador buscarSuplenteCompatible(Rol rol) {
        for (Jugador jugador : suplentes) {
            if (jugador.getRol() == rol && !jugador.isSancionado()) {
                return jugador;
            }
        }

        return null;
    }

    public Jugador[] generarConvocatoriaValida() throws JugadorSancionadoException, RolNoDisponibleException {
        Jugador[] convocatoria = new Jugador[5];

        for (int i = 0; i < titulares.length; i++) {
            Rol rolNecesario = Rol.values()[i];
            Jugador titular = titulares[i];

            if (titular != null && !titular.isSancionado()) {
                convocatoria[i] = titular;
            } else {
                Jugador suplente = buscarSuplenteCompatible(rolNecesario);

                if (suplente != null) {
                    convocatoria[i] = suplente;
                } else {
                    if (titular != null && titular.isSancionado()) {
                        throw new JugadorSancionadoException("El titular " + titular.getNickname() +
                                " está sancionado y no hay suplente disponible para el rol " + rolNecesario);
                    } else {
                        throw new RolNoDisponibleException("Falta jugador para el rol " + rolNecesario);
                    }
                }
            }
        }

        return convocatoria;
    }

    public void validarConvocatoria() throws JugadorSancionadoException, RolNoDisponibleException {
        generarConvocatoriaValida();
    }

    public void agregarPatrocinador(Patrocinador patrocinador) {
        if (patrocinador == null) {
            throw new IllegalArgumentException("El patrocinador no puede estar vacío.");
        }

        patrocinadores.add(patrocinador);
    }

    public Patrocinador obtenerPrimerPatrocinador() {
        if (patrocinadores.isEmpty()) {
            return null;
        }

        return patrocinadores.get(0);
    }

    public double calcularCosteTotalEquipo() {
        double total = 0;

        if (entrenador != null) {
            total += entrenador.calcularCosteMensual();
        }

        for (Jugador jugador : titulares) {
            if (jugador != null) {
                total += jugador.calcularCosteMensual();
            }
        }

        for (Jugador jugador : suplentes) {
            total += jugador.calcularCosteMensual();
        }

        return total;
    }

    public double calcularValorPatrocinadores() {
        double total = 0;

        for (Patrocinador patrocinador : patrocinadores) {
            total += patrocinador.getAportacion();
        }

        return total;
    }

    public double calcularRendimientoEquipo() {
        double total = 0;
        int contador = 0;

        for (Jugador jugador : titulares) {
            if (jugador != null && !jugador.isSancionado()) {
                total += jugador.calcularRendimiento();
                contador++;
            }
        }

        if (contador == 0) {
            return 0;
        }

        return total / contador;
    }

    public void mostrarPlantilla() {
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

    public void sumarVictoria() {
        victorias++;

        if (entrenador != null) {
            entrenador.sumarVictoria();
        }
    }

    public void sumarDerrota() {
        derrotas++;
    }

    public void sumarPuntosFavor(int puntos) {
        puntosFavor += puntos;
    }

    public void sumarPuntosContra(int puntos) {
        puntosContra += puntos;
    }

    public int calcularDiferenciaPuntos() {
        return puntosFavor - puntosContra;
    }

    public String getNombre() {
        return nombre;
    }

    public Entrenador getEntrenador() {
        return entrenador;
    }

    public int getVictorias() {
        return victorias;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public int getPuntosFavor() {
        return puntosFavor;
    }

    public int getPuntosContra() {
        return puntosContra;
    }

    public Jugador[] getTitulares() {
        return titulares;
    }

    public ArrayList<Jugador> getSuplentes() {
        return suplentes;
    }

    public ArrayList<Patrocinador> getPatrocinadores() {
        return patrocinadores;
    }

    @Override
    public String toString() {
        return nombre +
                " | Ciudad: " + ciudad +
                " | Victorias: " + victorias +
                " | Derrotas: " + derrotas +
                " | Dif. puntos: " + calcularDiferenciaPuntos() +
                " | Patrocinio: " + calcularValorPatrocinadores() + "€";
    }
}
