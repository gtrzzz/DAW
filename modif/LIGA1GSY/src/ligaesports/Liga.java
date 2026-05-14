package ligaesports;

import java.util.*; //para el arraylist, hashset, linledlist, queue, stack, collections, comparator y random

public class Liga { //clase principal de la lógica de la liga, aquí se controlan equipos, personas, partidos, calendario, incidencias e historial

    private String nombre; //nombre de la liga
    private ArrayList<PersonaLiga> personas; //lista de personas registradas, puede tener jugadores y entrenadores por polimorfismo (guarda un arraylist que almacena objetos de tipo PersonaLiga)
    private ArrayList<Equipo> equipos; //lista de equipos de la liga (arraylist que almacena objetos de tipo Equipo)
    private ArrayList<Incidencia> incidencias; //lista dinámica donde se guardan incidencias y sanciones (almacena objetos de tipo Incidencia)

    private ArrayList<Evento> eventosDelDia; //lista con los eventos del día del torneo (almacena objetos de tipo Evento)
    private int indiceSiguienteEvento; //posición del siguiente evento que se va a mostrar después de un partido

    private HashSet<String> idsPersonas; //hashset para controlar que no haya ids de personas repetidos (después se explica mejor qué es un hashset, pero básicamente es una estructura que guarda solo valores únicos y permite buscar rápidamente)
    private HashSet<String> nombresEquipos; //hashset para controlar que no haya nombres de equipos repetidos
    private HashSet<String> idsPartidos; //hashset para controlar que no haya ids de partidos repetidos

    private Queue<Partido> partidosPendientes; //cola fifo donde se guardan los partidos pendientes con una queue/cola (primero en entrar, primero en salir)
    private Stack<String> historialAcciones; //pila lifo donde se guardan las acciones realizadas con una stack/pila (último en entrar, primero en salir) para poder deshacerlas si es necesario
    private Stack<String> accionesDeshacer; //segunda pila para saber cómo deshacer algunas acciones reales y no solo guardar su descripción, por ejemplo para eliminar un equipo necesito saber su nombre, pero para eliminar una persona necesito saber su id, así que en esta pila guardo esa información clave para cada acción que pueda necesitar deshacer después

    private Partido[][] calendario; //matriz bidimensional para guardar partidos por jornada


    //aquí lo he planteado de esta manera: se juegan 3 jornadas iniciales con partidos aleatorios entre los equipos, y después se clasifican los 4 mejores equipos para jugar una fase final con dos series al mejor de 3 partidos, una por el primer puesto entre el primero y el segundo clasificado, y otra por el tercer puesto entre el tercero y el cuarto clasificado
    //para controlar la fase final, guardo en variables aparte los equipos que juegan cada serie, cuántas victorias lleva cada equipo en su serie y cuántos partidos se han jugado de cada serie, así puedo saber cuándo se clasifica un equipo para el puesto final o cuándo se tiene que jugar otro partido de la serie
    private boolean faseFinalIniciada; //indica si ya ha empezado la fase final del torneo
    private boolean torneoFinalizado; //indica si el torneo ya terminó del todo
    private Equipo finalPrimeroA; //primer equipo de la serie por el primer puesto 
    private Equipo finalPrimeroB; //segundo equipo de la serie por el primer puesto
    private Equipo finalTerceroA; //primer equipo de la serie por el tercer puesto
    private Equipo finalTerceroB; //segundo equipo de la serie por el tercer puesto
    private int victoriasFinalPrimeroA; //victorias del primer equipo en la final 1º/2º
    private int victoriasFinalPrimeroB; //victorias del segundo equipo en la final 1º/2º
    private int victoriasFinalTerceroA; //victorias del primer equipo en la final 3º/4º
    private int victoriasFinalTerceroB; //victorias del segundo equipo en la final 3º/4º
    private int partidosFinalPrimero; //partidos jugados en la serie por el primer puesto
    private int partidosFinalTercero; //partidos jugados en la serie por el tercer puesto

    public Liga(String nombre) { //constructor de la liga, inicializa todas las listas y estructuras
        this.nombre = nombre; //guardo el nombre de la liga del objeto que se está creando
        this.personas = new ArrayList<PersonaLiga>(); //creo la lista de personas registradas, al principio está vacía pero se van añadiendo personas con el método altaPersona, que también se encarga de controlar que no haya ids repetidos usando el hashset idsPersonas
        this.equipos = new ArrayList<Equipo>(); //creo la lista de equipos,, al igual que en el arraylist del anterior, empezará con una lista vacía y se irán añadiendo equipos con el método crearEquipo, que también controla que no haya nombres de equipos repetidos usando el hashset nombresEquipos
        this.incidencias = new ArrayList<Incidencia>(); //creo la lista de incidencias registradas, que se irá llenando con el método registrarIncidencia cada vez que se genere una incidencia aleatoria o se registre una incidencia manualmente, y también se puede consultar con los métodos listarIncidencias, buscarIncidenciasPorEquipo y buscarIncidenciasPorJugador (cada un o explicado mejor en su propio método)

        this.eventosDelDia = new ArrayList<Evento>(); //creo la lista de eventos del día, que se llena con el método generarEventosAleatoriosDelDia mezclando eventos fijos y eventos aleatorios de diferentes categorías, y se muestra al arrancar el programa con el método mostrarResumenEventoInicial y después de cada partido con el método mostrarSiguienteEventoTrasPartido
        this.indiceSiguienteEvento = 0; //empieza apuntando al primer evento, va aumentando cada vez que se muestra un evento para saber cuál es el siguiente, y cuando se llega al final de la lista ya no quedan eventos programados

        this.idsPersonas = new HashSet<String>(); //creo el hashset de ids de personas
        this.nombresEquipos = new HashSet<String>(); //creo el hashset de nombres de equipos
        this.idsPartidos = new HashSet<String>(); //creo el hashset de ids de partidos

        this.partidosPendientes = new LinkedList<Partido>(); //uso linkedlist como implementación de queue
        this.historialAcciones = new Stack<String>(); //creo la pila del historial
        this.accionesDeshacer = new Stack<String>(); //creo la pila de deshacer

        this.calendario = new Partido[10][5]; //creo una matriz con 10 jornadas y hasta 5 partidos por jornada

        this.faseFinalIniciada = false; //al principio todavía no se ha iniciado la fase final
        this.torneoFinalizado = false; //el torneo empieza sin estar terminado
        this.finalPrimeroA = null; //todavía no hay equipos clasificados para la final 1º/2º
        this.finalPrimeroB = null;
        this.finalTerceroA = null; //todavía no hay equipos clasificados para la final 3º/4º
        this.finalTerceroB = null;
        this.victoriasFinalPrimeroA = 0; //reinicio los contadores de victorias de las series
        this.victoriasFinalPrimeroB = 0;
        this.victoriasFinalTerceroA = 0;
        this.victoriasFinalTerceroB = 0;
        this.partidosFinalPrimero = 0; //contador de partidos jugados en la serie del primer puesto
        this.partidosFinalTercero = 0; //contador de partidos jugados en la serie del tercer puesto
    }

    public void altaPersona(PersonaLiga persona) throws PersonaDuplicadaException { //método para registrar una persona en la liga, lanza la excepción propia si ya existe persona con mismo id
        if (persona == null) { //compruebo que no se pase una persona vacía
            throw new IllegalArgumentException("La persona no puede estar vacía."); //lanzo una excepción estándar de Java porque no es un caso de persona duplicada, sino de que no se ha pasado una persona válida (podría haber creado excepción propia pero en este caso me parecía más adecuado usar una excepción ya existente por simplificar código)
        }

        String idNormalizado = normalizarClave(persona.getId()); //normalizo el id para comparar sin mayúsculas ni espacios

        if (idsPersonas.contains(idNormalizado)) { //si el id ya está en el hashset, lanzo excepción
            throw new PersonaDuplicadaException("Ya existe una persona con el ID " + persona.getId()); //lanzo la excepción propia de persona duplicada con un mensaje que incluye el id que causó el problema
        }

        personas.add(persona); //añado la persona a la lista
        idsPersonas.add(idNormalizado); //añado el id al hashset para evitar duplicados
        registrarAccion("Alta de persona: " + persona.getNickname(), "ELIMINAR_PERSONA:" + persona.getId()); //registro la acción y cómo deshacerla (en este caso, para eliminar la persona solo necesito su id, así que lo guardo en la pila de deshacer con un formato que me permita identificar que es una persona y no otra cosa si luego quiero implementar una función de deshacer acciones)
    }

    public void crearEquipo(Equipo equipo) throws EquipoDuplicadoException { //método para registrar un equipo nuevo que lanza excepción propia si ya existe un equipo con el mismo nombre
        if (equipo == null) {
            throw new IllegalArgumentException("El equipo no puede estar vacío."); //lanza excepción para evitar que se intente crear un equipo sin datos, al igual que en el método de altaPersona, uso una excepción estándar porque no es un caso de equipo duplicado, sino de que no se ha pasado un equipo válido
        }

        String nombreNormalizado = normalizarClave(equipo.getNombre()); //normalizo el nombre para comparar sin problemas de mayúsculas ni espacios

        if (nombresEquipos.contains(nombreNormalizado)) {
            throw new EquipoDuplicadoException("Ya existe un equipo con el nombre " + equipo.getNombre()); //lanzo la excepción propia de equipo duplicado con un mensaje que incluye el nombre que causó el problema
        }

        equipos.add(equipo); //añado el equipo a la lista de equipos
        nombresEquipos.add(nombreNormalizado); //añado el nombre al hashset para evitar duplicados
        registrarAccion("Creación de equipo: " + equipo.getNombre(), "ELIMINAR_EQUIPO:" + equipo.getNombre()); 
    }

    public void crearPartido(String id, int jornada, Equipo local, Equipo visitante) throws PartidoInvalidoException { //método para crear un partido, guardarlo en calendario y en la cola de partidos pendientes, lanza excepción propia si el id ya existe o si la jornada no es válida
        if (id == null || id.trim().equals("")) {
            throw new PartidoInvalidoException("El ID del partido no puede estar vacío."); //si el id es nulo o vacío, lanzo una excepción propia de partido inválido con un mensaje que explique el problema
        }

        String idNormalizado = normalizarClave(id); //normalizo el id para comparar sin problemas de mayúsculas ni espacios

        if (idsPartidos.contains(idNormalizado)) { //como el resto de métodos para crear, comprueba que el id no esté repetido, si lo está lanza excepción propia 
            throw new PartidoInvalidoException("Ya existe un partido con el ID " + id);
        }

        if (jornada < 1 || jornada > calendario.length) { //para comprobar que la jornada está dentro del calendario, si no lo está lanza excepcion propia
            throw new PartidoInvalidoException("La jornada debe estar entre 1 y " + calendario.length + ".");
        }

        int fila = jornada - 1; //resto 1 porque los arrays empiezan en 0
        int columna = buscarPrimeraColumnaLibre(fila); //busco un hueco libre en esa jornada

        if (columna == -1) {
            throw new PartidoInvalidoException("La jornada " + jornada + " ya no tiene huecos libres.");
        }

        Partido partido = new Partido(id, jornada, local, visitante); //creo el objeto partido con los datos que se han pasado al método

        idsPartidos.add(idNormalizado); //guardo el id para evitar partidos repetidos
        partidosPendientes.add(partido); //añado el partido pendiente a la cola de partidos pendientes para que se pueda disputar después
        calendario[fila][columna] = partido; //también lo guardo en la matriz del calendario por simple control (me parecía interesante aplicar esto como si fuera un calendario real con jornadas y partidos asignados a cada jornada, aunque realmente el orden de los partidos en la cola de pendientes es el que controla el orden real de disputa, pero así también puedo mostrar el calendario completo si quiero o controlar que no se asignen más partidos de los que caben en cada jornada)

        registrarAccion("Creación de partido: " + id, "ELIMINAR_PARTIDO:" + id); //registro la acción de crear el partido y cómo deshacerla (para eliminar el partido solo necesito su id, así que lo guardo en la pila de deshacer con un formato que me permita identificar que es un partido y no otra cosa si luego quiero implementar una función de deshacer acciones)
    }

    private String normalizarClave(String texto) { //método para comparar textos sin problemas de mayúsculas o espacios (para controlar que no haya ids de personas repetidos, ni nombres de equipos repetidos, ni ids de partidos repetidos, uso este método para normalizar el texto y compararlo sin que afecten las mayúsculas o los espacios al principio o al final, así puedo evitar problemas como que se intente registrar un equipo llamado "los diozes" y otro llamado "LOS DioZES   " y el sistema los considere diferentes por las mayúsculas y el espacio, cuando realmente deberían ser el mismo equipo) 
        return texto.trim().toLowerCase(); //quito espacios y paso a minúsculas
    }

    private int buscarPrimeraColumnaLibre(int fila) { //busca el primer hueco libre de una jornada en la matriz 
        if (fila < 0 || fila >= calendario.length) {
            return -1; //si no es válida pongo -1 para así asegurarme de que el programa no encuentre hueco libre en una fila que no existe y no intente guardar un partido fuera de los límites del calendario, además el método crearPartido ya comprueba que la jornada es válida antes de llamar a este método, así que esto es una medida extra de seguridad para evitar errores por si se llama a este método desde otro sitio sin comprobar la jornada antes
        }

        for (int i = 0; i < calendario[fila].length; i++) { //recorre columnas de la fila de la jornada buscando un hueco libre (que sería un valor null en la matriz)
            if (calendario[fila][i] == null) {
                return i; //si encuentra un hueco libre, devuelve la posición de la columna para que el método crearPartido sepa dónde guardar el partido en la matriz
            }
        }

        return -1;  //si no encuentra ningún hueco libre, devuelve -1 para que el método crearPartido sepa que no se pueden asignar más partidos a esa jornada
    }

    public void generarEventosAleatoriosDelDia() { //genera un horario de eventos mezclando algunos eventos aleatorios (hay varias categorías)
        eventosDelDia.clear(); //limpio eventos anteriores por si se vuelve a generar
        indiceSiguienteEvento = 0; //reinicio el índice del siguiente evento

        ArrayList<Evento> musica = new ArrayList<Evento>(); //creo listas separadas por categoría para elegir eventos aleatorios
        ArrayList<Evento> comida = new ArrayList<Evento>();
        ArrayList<Evento> show = new ArrayList<Evento>();
        ArrayList<Evento> premios = new ArrayList<Evento>();
        ArrayList<Evento> patrocinador = new ArrayList<Evento>();

        musica.add(new Evento("17:00", "Sesión DJ de Bizarrap", "Música electrónica antes del segundo bloque de partidos.", CategoriaEvento.MUSICA, "BZRP Stage")); //añado varios eventos de ejemplo a cada categoría, con un nombre, una descripción, una categoría y un patrocinador ficticio, para luego elegir algunos de forma aleatoria y mezclarlos con eventos fijos como la apertura de puertas o los partidos en sí
        musica.add(new Evento("17:00", "Concierto Gaming Live", "Actuación musical para animar al público.", CategoriaEvento.MUSICA, "GameFuel"));
        musica.add(new Evento("17:00", "Batalla de DJs", "Dos DJs compiten con música elegida por el público.", CategoriaEvento.MUSICA, "RedBull Arena"));
        musica.add(new Evento("17:00", "Warmup musical", "Sesión previa al partido central.", CategoriaEvento.MUSICA, "CloudPC"));

        comida.add(new Evento("19:00", "Descanso comida", "Zona de foodtrucks y descanso general de dos horas.", CategoriaEvento.COMIDA, "Foodtrucks oficiales"));
        comida.add(new Evento("19:00", "Fan Food Zone", "Menú especial para espectadores y jugadores.", CategoriaEvento.COMIDA, "GameFuel"));
        comida.add(new Evento("19:00", "Champion's Burger", "Zona de las mejores smash burgers.", CategoriaEvento.COMIDA, "CHAMPION'S BURGER"));
        comida.add(new Evento("19:00", "Descanso gastronómico", "Pausa larga para comida y descanso.", CategoriaEvento.COMIDA, "Arguiñano Food Truck"));

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

        eventosDelDia.add(new Evento("15:00", "Apertura de puertas", "Entrada del público al recinto.", CategoriaEvento.SHOW, "Organización 1GSY")); //añado eventos fijos y otros aleatorios
        eventosDelDia.add(elegirEventoAleatorio(patrocinador));
        eventosDelDia.add(new Evento("16:00", "Primer partido", "Inicio oficial de la competición.", CategoriaEvento.SHOW, "GSY ESPORTS"));
        eventosDelDia.add(elegirEventoAleatorio(musica));
        eventosDelDia.add(new Evento("17:30", "Segundo partido", "Nuevo enfrentamiento del torneo.", CategoriaEvento.SHOW, "GSY ESPORTS"));
        eventosDelDia.add(elegirEventoAleatorio(show));
        eventosDelDia.add(elegirEventoAleatorio(comida));
        eventosDelDia.add(new Evento("21:00", "Partido destacado", "Partido principal de la jornada.", CategoriaEvento.SHOW, "GSY ESPORTS"));
        eventosDelDia.add(elegirEventoAleatorio(premios));

        indiceSiguienteEvento = 3; //lo dejo en 3 para que después del primer partido se muestre el evento que toca
        registrarAccion("Generación aleatoria de eventos del día."); //guardo la acción en el historial
    }

    private Evento elegirEventoAleatorio(ArrayList<Evento> lista) { //elige un evento al azar de una lista
        Random random = new Random();
        return lista.get(random.nextInt(lista.size()));
    }

    public void mostrarResumenEventoInicial() { //muestra un resumen general del evento al arrancar el programa
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

    public void mostrarEventos() { //muestra todos los eventos del día
        System.out.println();
        System.out.println("HORARIO DEL EVENTO");

        for (Evento evento : eventosDelDia) { //recorre lista de eventos y muestra info 
            System.out.println("-------------------------");
            evento.mostrarEvento(); 
        }
    }

    public void mostrarSiguienteEventoTrasPartido() { //muestra el siguiente evento después de disputar un partido
        if (indiceSiguienteEvento >= eventosDelDia.size()) { //si ya se mostraron todos los eventos, aviso de que no quedan más eventos programados
            System.out.println("No quedan eventos programados.");
            return;
        }

        System.out.println();
        System.out.println("Siguiente evento programado:");
        eventosDelDia.get(indiceSiguienteEvento).mostrarEvento();
        indiceSiguienteEvento++;
    }

    public Partido obtenerSiguientePartido() { //devuelve el siguiente partido pendiente sin quitarlo de la cola
        return partidosPendientes.peek(); //peek mira el primero de la cola pero no lo elimina 
    }

    public void verSiguientePartido() { //muestra el primer partido de la cola sin sacarlo
        Partido partido = partidosPendientes.peek(); //miro el siguiente partido pendiente de la cola

        if (partido == null) {
            System.out.println("No hay partidos pendientes.");
        } else {
            partido.mostrarPartido(); 
        }
    }

    public void apostarAlSiguientePartido(int opcionEquipo, double cantidad) { //añade apoyo económico al siguiente partido
        Partido partido = partidosPendientes.peek(); //cojo el partido que se jugaría ahora 

        if (partido == null) {
            System.out.println("No hay partidos pendientes.");
            return; //si no hay partidos pendientes, no se puede apostar, así que aviso y salgo del método
        }

        if (cantidad < 0) {
            System.out.println("La cantidad no puede ser negativa.");
            return; //si la cantidad es negativa no se hace así que aviso y salgo del método
        }

        if (opcionEquipo == 1) {
            partido.apostarLocal(cantidad); //sumo la cantidad al equipo local
            registrarAccion("Apoyo económico al equipo local: " + cantidad + "€"); //suma la cantidad que hayas escrito por teclado a la cantidad apostada al equipo (en este caso local pero lo mismo para visitante, solo habria que poner la opción 2)
        } else if (opcionEquipo == 2) {
            partido.apostarVisitante(cantidad); //sumo la cantidad al equipo visitante
            registrarAccion("Apoyo económico al equipo visitante: " + cantidad + "€");
        } else {
            System.out.println("Opción de equipo no válida.");
        }

        partido.mostrarApuestas();
    }

    public void disputarSiguientePartido() { //disputa automáticamente el siguiente partido pendiente
        Partido partido = partidosPendientes.peek(); //miro el primer partido de la cola (mira pero no elimina)

        if (partido == null) {
            System.out.println("No hay partidos pendientes.");
            return;
        }

        try {
            System.out.println();
            System.out.println("Inicio del partido:");
            partido.mostrarApuestas();

            partido.disputarAleatorio(); //genera resultado, estadísticas, mvp y highlight

            partidosPendientes.poll(); //quito el partido de la cola porque ya se ha jugado, antes había usado remove pero me daba error porque si la cola está vacía lanza excepción y poll devuelve null

            registrarAccion("Partido disputado: " + partido.getId());

            System.out.println();
            System.out.println("Resultado final del partido:");
            partido.mostrarPartido();

            mostrarSiguienteEventoTrasPartido();

            procesarPartidoFinalizado(partido); //reviso incidencias, fase final y premios si corresponde

        } catch (JugadorSancionadoException | RolNoDisponibleException e) {
            System.out.println("No se pudo disputar el partido: " + e.getMessage());
        }
    }

    public void mostrarPartidosPendientes() { //recorre la cola y muestra todos los partidos pendientes
        if (partidosPendientes.isEmpty()) {
            System.out.println("No hay partidos pendientes.");
            return;
        }

        for (Partido partido : partidosPendientes) {
            System.out.println(partido);
        }
    }

    public void vaciarPartidosPendientes() { //borra todos los partidos que siguen pendientes
        partidosPendientes.clear(); //vacío la cola completa
        registrarAccion("Vaciado de la cola de partidos pendientes.");
        System.out.println("Cola de partidos pendientes vaciada.");
    }

    public void registrarResultadoManualSiguientePartido(int puntosLocal, int puntosVisitante) { //permite meter un resultado a mano
        Partido partido = partidosPendientes.peek(); //el resultado manual se aplica al siguiente partido

        if (partido == null) {
            System.out.println("No hay partidos pendientes.");
            return;
        }

        try {
            partido.registrarResultadoManual(puntosLocal, puntosVisitante); //guardo puntos y estadísticas desde el método de partido
            partidosPendientes.poll(); //lo quito de pendientes porque ya tiene resultado
            registrarAccion("Resultado manual registrado: " + partido.getId());

            System.out.println("Resultado registrado correctamente.");
            partido.mostrarPartido();

            procesarPartidoFinalizado(partido); //después del resultado hago las mismas comprobaciones que en un partido automático
        } catch (PartidoInvalidoException | JugadorSancionadoException | RolNoDisponibleException e) {
            System.out.println("No se pudo registrar el resultado: " + e.getMessage());
        }
    }

    private void procesarPartidoFinalizado(Partido partido) { //centraliza lo que pasa justo después de terminar un partido
        generarIncidenciaAleatoria(partido); //puede crear una incidencia, pero no siempre

        if (esPartidoFaseFinal(partido)) { //si era de final, actualizo la serie al mejor de 3
            actualizarSerieFaseFinal(partido);
        }

        if (partidosPendientes.isEmpty()) { //cuando ya no quedan partidos miro si toca avanzar de fase
            if (!faseFinalIniciada) {
                iniciarFaseFinal(); //al terminar las 3 jornadas iniciales empieza la fase final
            } else if (faseFinalCompletada() && !torneoFinalizado) {
                torneoFinalizado = true;
                entregarPremiosFinales(); //si ya acabaron las dos series, muestro premios finales
            }
        }
    }

    private boolean esPartidoFaseFinal(Partido partido) { //comprueba si el id corresponde a una de las dos series finales
        return partido.getId().startsWith("FINAL12-") || partido.getId().startsWith("FINAL34-");
    }

    private void iniciarFaseFinal() { //prepara la fase final con los 4 mejores equipos
        ArrayList<Equipo> clasificacion = obtenerClasificacionOrdenada(); //ordeno equipos por puntos y desempates

        if (clasificacion.size() < 4) {
            System.out.println("No hay equipos suficientes para iniciar la fase final.");
            torneoFinalizado = true;
            entregarPremiosFinales();
            return;
        }

        faseFinalIniciada = true; //marco que ya no estamos en liga regular
        finalPrimeroA = clasificacion.get(0); //el primero juega contra el segundo por el título
        finalPrimeroB = clasificacion.get(1);
        finalTerceroA = clasificacion.get(2); //el tercero juega contra el cuarto por el tercer puesto
        finalTerceroB = clasificacion.get(3);

        System.out.println();
        System.out.println("==============================================");
        System.out.println("FIN DE LAS 3 JORNADAS INICIALES");
        System.out.println("Se clasifican los 4 primeros equipos por puntos.");
        System.out.println("1º vs 2º jugarán por el primer y segundo puesto al mejor de 3.");
        System.out.println("3º vs 4º jugarán por el tercer y cuarto puesto al mejor de 3.");
        System.out.println("==============================================");

        for (int i = 0; i < clasificacion.size(); i++) {
            String estado = i < 4 ? "CLASIFICADO" : "ELIMINADO";
            System.out.println((i + 1) + ". " + clasificacion.get(i).getNombre() + " - " + estado);
        }

        encolarSiguientePartidoFinalPrimero(); //encolo el primer partido de la serie 1º/2º
        encolarSiguientePartidoFinalTercero(); //encolo el primer partido de la serie 3º/4º
        registrarAccion("Inicio de fase final con los 4 mejores equipos.");
    }

    private void encolarSiguientePartidoFinalPrimero() { //crea el siguiente partido de la serie por el primer puesto
        if (victoriasFinalPrimeroA == 2 || victoriasFinalPrimeroB == 2 || partidosFinalPrimero == 3) {
            return;
        }

        partidosFinalPrimero++; //aumento el número de partido de esta serie
        crearPartidoFaseFinal("FINAL12-" + partidosFinalPrimero, 3 + partidosFinalPrimero, finalPrimeroA, finalPrimeroB);
    }

    private void encolarSiguientePartidoFinalTercero() { //crea el siguiente partido de la serie por el tercer puesto
        if (victoriasFinalTerceroA == 2 || victoriasFinalTerceroB == 2 || partidosFinalTercero == 3) {
            return;
        }

        partidosFinalTercero++; //aumento el número de partido de esta serie
        crearPartidoFaseFinal("FINAL34-" + partidosFinalTercero, 3 + partidosFinalTercero, finalTerceroA, finalTerceroB);
    }

    private void crearPartidoFaseFinal(String id, int jornada, Equipo local, Equipo visitante) { //crea un partido final reutilizando crearPartido
        try {
            crearPartido(id, jornada, local, visitante);
            registrarAccion("Partido de fase final encolado: " + id);
        } catch (PartidoInvalidoException e) {
            System.out.println("No se pudo crear el partido de fase final: " + e.getMessage());
        }
    }

    private void actualizarSerieFaseFinal(Partido partido) { //suma la victoria de una serie final y decide si se juega otro partido
        Equipo ganador = partido.calcularGanador(); //calculo quién ganó el partido

        if (ganador == null) {
            return;
        }

        if (partido.getId().startsWith("FINAL12-")) { //serie del primer y segundo puesto
            if (ganador == finalPrimeroA) {
                victoriasFinalPrimeroA++;
            } else {
                victoriasFinalPrimeroB++;
            }

            System.out.println("Serie 1º/2º: " + finalPrimeroA.getNombre() + " " + victoriasFinalPrimeroA + " - " + victoriasFinalPrimeroB + " " + finalPrimeroB.getNombre());
            encolarSiguientePartidoFinalPrimero(); //si nadie llegó a 2 victorias, se encola otro
        } else if (partido.getId().startsWith("FINAL34-")) { //serie del tercer y cuarto puesto
            if (ganador == finalTerceroA) {
                victoriasFinalTerceroA++;
            } else {
                victoriasFinalTerceroB++;
            }

            System.out.println("Serie 3º/4º: " + finalTerceroA.getNombre() + " " + victoriasFinalTerceroA + " - " + victoriasFinalTerceroB + " " + finalTerceroB.getNombre());
            encolarSiguientePartidoFinalTercero(); //si nadie llegó a 2 victorias, se encola otro
        }
    }

    private boolean faseFinalCompletada() { //devuelve true cuando las dos series finales ya terminaron
        return (victoriasFinalPrimeroA == 2 || victoriasFinalPrimeroB == 2 || partidosFinalPrimero == 3) &&
                (victoriasFinalTerceroA == 2 || victoriasFinalTerceroB == 2 || partidosFinalTercero == 3);
    }

    private void generarIncidenciaAleatoria(Partido partido) { //genera incidencias aleatorias después de algunos partidos
        Random random = new Random();
        int probabilidad = random.nextInt(100); //uso un número de 0 a 99 para controlar porcentajes

        if (probabilidad < 65) { //la mayoría de partidos no generan incidencias
            return;
        }

        Equipo equipo = random.nextBoolean() ? partido.getLocal() : partido.getVisitante(); //elijo uno de los dos equipos
        Jugador jugador = elegirJugadorAleatorio(equipo); //puede ser necesario si la incidencia afecta a un jugador
        TipoIncidencia tipo;
        String descripcion;

        if (probabilidad < 78) {
            jugador = elegirJugadorSancionable(equipo); //evito sancionar titulares si no hay suplente compatible

            if (jugador == null) {
                return;
            }

            tipo = TipoIncidencia.SANCION;
            descripcion = "Sanción aleatoria tras revisión arbitral del partido " + partido.getId() + ".";
        } else if (probabilidad < 88) {
            tipo = TipoIncidencia.ERROR_TECNICO;
            descripcion = "Problema técnico detectado durante el partido " + partido.getId() + ".";
            jugador = null;
        } else if (probabilidad < 95 && jugador != null) {
            tipo = TipoIncidencia.EXPULSION;
            descripcion = "Expulsión registrada por conducta antideportiva en el partido " + partido.getId() + ".";
        } else {
            tipo = TipoIncidencia.OTRO;
            descripcion = "Incidencia administrativa revisada tras el partido " + partido.getId() + ".";
            jugador = null;
        }

        Incidencia incidencia = new Incidencia("INC-" + partido.getId() + "-" + (incidencias.size() + 1), tipo, descripcion, equipo, jugador); //creo un id único usando el partido
        registrarIncidencia(incidencia); //la guardo y aplico la sanción si procede

        System.out.println();
        System.out.println("Incidencia aleatoria generada:");
        incidencia.mostrarIncidencia();
    }

    private Jugador elegirJugadorAleatorio(Equipo equipo) { //elige cualquier jugador del equipo para incidencias no críticas
        ArrayList<Jugador> jugadoresDisponibles = new ArrayList<Jugador>(); //junto titulares y suplentes en una lista

        for (Jugador jugador : equipo.getTitulares()) {
            if (jugador != null) {
                jugadoresDisponibles.add(jugador);
            }
        }

        for (Jugador jugador : equipo.getSuplentes()) {
            jugadoresDisponibles.add(jugador);
        }

        if (jugadoresDisponibles.isEmpty()) {
            return null;
        }

        Random random = new Random();
        return jugadoresDisponibles.get(random.nextInt(jugadoresDisponibles.size()));
    }

    private Jugador elegirJugadorSancionable(Equipo equipo) { //elige un jugador que se pueda sancionar sin bloquear el equipo
        ArrayList<Jugador> candidatos = new ArrayList<Jugador>(); //aquí guardo solo jugadores válidos para sanción

        for (Jugador jugador : equipo.getTitulares()) {
            if (jugador != null && !jugador.isSancionado() && equipo.buscarSuplenteCompatible(jugador.getRol()) != null) {
                candidatos.add(jugador);
            }
        }

        for (Jugador jugador : equipo.getSuplentes()) {
            if (!jugador.isSancionado()) {
                candidatos.add(jugador);
            }
        }

        if (candidatos.isEmpty()) {
            return null;
        }

        Random random = new Random();
        return candidatos.get(random.nextInt(candidatos.size()));
    }

    public void registrarIncidencia(Incidencia incidencia) { //guarda una incidencia y aplica su efecto
        incidencias.add(incidencia); //añado la incidencia al listado
        incidencia.aplicarSancion(); //si es sanción o expulsión, el jugador queda sancionado
        registrarAccion("Registro de incidencia: " + incidencia.getId());
    }

    public void listarIncidencias() { //muestra todas las incidencias registradas
        if (incidencias.isEmpty()) {
            System.out.println("No hay incidencias registradas.");
            return;
        }

        for (Incidencia incidencia : incidencias) {
            incidencia.mostrarIncidencia();
            System.out.println("----------------------");
        }
    }

    public void buscarIncidenciasPorEquipo(String nombreEquipo) { //filtra incidencias usando el nombre del equipo
        Equipo equipo = buscarEquipoPorNombre(nombreEquipo); //primero busco el equipo real
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

    public void buscarIncidenciasPorJugador(String idJugador) { //filtra incidencias usando el id del jugador
        Jugador jugador = buscarJugadorPorId(idJugador); //primero busco el jugador real
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

    public void mostrarCalendario() { //recorre la matriz del calendario y muestra jornadas con partidos
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

    public void mostrarJornada(int jornada) { //muestra solo los partidos de una jornada concreta
        if (jornada < 1 || jornada > calendario.length) {
            System.out.println("La jornada debe estar entre 1 y " + calendario.length + ".");
            return;
        }

        int fila = jornada - 1; //ajusto porque la matriz empieza en 0
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

    public void mostrarPersonas() { //muestra todas las personas registradas
        for (PersonaLiga persona : personas) {
            System.out.println(persona);
        }
    }

    public PersonaLiga buscarPersonaPorId(String id) { //busca cualquier persona, sea jugador o entrenador
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

    public void mostrarPersonaPorId(String id) { //muestra el resumen de una persona concreta
        PersonaLiga persona = buscarPersonaPorId(id); //reutilizo la búsqueda general por id

        if (persona == null) {
            System.out.println("No existe ninguna persona con ese ID.");
        } else {
            persona.mostrarResumen();
        }
    }

    public void modificarDatosPersona(String id, String nombre, String nickname, int edad, double salarioBase) { //modifica datos básicos de una persona
        PersonaLiga persona = buscarPersonaPorId(id); //busco la persona que se quiere cambiar

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

    public void eliminarPersona(String id) { //elimina una persona si no está vinculada a un equipo
        PersonaLiga persona = buscarPersonaPorId(id); //primero compruebo que exista

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

        personas.remove(persona); //elimino de la lista principal
        idsPersonas.remove(normalizarClave(persona.getId())); //también libero el id en el hashset
        registrarAccion("Eliminación de persona: " + persona.getId());
        System.out.println("Persona eliminada correctamente.");
    }

    public void mostrarJugadores() { //muestra solo personas que sean jugadores
        for (PersonaLiga persona : personas) {
            if (persona instanceof Jugador) {
                System.out.println(persona);
            }
        }
    }

    public void mostrarEquipos() { //muestra todos los equipos registrados
        for (Equipo equipo : equipos) {
            System.out.println(equipo);
        }
    }

    public void mostrarClasificacion() { //muestra la tabla ordenada de equipos
        ArrayList<Equipo> clasificacion = obtenerClasificacionOrdenada(); //obtengo una copia ordenada

        System.out.println("Clasificación de " + nombre);

        for (int i = 0; i < clasificacion.size(); i++) {
            System.out.println((i + 1) + ". " + clasificacion.get(i));
        }
    }

    private ArrayList<Equipo> obtenerClasificacionOrdenada() { //ordena equipos por puntos, diferencia y nombre
        ArrayList<Equipo> clasificacion = new ArrayList<Equipo>(equipos); //hago copia para no alterar la lista original

        Collections.sort(clasificacion, new Comparator<Equipo>() {
            @Override
            public int compare(Equipo e1, Equipo e2) {
                if (e1.getPuntosClasificacion() != e2.getPuntosClasificacion()) { //primero comparo los puntos de liga
                    return e2.getPuntosClasificacion() - e1.getPuntosClasificacion();
                }

                if (e1.calcularDiferenciaPuntos() != e2.calcularDiferenciaPuntos()) { //si empatan, uso la diferencia de puntos
                    return e2.calcularDiferenciaPuntos() - e1.calcularDiferenciaPuntos();
                }

                return e1.getNombre().compareTo(e2.getNombre()); //si sigue el empate, ordeno por nombre
            }
        });

        return clasificacion;
    }

    public Jugador buscarJugadorPorId(String id) { //busca solo jugadores por id
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

    public Equipo buscarEquipoPorNombre(String nombreEquipo) { //busca un equipo por nombre ignorando mayúsculas
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

    public Equipo buscarEquipoDeJugador(Jugador jugador) { //devuelve el equipo al que pertenece un jugador
        for (Equipo equipo : equipos) {
            if (equipo.contieneJugador(jugador)) {
                return equipo;
            }
        }

        return null;
    }

    public void sustituirTitularPorSuplente(String nombreEquipo, Rol rol, String idSuplente) { //hace una sustitución manual por rol
        Equipo equipo = buscarEquipoPorNombre(nombreEquipo); //busco el equipo indicado
        Jugador suplente = buscarJugadorPorId(idSuplente); //busco el suplente que quiere entrar

        if (equipo == null) {
            System.out.println("No existe ningún equipo con ese nombre.");
            return;
        }

        if (suplente == null) {
            System.out.println("No existe ningún jugador con ese ID.");
            return;
        }

        try {
            equipo.sustituirTitularPorSuplente(rol, suplente); //la lógica de intercambio está en equipo
            registrarAccion("Sustitución en " + equipo.getNombre() + ": rol " + rol);
            System.out.println("Sustitución realizada correctamente.");
        } catch (RolNoDisponibleException | JugadorSancionadoException | IllegalArgumentException e) {
            System.out.println("No se pudo sustituir: " + e.getMessage());
        }
    }

    public void ficharJugadorLibreComoSuplente(String idJugador, String nombreEquipo) { //ficha a un jugador libre como suplente
        Jugador jugador = buscarJugadorPorId(idJugador); //busco el jugador candidato
        Equipo equipoDestino = buscarEquipoPorNombre(nombreEquipo); //busco el equipo destino

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

        equipoDestino.ficharSuplente(jugador); //entra como suplente para no romper titulares
        registrarAccion("Mercado: fichaje de " + jugador.getNickname() + " por " + equipoDestino.getNombre());
        System.out.println("Jugador fichado como suplente correctamente.");
    }

    public void transferirJugador(String idJugador, String nombreEquipoDestino) { //mueve un jugador de un equipo a otro
        Jugador jugador = buscarJugadorPorId(idJugador); //busco el jugador que se quiere transferir
        Equipo equipoDestino = buscarEquipoPorNombre(nombreEquipoDestino); //busco el equipo que lo recibirá

        if (jugador == null) {
            System.out.println("No existe ningún jugador con ese ID.");
            return;
        }

        if (equipoDestino == null) {
            System.out.println("No existe ningún equipo con ese nombre.");
            return;
        }

        Equipo equipoOrigen = buscarEquipoDeJugador(jugador); //puede ser null si está libre

        if (equipoOrigen == null) {
            equipoDestino.ficharSuplente(jugador); //si estaba libre, simplemente se ficha como suplente
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

        equipoOrigen.eliminarJugador(jugador); //lo saco del equipo origen
        equipoDestino.ficharSuplente(jugador); //lo meto como suplente en el destino

        registrarAccion("Mercado: transferencia de " + jugador.getNickname() +
                " desde " + equipoOrigen.getNombre() + " a " + equipoDestino.getNombre());

        System.out.println("Transferencia realizada correctamente. El jugador entra como suplente.");
    }

    public void entregarPremiosFinales() { //muestra premios y puestos finales del torneo
        System.out.println();
        System.out.println("==============================================");
        System.out.println("ENTREGA DE PREMIOS FINALES");
        System.out.println("==============================================");

        if (faseFinalCompletada()) {
            Equipo primero = obtenerGanadorSerie(finalPrimeroA, finalPrimeroB, victoriasFinalPrimeroA, victoriasFinalPrimeroB); //ganador de la serie por el título
            Equipo segundo = obtenerPerdedorSerie(finalPrimeroA, finalPrimeroB, victoriasFinalPrimeroA, victoriasFinalPrimeroB);
            Equipo tercero = obtenerGanadorSerie(finalTerceroA, finalTerceroB, victoriasFinalTerceroA, victoriasFinalTerceroB);
            Equipo cuarto = obtenerPerdedorSerie(finalTerceroA, finalTerceroB, victoriasFinalTerceroA, victoriasFinalTerceroB);

            System.out.println("Podio y puestos finales del torneo:");
            System.out.println("1. " + primero.getNombre());
            System.out.println("2. " + segundo.getNombre());
            System.out.println("3. " + tercero.getNombre());
            System.out.println("4. " + cuarto.getNombre());
        } else {
            ArrayList<Equipo> clasificacion = obtenerClasificacionOrdenada(); //si no hubo fase final completa, uso la clasificación general

            if (!clasificacion.isEmpty()) {
                System.out.println("Podio del torneo:");

                for (int i = 0; i < clasificacion.size() && i < 3; i++) {
                    System.out.println((i + 1) + ". " + clasificacion.get(i).getNombre());
                }
            }
        }

        Jugador mvpTorneo = null; //guardará el jugador con más puntos mvp
        Jugador mejorHighlight = null; //guardará el jugador con más highlights

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

    private Equipo obtenerGanadorSerie(Equipo equipoA, Equipo equipoB, int victoriasA, int victoriasB) { //devuelve quién ganó una serie final
        if (victoriasA >= victoriasB) {
            return equipoA;
        }

        return equipoB;
    }

    private Equipo obtenerPerdedorSerie(Equipo equipoA, Equipo equipoB, int victoriasA, int victoriasB) { //devuelve quién perdió una serie final
        if (victoriasA >= victoriasB) {
            return equipoB;
        }

        return equipoA;
    }

    public void registrarAccion(String accion) { //registra una acción sin deshacer real
        historialAcciones.push(accion); //guardo el texto en la pila del historial
        accionesDeshacer.push("SIN_DESHACER"); //marco que solo se puede quitar del historial
    }

    public void registrarAccion(String accion, String accionDeshacer) { //registra una acción y la orden para deshacerla
        historialAcciones.push(accion); //guardo lo que se hizo
        accionesDeshacer.push(accionDeshacer); //guardo cómo deshacerlo si se puede
    }

    public void mostrarHistorial() { //muestra las acciones desde la más reciente hasta la más antigua
        if (historialAcciones.isEmpty()) {
            System.out.println("No hay historial.");
            return;
        }

        for (int i = historialAcciones.size() - 1; i >= 0; i--) {
            System.out.println(historialAcciones.get(i));
        }
    }

    public void deshacerUltimaAccion() { //intenta deshacer la última acción registrada
        if (historialAcciones.isEmpty()) {
            System.out.println("No hay acciones en el historial.");
        } else {
            String accion = historialAcciones.pop(); //saco la acción visible
            String accionDeshacer = accionesDeshacer.pop(); //saco también la orden de deshacer

            if (accionDeshacer.equals("SIN_DESHACER")) {
                System.out.println("Acción eliminada solo del historial: " + accion);
                System.out.println("Esta operación no tiene deshacer real implementado.");
            } else if (accionDeshacer.startsWith("ELIMINAR_PERSONA:")) {
                deshacerAltaPersona(accionDeshacer.substring("ELIMINAR_PERSONA:".length()));
            } else if (accionDeshacer.startsWith("ELIMINAR_EQUIPO:")) {
                deshacerCreacionEquipo(accionDeshacer.substring("ELIMINAR_EQUIPO:".length()));
            } else if (accionDeshacer.startsWith("ELIMINAR_PARTIDO:")) {
                deshacerCreacionPartido(accionDeshacer.substring("ELIMINAR_PARTIDO:".length()));
            }
        }
    }

    private void deshacerAltaPersona(String id) { //deshace un alta eliminando la persona si sigue libre
        PersonaLiga persona = buscarPersonaPorId(id); //busco la persona que se dio de alta

        if (persona == null) {
            System.out.println("No se pudo deshacer: la persona ya no existe.");
            return;
        }

        if (persona instanceof Jugador && buscarEquipoDeJugador((Jugador) persona) != null) {
            System.out.println("No se pudo deshacer: el jugador ya pertenece a un equipo.");
            return;
        }

        if (persona instanceof Entrenador) {
            for (Equipo equipo : equipos) {
                if (equipo.getEntrenador() == persona) {
                    System.out.println("No se pudo deshacer: el entrenador ya está asignado a un equipo.");
                    return;
                }
            }
        }

        personas.remove(persona); //la quito de la lista
        idsPersonas.remove(normalizarClave(persona.getId())); //libero su id para poder reutilizarlo
        System.out.println("Deshacer realizado: persona eliminada realmente.");
    }

    private void deshacerCreacionEquipo(String nombreEquipo) { //deshace la creación de un equipo si no tiene partidos pendientes
        Equipo equipo = buscarEquipoPorNombre(nombreEquipo); //busco el equipo que habría que eliminar

        if (equipo == null) {
            System.out.println("No se pudo deshacer: el equipo ya no existe.");
            return;
        }

        for (Partido partido : partidosPendientes) {
            if (partido.getLocal() == equipo || partido.getVisitante() == equipo) {
                System.out.println("No se pudo deshacer: el equipo ya tiene partidos pendientes.");
                return;
            }
        }

        equipos.remove(equipo); //elimino el equipo de la liga
        nombresEquipos.remove(normalizarClave(equipo.getNombre())); //libero el nombre en el hashset
        System.out.println("Deshacer realizado: equipo eliminado realmente.");
    }

    private void deshacerCreacionPartido(String idPartido) { //deshace un partido creado si todavía está pendiente
        Partido partidoEncontrado = null; //aquí guardaré el partido si lo encuentro en la cola

        for (Partido partido : partidosPendientes) {
            if (partido.getId().equalsIgnoreCase(idPartido)) {
                partidoEncontrado = partido;
            }
        }

        if (partidoEncontrado == null) {
            System.out.println("No se pudo deshacer: el partido ya no está pendiente.");
            return;
        }

        partidosPendientes.remove(partidoEncontrado); //lo quito de la cola de pendientes
        idsPartidos.remove(normalizarClave(partidoEncontrado.getId())); //libero el id del partido

        for (int i = 0; i < calendario.length; i++) {
            for (int j = 0; j < calendario[i].length; j++) {
                if (calendario[i][j] == partidoEncontrado) {
                    calendario[i][j] = null; //también lo borro de la matriz del calendario
                }
            }
        }

        System.out.println("Deshacer realizado: partido eliminado realmente.");
    }

    public ArrayList<Equipo> getEquipos() { //getter de la lista de equipos
        return equipos;
    }

    public ArrayList<PersonaLiga> getPersonas() { //getter de la lista de personas
        return personas;
    }
}
