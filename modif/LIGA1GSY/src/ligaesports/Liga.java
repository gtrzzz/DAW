package ligaesports;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Liga {

    private String nombre;
    private ArrayList<PersonaLiga> personas;
    private ArrayList<Equipo> equipos;
    private ArrayList<Incidencia> incidencias;

    private ArrayList<Evento> eventosDelDia;
    private int indiceSiguienteEvento;

    private HashSet<String> idsPersonas;
    private HashSet<String> nombresEquipos;
    private HashSet<String> idsPartidos;

    private Queue<Partido> partidosPendientes;
    private Stack<String> historialAcciones;

    private Partido[][] calendario;

    public Liga(String nombre) {
        this.nombre = nombre;
        this.personas = new ArrayList<PersonaLiga>();
        this.equipos = new ArrayList<Equipo>();
        this.incidencias = new ArrayList<Incidencia>();

        this.eventosDelDia = new ArrayList<Evento>();
        this.indiceSiguienteEvento = 0;

        this.idsPersonas = new HashSet<String>();
        this.nombresEquipos = new HashSet<String>();
        this.idsPartidos = new HashSet<String>();

        this.partidosPendientes = new LinkedList<Partido>();
        this.historialAcciones = new Stack<String>();

        this.calendario = new Partido[10][5];
    }

    public void altaPersona(PersonaLiga persona) throws PersonaDuplicadaException {
        if (persona == null) {
            throw new IllegalArgumentException("La persona no puede estar vacía.");
        }

        String idNormalizado = normalizarClave(persona.getId());

        if (idsPersonas.contains(idNormalizado)) {
            throw new PersonaDuplicadaException("Ya existe una persona con el ID " + persona.getId());
        }

        personas.add(persona);
        idsPersonas.add(idNormalizado);
        registrarAccion("Alta de persona: " + persona.getNickname());
    }

    public void crearEquipo(Equipo equipo) throws EquipoDuplicadoException {
        if (equipo == null) {
            throw new IllegalArgumentException("El equipo no puede estar vacío.");
        }

        String nombreNormalizado = normalizarClave(equipo.getNombre());

        if (nombresEquipos.contains(nombreNormalizado)) {
            throw new EquipoDuplicadoException("Ya existe un equipo con el nombre " + equipo.getNombre());
        }

        equipos.add(equipo);
        nombresEquipos.add(nombreNormalizado);
        registrarAccion("Creación de equipo: " + equipo.getNombre());
    }

    public void crearPartido(String id, int jornada, Equipo local, Equipo visitante) throws PartidoInvalidoException {
        if (id == null || id.trim().equals("")) {
            throw new PartidoInvalidoException("El ID del partido no puede estar vacío.");
        }

        String idNormalizado = normalizarClave(id);

        if (idsPartidos.contains(idNormalizado)) {
            throw new PartidoInvalidoException("Ya existe un partido con el ID " + id);
        }

        if (jornada < 1 || jornada > calendario.length) {
            throw new PartidoInvalidoException("La jornada debe estar entre 1 y " + calendario.length + ".");
        }

        int fila = jornada - 1;
        int columna = buscarPrimeraColumnaLibre(fila);

        if (columna == -1) {
            throw new PartidoInvalidoException("La jornada " + jornada + " ya no tiene huecos libres.");
        }

        Partido partido = new Partido(id, jornada, local, visitante);

        idsPartidos.add(idNormalizado);
        partidosPendientes.add(partido);
        calendario[fila][columna] = partido;

        registrarAccion("Creación de partido: " + id);
    }

    private String normalizarClave(String texto) {
        return texto.trim().toLowerCase();
    }

    private int buscarPrimeraColumnaLibre(int fila) {
        if (fila < 0 || fila >= calendario.length) {
            return -1;
        }

        for (int i = 0; i < calendario[fila].length; i++) {
            if (calendario[fila][i] == null) {
                return i;
            }
        }

        return -1;
    }

    public void generarEventosAleatoriosDelDia() {
        eventosDelDia.clear();
        indiceSiguienteEvento = 0;

        ArrayList<Evento> musica = new ArrayList<Evento>();
        ArrayList<Evento> comida = new ArrayList<Evento>();
        ArrayList<Evento> show = new ArrayList<Evento>();
        ArrayList<Evento> premios = new ArrayList<Evento>();
        ArrayList<Evento> patrocinador = new ArrayList<Evento>();

        musica.add(new Evento("17:00", "Sesión DJ de HyperByte", "Música electrónica antes del segundo bloque de partidos.", CategoriaEvento.MUSICA, "HyperByte DJ Set"));
        musica.add(new Evento("17:00", "Concierto Gaming Live", "Actuación musical para animar al público.", CategoriaEvento.MUSICA, "GameFuel"));
        musica.add(new Evento("17:00", "Batalla de DJs", "Dos DJs compiten con música elegida por el público.", CategoriaEvento.MUSICA, "RedBull Arena"));
        musica.add(new Evento("17:00", "Warmup musical", "Sesión previa al partido central.", CategoriaEvento.MUSICA, "CloudPC"));

        comida.add(new Evento("19:00", "Descanso comida", "Zona de foodtrucks y descanso general de dos horas.", CategoriaEvento.COMIDA, "Foodtrucks oficiales"));
        comida.add(new Evento("19:00", "Fan Food Zone", "Menú especial para espectadores y jugadores.", CategoriaEvento.COMIDA, "GameFuel"));
        comida.add(new Evento("19:00", "Street Food Arena", "Foodtrucks temáticos dentro del recinto.", CategoriaEvento.COMIDA, "Nexus Energy"));
        comida.add(new Evento("19:00", "Descanso gastronómico", "Pausa larga para comida y descanso.", CategoriaEvento.COMIDA, "Pixel Food"));

        show.add(new Evento("18:30", "Show de luces", "Espectáculo visual antes del partido destacado.", CategoriaEvento.SHOW, "TechZone"));
        show.add(new Evento("18:30", "Cosplay Stage", "Desfile de cosplay del público.", CategoriaEvento.SHOW, "FoxWear"));
        show.add(new Evento("18:30", "Reto 1vs1 del público", "Un espectador participa en un minijuego en directo.", CategoriaEvento.SHOW, "ProGaming Store"));
        show.add(new Evento("18:30", "Karaoke gamer", "Actividad participativa para el público.", CategoriaEvento.SHOW, "CloudPC"));

        premios.add(new Evento("22:30", "Entrega de premios al mejor highlight", "Reconocimiento a la mejor jugada del día.", CategoriaEvento.PREMIOS, "RedBull Arena"));
        premios.add(new Evento("22:30", "Premio al jugador revelación", "Entrega simbólica a un jugador destacado.", CategoriaEvento.PREMIOS, "HyperByte"));
        premios.add(new Evento("22:30", "Premio al MVP de la jornada", "Reconocimiento al jugador más determinante.", CategoriaEvento.PREMIOS, "Nexus Energy"));
        premios.add(new Evento("22:30", "Premio a la mejor remontada", "Entrega especial por la jugada más emocionante.", CategoriaEvento.PREMIOS, "TechZone"));

        patrocinador.add(new Evento("15:30", "Presentación de patrocinadores", "Los patrocinadores oficiales presentan sus stands.", CategoriaEvento.PATROCINADOR, "Organización 1GSY"));
        patrocinador.add(new Evento("15:30", "Demo de setup profesional", "Exposición de periféricos y ordenadores gaming.", CategoriaEvento.PATROCINADOR, "CloudPC"));
        patrocinador.add(new Evento("15:30", "Zona de merchandising", "Presentación de camisetas y productos oficiales.", CategoriaEvento.PATROCINADOR, "FoxWear"));
        patrocinador.add(new Evento("15:30", "Experiencia energética", "Actividad promocional para el público.", CategoriaEvento.PATROCINADOR, "Nexus Energy"));

        eventosDelDia.add(new Evento("15:00", "Apertura de puertas", "Entrada del público al recinto.", CategoriaEvento.SHOW, "Organización 1GSY"));
        eventosDelDia.add(elegirEventoAleatorio(patrocinador));
        eventosDelDia.add(new Evento("16:00", "Primer partido", "Inicio oficial de la competición.", CategoriaEvento.SHOW, "Liga Nexus 1GSY"));
        eventosDelDia.add(elegirEventoAleatorio(musica));
        eventosDelDia.add(new Evento("17:30", "Segundo partido", "Nuevo enfrentamiento del torneo.", CategoriaEvento.SHOW, "Liga Nexus 1GSY"));
        eventosDelDia.add(elegirEventoAleatorio(show));
        eventosDelDia.add(elegirEventoAleatorio(comida));
        eventosDelDia.add(new Evento("21:00", "Partido destacado", "Partido principal de la jornada.", CategoriaEvento.SHOW, "Liga Nexus 1GSY"));
        eventosDelDia.add(elegirEventoAleatorio(premios));

        indiceSiguienteEvento = 3;
        registrarAccion("Generación aleatoria de eventos del día.");
    }

    private Evento elegirEventoAleatorio(ArrayList<Evento> lista) {
        Random random = new Random();
        return lista.get(random.nextInt(lista.size()));
    }

    public void mostrarResumenEventoInicial() {
        System.out.println("==============================================");
        System.out.println("RESUMEN DEL EVENTO");
        System.out.println("Nombre: " + nombre);
        System.out.println("Formato: torneo presencial de e-sports 5vs5");
        System.out.println("Duración estimada de partidos: 60 minutos");
        System.out.println("Pausas entre partidos: 30 minutos / 1 hora");
        System.out.println("Descanso comida: 2 horas con foodtrucks");
        System.out.println("Extras: música, patrocinadores, shows, premios y apoyo de comunidad");
        System.out.println("==============================================");

        mostrarEventos();
    }

    public void mostrarEventos() {
        System.out.println();
        System.out.println("HORARIO DEL EVENTO");

        for (Evento evento : eventosDelDia) {
            System.out.println("-------------------------");
            evento.mostrarEvento();
        }
    }

    public void mostrarSiguienteEventoTrasPartido() {
        if (indiceSiguienteEvento >= eventosDelDia.size()) {
            System.out.println("No quedan eventos programados.");
            return;
        }

        System.out.println();
        System.out.println("Siguiente evento programado:");
        eventosDelDia.get(indiceSiguienteEvento).mostrarEvento();
        indiceSiguienteEvento++;
    }

    public Partido obtenerSiguientePartido() {
        return partidosPendientes.peek();
    }

    public void verSiguientePartido() {
        Partido partido = partidosPendientes.peek();

        if (partido == null) {
            System.out.println("No hay partidos pendientes.");
        } else {
            partido.mostrarPartido();
        }
    }

    public void apostarAlSiguientePartido(int opcionEquipo, double cantidad) {
        Partido partido = partidosPendientes.peek();

        if (partido == null) {
            System.out.println("No hay partidos pendientes.");
            return;
        }

        if (cantidad < 0) {
            System.out.println("La cantidad no puede ser negativa.");
            return;
        }

        if (opcionEquipo == 1) {
            partido.apostarLocal(cantidad);
            registrarAccion("Apoyo económico al equipo local: " + cantidad + "€");
        } else if (opcionEquipo == 2) {
            partido.apostarVisitante(cantidad);
            registrarAccion("Apoyo económico al equipo visitante: " + cantidad + "€");
        } else {
            System.out.println("Opción de equipo no válida.");
        }

        partido.mostrarApuestas();
    }

    public void disputarSiguientePartido() {
        Partido partido = partidosPendientes.peek();

        if (partido == null) {
            System.out.println("No hay partidos pendientes.");
            return;
        }

        try {
            System.out.println();
            System.out.println("Inicio del partido:");
            partido.mostrarApuestas();

            partido.disputarAleatorio();

            partidosPendientes.poll();

            registrarAccion("Partido disputado: " + partido.getId());

            System.out.println();
            System.out.println("Resultado final del partido:");
            partido.mostrarPartido();

            mostrarSiguienteEventoTrasPartido();

            if (partidosPendientes.isEmpty()) {
                entregarPremiosFinales();
            }

        } catch (JugadorSancionadoException | RolNoDisponibleException e) {
            System.out.println("No se pudo disputar el partido: " + e.getMessage());
        }
    }

    public void mostrarPartidosPendientes() {
        if (partidosPendientes.isEmpty()) {
            System.out.println("No hay partidos pendientes.");
            return;
        }

        for (Partido partido : partidosPendientes) {
            System.out.println(partido);
        }
    }

    public void vaciarPartidosPendientes() {
        partidosPendientes.clear();
        registrarAccion("Vaciado de la cola de partidos pendientes.");
        System.out.println("Cola de partidos pendientes vaciada.");
    }

    public void registrarResultadoManualSiguientePartido(int puntosLocal, int puntosVisitante) {
        Partido partido = partidosPendientes.peek();

        if (partido == null) {
            System.out.println("No hay partidos pendientes.");
            return;
        }

        try {
            partido.registrarResultadoManual(puntosLocal, puntosVisitante);
            partidosPendientes.poll();
            registrarAccion("Resultado manual registrado: " + partido.getId());

            System.out.println("Resultado registrado correctamente.");
            partido.mostrarPartido();

            if (partidosPendientes.isEmpty()) {
                entregarPremiosFinales();
            }
        } catch (PartidoInvalidoException | JugadorSancionadoException | RolNoDisponibleException e) {
            System.out.println("No se pudo registrar el resultado: " + e.getMessage());
        }
    }

    public void registrarIncidencia(Incidencia incidencia) {
        incidencias.add(incidencia);
        incidencia.aplicarSancion();
        registrarAccion("Registro de incidencia: " + incidencia.getId());
    }

    public void listarIncidencias() {
        if (incidencias.isEmpty()) {
            System.out.println("No hay incidencias registradas.");
            return;
        }

        for (Incidencia incidencia : incidencias) {
            incidencia.mostrarIncidencia();
            System.out.println("----------------------");
        }
    }

    public void buscarIncidenciasPorEquipo(String nombreEquipo) {
        Equipo equipo = buscarEquipoPorNombre(nombreEquipo);
        boolean encontrada = false;

        if (equipo == null) {
            System.out.println("No existe ningún equipo con ese nombre.");
            return;
        }

        for (Incidencia incidencia : incidencias) {
            if (incidencia.getEquipoRelacionado() == equipo) {
                incidencia.mostrarIncidencia();
                System.out.println("----------------------");
                encontrada = true;
            }
        }

        if (!encontrada) {
            System.out.println("No hay incidencias para ese equipo.");
        }
    }

    public void buscarIncidenciasPorJugador(String idJugador) {
        Jugador jugador = buscarJugadorPorId(idJugador);
        boolean encontrada = false;

        if (jugador == null) {
            System.out.println("No existe ningún jugador con ese ID.");
            return;
        }

        for (Incidencia incidencia : incidencias) {
            if (incidencia.getJugadorRelacionado() == jugador) {
                incidencia.mostrarIncidencia();
                System.out.println("----------------------");
                encontrada = true;
            }
        }

        if (!encontrada) {
            System.out.println("No hay incidencias para ese jugador.");
        }
    }

    public void mostrarCalendario() {
        for (int i = 0; i < calendario.length; i++) {
            boolean hayPartidos = false;

            for (int j = 0; j < calendario[i].length; j++) {
                if (calendario[i][j] != null) {
                    hayPartidos = true;
                }
            }

            if (hayPartidos) {
                System.out.println("Jornada " + (i + 1));

                for (int j = 0; j < calendario[i].length; j++) {
                    if (calendario[i][j] != null) {
                        System.out.println(calendario[i][j]);
                    }
                }
            }
        }
    }

    public void mostrarJornada(int jornada) {
        if (jornada < 1 || jornada > calendario.length) {
            System.out.println("La jornada debe estar entre 1 y " + calendario.length + ".");
            return;
        }

        int fila = jornada - 1;
        boolean hayPartidos = false;

        System.out.println("Jornada " + jornada);

        for (int i = 0; i < calendario[fila].length; i++) {
            Partido partido = calendario[fila][i];

            if (partido != null) {
                partido.mostrarPartido();
                System.out.println("----------------------");
                hayPartidos = true;
            }
        }

        if (!hayPartidos) {
            System.out.println("No hay partidos en esta jornada.");
        }
    }

    public void mostrarPersonas() {
        for (PersonaLiga persona : personas) {
            System.out.println(persona);
        }
    }

    public PersonaLiga buscarPersonaPorId(String id) {
        if (id == null || id.trim().equals("")) {
            return null;
        }

        for (PersonaLiga persona : personas) {
            if (persona.getId().equalsIgnoreCase(id)) {
                return persona;
            }
        }

        return null;
    }

    public void mostrarPersonaPorId(String id) {
        PersonaLiga persona = buscarPersonaPorId(id);

        if (persona == null) {
            System.out.println("No existe ninguna persona con ese ID.");
        } else {
            persona.mostrarResumen();
        }
    }

    public void modificarDatosPersona(String id, String nombre, String nickname, int edad, double salarioBase) {
        PersonaLiga persona = buscarPersonaPorId(id);

        if (persona == null) {
            System.out.println("No existe ninguna persona con ese ID.");
            return;
        }

        try {
            persona.setNombre(nombre);
            persona.setNickname(nickname);
            persona.setEdad(edad);
            persona.setSalarioBase(salarioBase);
            registrarAccion("Modificación de persona: " + persona.getId());
            System.out.println("Persona modificada correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo modificar la persona: " + e.getMessage());
        }
    }

    public void eliminarPersona(String id) {
        PersonaLiga persona = buscarPersonaPorId(id);

        if (persona == null) {
            System.out.println("No existe ninguna persona con ese ID.");
            return;
        }

        if (persona instanceof Jugador && buscarEquipoDeJugador((Jugador) persona) != null) {
            System.out.println("No se puede eliminar: el jugador pertenece a un equipo.");
            return;
        }

        if (persona instanceof Entrenador) {
            for (Equipo equipo : equipos) {
                if (equipo.getEntrenador() == persona) {
                    System.out.println("No se puede eliminar: el entrenador está asignado a un equipo.");
                    return;
                }
            }
        }

        personas.remove(persona);
        idsPersonas.remove(normalizarClave(persona.getId()));
        registrarAccion("Eliminación de persona: " + persona.getId());
        System.out.println("Persona eliminada correctamente.");
    }

    public void mostrarJugadores() {
        for (PersonaLiga persona : personas) {
            if (persona instanceof Jugador) {
                System.out.println(persona);
            }
        }
    }

    public void mostrarEquipos() {
        for (Equipo equipo : equipos) {
            System.out.println(equipo);
        }
    }

    public void mostrarClasificacion() {
        ArrayList<Equipo> clasificacion = obtenerClasificacionOrdenada();

        System.out.println("Clasificación de " + nombre);

        for (int i = 0; i < clasificacion.size(); i++) {
            System.out.println((i + 1) + ". " + clasificacion.get(i));
        }
    }

    private ArrayList<Equipo> obtenerClasificacionOrdenada() {
        ArrayList<Equipo> clasificacion = new ArrayList<Equipo>(equipos);

        Collections.sort(clasificacion, new Comparator<Equipo>() {
            @Override
            public int compare(Equipo e1, Equipo e2) {
                if (e1.getVictorias() != e2.getVictorias()) {
                    return e2.getVictorias() - e1.getVictorias();
                }

                if (e1.calcularDiferenciaPuntos() != e2.calcularDiferenciaPuntos()) {
                    return e2.calcularDiferenciaPuntos() - e1.calcularDiferenciaPuntos();
                }

                return e1.getNombre().compareTo(e2.getNombre());
            }
        });

        return clasificacion;
    }

    public Jugador buscarJugadorPorId(String id) {
        if (id == null || id.trim().equals("")) {
            return null;
        }

        for (PersonaLiga persona : personas) {
            if (persona instanceof Jugador && persona.getId().equalsIgnoreCase(id)) {
                return (Jugador) persona;
            }
        }

        return null;
    }

    public Equipo buscarEquipoPorNombre(String nombreEquipo) {
        if (nombreEquipo == null || nombreEquipo.trim().equals("")) {
            return null;
        }

        for (Equipo equipo : equipos) {
            if (equipo.getNombre().equalsIgnoreCase(nombreEquipo)) {
                return equipo;
            }
        }

        return null;
    }

    public Equipo buscarEquipoDeJugador(Jugador jugador) {
        for (Equipo equipo : equipos) {
            if (equipo.contieneJugador(jugador)) {
                return equipo;
            }
        }

        return null;
    }

    public void sustituirTitularPorSuplente(String nombreEquipo, Rol rol, String idSuplente) {
        Equipo equipo = buscarEquipoPorNombre(nombreEquipo);
        Jugador suplente = buscarJugadorPorId(idSuplente);

        if (equipo == null) {
            System.out.println("No existe ningún equipo con ese nombre.");
            return;
        }

        if (suplente == null) {
            System.out.println("No existe ningún jugador con ese ID.");
            return;
        }

        try {
            equipo.sustituirTitularPorSuplente(rol, suplente);
            registrarAccion("Sustitución en " + equipo.getNombre() + ": rol " + rol);
            System.out.println("Sustitución realizada correctamente.");
        } catch (RolNoDisponibleException | JugadorSancionadoException | IllegalArgumentException e) {
            System.out.println("No se pudo sustituir: " + e.getMessage());
        }
    }

    public void ficharJugadorLibreComoSuplente(String idJugador, String nombreEquipo) {
        Jugador jugador = buscarJugadorPorId(idJugador);
        Equipo equipoDestino = buscarEquipoPorNombre(nombreEquipo);

        if (jugador == null) {
            System.out.println("No existe ningún jugador con ese ID.");
            return;
        }

        if (equipoDestino == null) {
            System.out.println("No existe ningún equipo con ese nombre.");
            return;
        }

        if (buscarEquipoDeJugador(jugador) != null) {
            System.out.println("Ese jugador ya pertenece a un equipo.");
            return;
        }

        equipoDestino.ficharSuplente(jugador);
        registrarAccion("Mercado: fichaje de " + jugador.getNickname() + " por " + equipoDestino.getNombre());
        System.out.println("Jugador fichado como suplente correctamente.");
    }

    public void transferirJugador(String idJugador, String nombreEquipoDestino) {
        Jugador jugador = buscarJugadorPorId(idJugador);
        Equipo equipoDestino = buscarEquipoPorNombre(nombreEquipoDestino);

        if (jugador == null) {
            System.out.println("No existe ningún jugador con ese ID.");
            return;
        }

        if (equipoDestino == null) {
            System.out.println("No existe ningún equipo con ese nombre.");
            return;
        }

        Equipo equipoOrigen = buscarEquipoDeJugador(jugador);

        if (equipoOrigen == null) {
            equipoDestino.ficharSuplente(jugador);
            System.out.println("El jugador estaba libre. Se ha fichado como suplente.");
            registrarAccion("Mercado: fichaje libre de " + jugador.getNickname());
            return;
        }

        if (equipoOrigen == equipoDestino) {
            System.out.println("El jugador ya pertenece a ese equipo.");
            return;
        }

        if (equipoOrigen.esTitular(jugador) && equipoOrigen.buscarSuplenteCompatible(jugador.getRol()) == null) {
            System.out.println("No se puede transferir: el equipo origen se quedaría sin jugador disponible para el rol " + jugador.getRol() + ".");
            return;
        }

        equipoOrigen.eliminarJugador(jugador);
        equipoDestino.ficharSuplente(jugador);

        registrarAccion("Mercado: transferencia de " + jugador.getNickname() +
                " desde " + equipoOrigen.getNombre() + " a " + equipoDestino.getNombre());

        System.out.println("Transferencia realizada correctamente. El jugador entra como suplente.");
    }

    public void entregarPremiosFinales() {
        System.out.println();
        System.out.println("==============================================");
        System.out.println("ENTREGA DE PREMIOS FINALES");
        System.out.println("==============================================");

        ArrayList<Equipo> clasificacion = obtenerClasificacionOrdenada();

        if (!clasificacion.isEmpty()) {
            System.out.println("Podio del torneo:");

            for (int i = 0; i < clasificacion.size() && i < 3; i++) {
                System.out.println((i + 1) + ". " + clasificacion.get(i).getNombre());
            }
        }

        Jugador mvpTorneo = null;
        Jugador mejorHighlight = null;

        for (PersonaLiga persona : personas) {
            if (persona instanceof Jugador) {
                Jugador jugador = (Jugador) persona;

                if (mvpTorneo == null || jugador.getPuntosMvp() > mvpTorneo.getPuntosMvp()) {
                    mvpTorneo = jugador;
                }

                if (mejorHighlight == null || jugador.getHighlightsRealizados() > mejorHighlight.getHighlightsRealizados()) {
                    mejorHighlight = jugador;
                }
            }
        }

        if (mvpTorneo != null) {
            System.out.println();
            System.out.println("Premio MVP del torneo:");
            System.out.println(mvpTorneo.getNickname() + " con " + mvpTorneo.getPuntosMvp() + " puntos MVP.");
        }

        if (mejorHighlight != null) {
            System.out.println();
            System.out.println("Entrega de premios al mejor highlight por RedBull Arena:");
            System.out.println(mejorHighlight.getNickname() + " con " +
                    mejorHighlight.getHighlightsRealizados() + " highlights.");
        }

        registrarAccion("Entrega de premios finales del torneo.");
    }

    public void registrarAccion(String accion) {
        historialAcciones.push(accion);
    }

    public void mostrarHistorial() {
        if (historialAcciones.isEmpty()) {
            System.out.println("No hay historial.");
            return;
        }

        for (int i = historialAcciones.size() - 1; i >= 0; i--) {
            System.out.println(historialAcciones.get(i));
        }
    }

    public void deshacerUltimaAccion() {
        if (historialAcciones.isEmpty()) {
            System.out.println("No hay acciones en el historial.");
        } else {
            String accion = historialAcciones.pop();
            System.out.println("Acción eliminada solo del historial: " + accion);
        }
    }

    public ArrayList<Equipo> getEquipos() {
        return equipos;
    }

    public ArrayList<PersonaLiga> getPersonas() {
        return personas;
    }
}
