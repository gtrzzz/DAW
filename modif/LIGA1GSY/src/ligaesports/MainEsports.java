package ligaesports;

import java.util.Scanner;

public class MainEsports {

    public static void main(String[] args) { //método principal, desde aquí arranca el programa y el menú

        Scanner teclado = new Scanner(System.in);
        Liga liga = new Liga("1GSY WORLD CHAMPIONSHIP SERIES"); //creo liga de tipo Liga donde le paso el nombre de la liga

        cargarDatosPrueba(liga); //aquí lo que hago es llamar a ese método para pasarle unos valores que ya he predefinido para así tener ya unos equipos, patrocinadores y unas jornadas por defecto a la liga que acabo de crear arriba

        liga.generarEventosAleatoriosDelDia(); //este método es de la clase Liga, y genera una serie de eventos aleatorios que tiene unos arraylist para cada evento (explicado mejor en el propio método)
        liga.mostrarResumenEventoInicial(); //más de lo mismo, es método de la clase Liga que muestra un resumen inicial (explicado mejor en el propio método)

        int opcion; //declaro variable para poder elegir opciones para el menú

        do { //do while que se va a ejecutar hasta que la opción sea 25
            System.out.println();
            System.out.println("===== 1GSY WORLD CHAMPIONSHIP SERIES =====");
            System.out.println("1. Mostrar personas");
            System.out.println("2. Buscar persona por ID");
            System.out.println("3. Modificar persona");
            System.out.println("4. Eliminar persona");
            System.out.println("5. Mostrar jugadores");
            System.out.println("6. Mostrar equipos");
            System.out.println("7. Mostrar plantillas completas");
            System.out.println("8. Crear jugador libre");
            System.out.println("9. Mercado de fichajes");
            System.out.println("10. Sustituir titular por suplente");
            System.out.println("11. Mostrar calendario");
            System.out.println("12. Consultar jornada");
            System.out.println("13. Ver siguiente partido");
            System.out.println("14. Mostrar partidos pendientes");
            System.out.println("15. Vaciar cola de partidos");
            System.out.println("16. Apostar al siguiente partido");
            System.out.println("17. Registrar resultado manual");
            System.out.println("18. Disputar siguiente partido automáticamente");
            System.out.println("19. Gestionar incidencias y sanciones");
            System.out.println("20. Mostrar clasificación");
            System.out.println("21. Mostrar eventos");
            System.out.println("22. Mostrar historial");
            System.out.println("23. Deshacer última acción");
            System.out.println("24. Entregar premios finales");
            System.out.println("25. Salir");
            opcion = leerEntero(teclado, "Elige una opción: ", 1, 25); //uso el método leerEntero para asegurarme de que se mete un número válido entre 1 y 25

            if (opcion == 1) { //según la opción que elija el usuario, se ejecuta una parte del menú u otra, es este caso es la opción 1 que muestra todas las personas registradas en la liga
                liga.mostrarPersonas(); //muestra todas las personas que hay registradas en la liga

            } else if (opcion == 2) {
                String id = leerTexto(teclado, "ID de la persona: "); //pido el id para buscar una persona concreta
                liga.mostrarPersonaPorId(id); //llamo al método de liga que busca y muestra esa persona

            } else if (opcion == 3) {
                modificarPersonaPorTeclado(teclado, liga); //llamo a un método aparte para no llenar el main de preguntas

            } else if (opcion == 4) {
                String id = leerTexto(teclado, "ID de la persona a eliminar: "); //pido el id de la persona que se quiere borrar
                liga.eliminarPersona(id); //se elimina solo si se puede eliminar según las reglas de la liga

            } else if (opcion == 5) {
                liga.mostrarJugadores(); //muestra solo las personas que son jugadores

            } else if (opcion == 6) {
                liga.mostrarEquipos(); //muestra todos los equipos con sus datos principales

            } else if (opcion == 7) {
                for (Equipo equipo : liga.getEquipos()) { //recorro todos los equipos para enseñar su plantilla completa
                    equipo.mostrarPlantilla(); //muestra titulares, suplentes, entrenador, patrocinadores y coste
                    System.out.println("----------------------");
                }

            } else if (opcion == 8) {
                crearJugadorPorTeclado(teclado, liga); //crea un jugador nuevo que de momento no pertenece a ningún equipo

            } else if (opcion == 9) {
                mercadoFichajes(teclado, liga); //abre el submenú para fichar o transferir jugadores

            } else if (opcion == 10) {
                sustituirPorTeclado(teclado, liga); //permite cambiar un titular por un suplente del mismo rol

            } else if (opcion == 11) {
                liga.mostrarCalendario(); //muestra todos los partidos guardados en la matriz del calendario

            } else if (opcion == 12) {
                int jornada = leerEntero(teclado, "Jornada: ", 1, 10); //pido una jornada válida entre las que puede tener el calendario
                liga.mostrarJornada(jornada); //muestra solo los partidos de esa jornada

            } else if (opcion == 13) {
                liga.verSiguientePartido(); //enseña el siguiente partido de la cola sin sacarlo de la cola

            } else if (opcion == 14) {
                liga.mostrarPartidosPendientes(); //muestra todos los partidos que siguen pendientes en la cola

            } else if (opcion == 15) {
                liga.vaciarPartidosPendientes(); //borra todos los partidos pendientes si se quiere limpiar la cola

            } else if (opcion == 16) {
                apostarPorTeclado(teclado, liga); //permite añadir apoyo económico al equipo local o visitante

            } else if (opcion == 17) {
                registrarResultadoPorTeclado(teclado, liga); //registra manualmente el resultado del siguiente partido

            } else if (opcion == 18) {
                gestionarInicioPartido(teclado, liga); //disputa automáticamente el siguiente partido de la cola

            } else if (opcion == 19) {
                gestionarIncidencias(teclado, liga); //abre el submenú de incidencias y sanciones

            } else if (opcion == 20) {
                liga.mostrarClasificacion(); //muestra la clasificación ordenada según los criterios de la liga

            } else if (opcion == 21) {
                liga.mostrarEventos(); //muestra los eventos del día del torneo

            } else if (opcion == 22) {
                liga.mostrarHistorial(); //muestra las últimas acciones guardadas en la pila

            } else if (opcion == 23) {
                liga.deshacerUltimaAccion(); //deshace la última acción

            } else if (opcion == 24) {
                liga.entregarPremiosFinales(); //muestra los premios y puestos finales del torneo

            } else if (opcion == 25) {
                System.out.println("Saliendo del programa..."); //cuando la opción es 25 termina el bucle

            } else {
                System.out.println("Opción no válida.");
            }

        } while (opcion != 25); //el menú se repite mientras no se elija salir

        teclado.close(); //cierro el scanner al final del programa
    }

    public static void modificarPersonaPorTeclado(Scanner teclado, Liga liga) { //método para pedir por teclado los nuevos datos de una persona
        String id = leerTexto(teclado, "ID de la persona a modificar: "); //primero pido el id para saber qué persona se va a modificar
        String nombre = leerTexto(teclado, "Nuevo nombre: "); //pido el nuevo nombre real
        String nickname = leerTexto(teclado, "Nuevo nickname: "); //pido el nuevo nickname
        int edad = leerEntero(teclado, "Nueva edad: ", 1, 100); //pido una edad válida
        double salarioBase = leerDouble(teclado, "Nuevo salario base: ", 0, Double.MAX_VALUE); //pido el nuevo salario y no dejo que sea negativo (he puesto el salario como el máximo valor para el double por facilitar a la hora de crear pero podría poner cualquier múmero)

        liga.modificarDatosPersona(id, nombre, nickname, edad, salarioBase); //le paso todos los datos a liga, que es quien modifica realmente la persona
    }

    public static void sustituirPorTeclado(Scanner teclado, Liga liga) { //método para hacer una sustitución manual entre titular y suplente
        String nombreEquipo = leerTexto(teclado, "Nombre del equipo: "); //pido el equipo donde se va a hacer la sustitución
        Rol rol = pedirRol(teclado); //pido el rol que se quiere sustituir
        String idSuplente = leerTexto(teclado, "ID del suplente que entra: "); //pido el jugador suplente que va a entrar

        liga.sustituirTitularPorSuplente(nombreEquipo, rol, idSuplente); //la clase liga comprueba si todo es válido y hace el cambio
    }

    public static void registrarResultadoPorTeclado(Scanner teclado, Liga liga) { //método para meter a mano el resultado del siguiente partido
        Partido partido = liga.obtenerSiguientePartido(); //cojo el primer partido de la cola sin eliminarlo todavía

        if (partido == null) { //si no hay partido pendiente no se puede registrar nada
            System.out.println("No hay partidos pendientes.");
            return; //este return vacío es necesario porque si quieres hacer un registro del partido y no hay más partidos pendientes, el programa da error, se hace para salir del método sin que dé error
        }

        partido.mostrarPartido(); //enseño el partido para que se vea qué equipos juegan
        int puntosLocal = leerEntero(teclado, "Puntos equipo local: ", 0, 999); //pido los puntos del equipo local
        int puntosVisitante = leerEntero(teclado, "Puntos equipo visitante: ", 0, 999); //pido los puntos del equipo visitante

        liga.registrarResultadoManualSiguientePartido(puntosLocal, puntosVisitante); //liga registra el resultado y actualiza estadísticas
    }

    public static void gestionarIncidencias(Scanner teclado, Liga liga) { //submenú para trabajar con incidencias y sanciones
        System.out.println();
        System.out.println("=== INCIDENCIAS Y SANCIONES ===");
        System.out.println("1. Registrar incidencia");
        System.out.println("2. Listar incidencias");
        System.out.println("3. Buscar incidencias por equipo");
        System.out.println("4. Buscar incidencias por jugador");
        int opcion = leerEntero(teclado, "Elige opción: ", 1, 4); //pido una opción válida del submenú

        if (opcion == 1) { //dependiendo de la opción se registra, lista o busca incidencias
            registrarIncidenciaPorTeclado(teclado, liga);
        } else if (opcion == 2) {
            liga.listarIncidencias();
        } else if (opcion == 3) {
            String nombreEquipo = leerTexto(teclado, "Nombre del equipo: "); //pido el nombre del equipo para filtrar incidencias
            liga.buscarIncidenciasPorEquipo(nombreEquipo); //busca incidencias relacionadas con ese equipo
        } else {
            String idJugador = leerTexto(teclado, "ID del jugador: "); //pido el id del jugador para filtrar incidencias
            liga.buscarIncidenciasPorJugador(idJugador); //busca incidencias relacionadas con ese jugador
        }
    }

    public static void registrarIncidenciaPorTeclado(Scanner teclado, Liga liga) { //método que pide todos los datos para crear una incidencia
        String id = leerTexto(teclado, "ID de la incidencia: "); //pido un id para la incidencia
        TipoIncidencia tipo = pedirTipoIncidencia(teclado); //pido el tipo usando otro método para no repetir el menú
        String descripcion = leerTexto(teclado, "Descripción: "); //pido una explicación/descripción de lo que ha pasado (la justificación de la incidencia)

        Equipo equipo = null; //empieza en null porque puede que la incidencia no tenga equipo concreto
        Jugador jugador = null; //igual con el jugador, puede estar relacionada o no

        String nombreEquipo = leerTextoOpcional(teclado, "Equipo relacionado (Enter si no hay): "); //este dato lo dejo opcional
        if (!nombreEquipo.equals("")) { //si el usuario escribe algo, intento buscar ese equipo (si el nombre del equipo NO está vacío entonces es porque algo se ha escrito por teclado)
            equipo = liga.buscarEquipoPorNombre(nombreEquipo); //aquí va a comparar lo que se ha escrito por teclado, si coincide con el nombre de algún equipo entonces se registra la incidencia a ese equipo, si no, se guarda la incidencia pero sin equipo relacionadoo como se indica justo abajo

            if (equipo == null) { //si no existe, aviso pero no paro el programa
                System.out.println("No existe ese equipo. La incidencia se guardará sin equipo relacionado."); //imprime por pantalla que se registra incidencia sin equipo relacionado
            }
        }

        String idJugador = leerTextoOpcional(teclado, "Jugador relacionado (Enter si no hay): "); //también dejo opcional el jugador, mismo funcionamiento que en el método anterior pero para los jugadores (busca el id del jugador y registra según si se encuentra o no)
        if (!idJugador.equals("")) { //si se escribe un id, busco ese jugador
            jugador = liga.buscarJugadorPorId(idJugador);

            if (jugador == null) { //si el jugador no existe, lo aviso y se guarda sin jugador
                System.out.println("No existe ese jugador. La incidencia se guardará sin jugador relacionado.");
            }
        }

        liga.registrarIncidencia(new Incidencia(id, tipo, descripcion, equipo, jugador)); //creo la incidencia y se la paso a liga para guardarla
        System.out.println("Incidencia registrada correctamente.");
    }

    public static TipoIncidencia pedirTipoIncidencia(Scanner teclado) { //método para pedir el tipo de incidencia y devolver el enum correspondiente
        System.out.println("Tipo de incidencia:");
        System.out.println("1. SANCION");
        System.out.println("2. EXPULSION");
        System.out.println("3. ERROR_TECNICO");
        System.out.println("4. PARTIDO_APLAZADO");
        System.out.println("5. OTRO");
        int opcion = leerEntero(teclado, "Elige tipo: ", 1, 5); //controlo que el usuario solo pueda elegir entre los tipos que hay

        if (opcion == 1) { //con estos if convierto el número elegido en un valor del enum TipoIncidencia
            return TipoIncidencia.SANCION;
        } else if (opcion == 2) {
            return TipoIncidencia.EXPULSION;
        } else if (opcion == 3) {
            return TipoIncidencia.ERROR_TECNICO;
        } else if (opcion == 4) {
            return TipoIncidencia.PARTIDO_APLAZADO;
        }

        return TipoIncidencia.OTRO; //si no es ninguna de las anteriores, devuelve otro
    }

    public static void gestionarInicioPartido(Scanner teclado, Liga liga) { //método que prepara el inicio del siguiente partido
        Partido partido = liga.obtenerSiguientePartido(); //cojo el siguiente partido de la cola

        if (partido == null) { //si no hay partido, no se puede disputar nada
            System.out.println("No hay partidos pendientes.");
            return; //este return vacío es igual que en registrarResultadoPorTeclado, es necesario para poder salir del método y continuar con la ejecución del programa
        }

        System.out.println(); //espacio vacío por organización y mejor visibilidad
        System.out.println("Antes de iniciar el partido se muestran las apuestas actuales:"); //imprime por pantalla
        partido.mostrarApuestas(); //llama al método para mostrar las apuestas

        System.out.print("¿Quieres añadir apoyo económico antes del partido? s/n: ");
        String respuesta = teclado.nextLine(); //leo si el usuario quiere añadir apoyo económico antes de jugar (que si quiere apostar, vaya)

        if (respuesta.equalsIgnoreCase("s")) { //si responde s, le dejo apostar antes del partido
            apostarPorTeclado(teclado, liga);
        } //si responde n, no hace nada y se va a disputar el partido directamente

        liga.disputarSiguientePartido(); //después se disputa el partido automáticamente
    }

    public static void apostarPorTeclado(Scanner teclado, Liga liga) { //método para añadir apoyo económico al siguiente partido
        Partido partido = liga.obtenerSiguientePartido(); //miro cuál es el partido que está primero en la cola

        if (partido == null) { //si no hay partido, no tiene sentido apostar
            System.out.println("No hay partidos pendientes.");
            return;
        }

        partido.mostrarApuestas(); //muestro cómo van las apuestas antes de sumar nada

        System.out.println("¿A qué equipo quieres sumar apoyo de comunidad?");
        System.out.println("1. Equipo local");
        System.out.println("2. Equipo visitante");
        int opcionEquipo = leerEntero(teclado, "Elige opción: ", 1, 2); //el usuario elige si apoya al local o al visitante
        double cantidad = leerDouble(teclado, "Cantidad a añadir: ", 0, Double.MAX_VALUE); //la cantidad no puede ser negativa

        liga.apostarAlSiguientePartido(opcionEquipo, cantidad); //liga se encarga de sumar ese dinero al partido
    }

    public static void crearJugadorPorTeclado(Scanner teclado, Liga liga) { //método para crear un jugador libre desde el menú
        try { //uso try catch porque al crear el jugador pueden saltar errores de validación o de id repetido
            String id = leerTexto(teclado, "ID del jugador: "); //pido el id del jugador

            String nombre = leerTexto(teclado, "Nombre real: "); //pido el nombre real

            String nickname = leerTexto(teclado, "Nickname: "); //pido el nickname

            int edad = leerEntero(teclado, "Edad: ", 1, 120); //pido una edad válida

            double salarioBase = leerDouble(teclado, "Salario base: ", 0, Double.MAX_VALUE); //pido el salario base y no permito negativos

            Rol rol = pedirRol(teclado); //pido el rol usando el método pedirRol

            int nivelMecanico = leerEntero(teclado, "Nivel mecánico 0-100: ", 0, 100); //pido el nivel mecánico entre 0 y 100
            int nivelEstrategico = leerEntero(teclado, "Nivel estratégico 0-100: ", 0, 100); //pido el nivel estratégico entre 0 y 100

            Jugador jugador = new Jugador(id, nombre, nickname, edad, salarioBase, rol, nivelMecanico, nivelEstrategico); //creo el objeto jugador con todos los datos anteriores

            liga.altaPersona(jugador); //lo doy de alta en la liga como persona registrada

            System.out.println("Jugador libre creado correctamente.");

        } catch (PersonaDuplicadaException e) { //si el id ya existe, se captura esta excepción propia de que hay una persona con el mismo id y se muestra el error
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) { //si algún dato no es válido, se muestra el error
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static Rol pedirRol(Scanner teclado) { //método para pedir un rol y devolver el valor del enum Rol
        System.out.println("Rol:");
        System.out.println("1. TOP");
        System.out.println("2. JUNGLE");
        System.out.println("3. MID");
        System.out.println("4. ADC");
        System.out.println("5. SUPPORT");
        int opcionRol = leerEntero(teclado, "Elige rol: ", 1, 5); //controlo que solo se pueda elegir un rol válido

        if (opcionRol == 1) { //con estos if convierto el número elegido en el rol correspondiente
            return Rol.TOP;
        } else if (opcionRol == 2) {
            return Rol.JUNGLE;
        } else if (opcionRol == 3) {
            return Rol.MID;
        } else if (opcionRol == 4) {
            return Rol.ADC;
        }

        return Rol.SUPPORT; //si no es ninguno de los anteriores, tiene que ser support porque era la opción 5
    }

    public static void mercadoFichajes(Scanner teclado, Liga liga) { //submenú para fichar jugadores libres o transferirlos entre equipos
        System.out.println();
        System.out.println("=== MERCADO DE FICHAJES ===");
        System.out.println("1. Fichar jugador libre como suplente");
        System.out.println("2. Transferir jugador entre equipos");
        int opcion = leerEntero(teclado, "Elige opción: ", 1, 2); //solo dejo elegir entre fichaje libre o transferencia

        String idJugador = leerTexto(teclado, "ID del jugador: "); //pido el jugador que se quiere mover

        String nombreEquipo = leerTexto(teclado, "Nombre del equipo destino: "); //pido el equipo al que va a ir

        if (opcion == 1) { //si es opción 1 se ficha como suplente si está libre
            liga.ficharJugadorLibreComoSuplente(idJugador, nombreEquipo);
        } else if (opcion == 2) {
            liga.transferirJugador(idJugador, nombreEquipo); //si es opción 2 se transfiere desde su equipo actual al nuevo
        }
    }

    public static String leerTexto(Scanner teclado, String mensaje) { //método para leer un texto obligatorio por teclado
        String texto; //declaro la variable donde se va a guardar lo que escriba el usuario

        do { //repito hasta que el usuario escriba algo
            System.out.print(mensaje); //muestro el mensaje que se le pasa al método
            texto = teclado.nextLine().trim(); //leo el texto y quito espacios al principio y al final

            if (texto.equals("")) { //si está vacío, aviso y vuelvo a pedirlo
                System.out.println("El texto no puede estar vacío.");
            }
        } while (texto.equals(""));

        return texto; //devuelvo el texto ya validado
    }

    public static String leerTextoOpcional(Scanner teclado, String mensaje) { //método para leer texto que sí puede quedarse vacío
        System.out.print(mensaje); //muestro el mensaje que se le pasa al método
        return teclado.nextLine().trim(); //devuelvo lo que escriba el usuario, aunque sea vacío
    }

    public static int leerEntero(Scanner teclado, String mensaje, int minimo, int maximo) { //método para leer un número entero dentro de un rango
        while (true) { //uso while true porque solo salgo del método cuando el dato es correcto, podría usar un booleano para controlar el bucle pero así simplifica un poco el código y no hace falta declarar variables extra
            System.out.print(mensaje); //muestro el mensaje que se le pasa al método
            String entrada = teclado.nextLine().trim(); //leo la entrada como texto para poder controlar errores

            try { //intento convertir lo que ha escrito el usuario a entero
                int valor = Integer.parseInt(entrada); //si se puede convertir, lo guardo en valor

                if (valor < minimo || valor > maximo) { //compruebo que esté dentro del rango permitido
                    System.out.println("Introduce un número entre " + minimo + " y " + maximo + ".");
                } else {
                    return valor; //si todo está bien, devuelvo el número
                }
            } catch (NumberFormatException e) { //si escribe cualquier otra cosa que no sea un entero entra aquí
                System.out.println("Introduce un número entero válido.");
            }
        }
    }

    public static double leerDouble(Scanner teclado, String mensaje, double minimo, double maximo) { //método para leer un número decimal dentro de un rango
        while (true) { //se repite hasta que el número sea válido, al igual que en leerEntero uso un while true para simplificar el código y no tener que declarar variables extra
            System.out.print(mensaje); //muestro el mensaje que se le pasa al método
            String entrada = teclado.nextLine().trim().replace(',', '.'); //permito usar coma o punto para los decimales y quito espacios al principio y al final

            try { //intento convertir el texto a double
                double valor = Double.parseDouble(entrada); //si se puede convertir, lo guardo como decimal

                if (valor < minimo || valor > maximo) { //compruebo que esté entre el mínimo y el máximo
                    System.out.println("Introduce un número entre " + minimo + " y " + maximo + ".");
                } else {
                    return valor; //si está bien, devuelvo el número decimal
                }
            } catch (NumberFormatException e) { //si no se puede convertir a número, aviso al usuario
                System.out.println("Introduce un número válido.");
            }
        }
    }

    public static void cargarDatosPrueba(Liga liga) { //método para cargar datos iniciales y no tener que meterlos todos a mano
        try { //uso try catch porque al crear equipos, personas o partidos pueden saltar excepciones
            Equipo equipo1 = crearEquipoCompleto(liga, "Erbeti", "Sevilla", "Iván Gutiérrez", "zzz", "Estrategia", "NW", 1, 75);
            Equipo equipo2 = crearEquipoCompleto(liga, "Karpa", "Barcelona", "Laura Ruiz", "ruiz.coach", "Draft y análisis", "PD", 10, 73);
            Equipo equipo3 = crearEquipoCompleto(liga, "G3B", "Madrid", "Pedro Sánchez", "preZidente", "Juego tardío", "CT", 20, 82);
            Equipo equipo4 = crearEquipoCompleto(liga, "Nube8", "Cádiz", "Marta León", "Martrains", "Juego agresivo", "QF", 30, 72);
            Equipo equipo5 = crearEquipoCompleto(liga, "Los Diozes", "Málaga", "Juan Alberto García", "IlloJuan", "Puñalaíta/Control del mapa", "BW", 40, 78);
            Equipo equipo6 = crearEquipoCompleto(liga, "South Side", "Granada", "Fernando Alonso", "El Nano", "Peleas grupales", "SR", 50, 76);

            equipo1.agregarPatrocinador(new Patrocinador("RedBull Arena", 8000)); //añado patrocinadores a cada equipo para que el torneo tenga más realismo
            equipo1.agregarPatrocinador(new Patrocinador("PCDescomponentes", 5000)); //nombre y aportación

            equipo2.agregarPatrocinador(new Patrocinador("Revolut", 9000));
            equipo2.agregarPatrocinador(new Patrocinador("Kick", 4000));

            equipo3.agregarPatrocinador(new Patrocinador("Bizarrap", 12000));
            equipo3.agregarPatrocinador(new Patrocinador("Acliclas", 7000));

            equipo4.agregarPatrocinador(new Patrocinador("Megabyte", 6000));
            equipo4.agregarPatrocinador(new Patrocinador("Nike", 4500));

            equipo5.agregarPatrocinador(new Patrocinador("InfoJobs", 7500));
            equipo5.agregarPatrocinador(new Patrocinador("Sony", 3500));

            equipo6.agregarPatrocinador(new Patrocinador("Rolex", 8500));
            equipo6.agregarPatrocinador(new Patrocinador("Hugo Boss", 4200));

            liga.crearPartido("P1", 1, equipo1, equipo2); //creo los partidos de las tres primeras jornadas
            liga.crearPartido("P2", 1, equipo3, equipo4);
            liga.crearPartido("P3", 1, equipo5, equipo6);

            liga.crearPartido("P4", 2, equipo1, equipo3);
            liga.crearPartido("P5", 2, equipo2, equipo5);
            liga.crearPartido("P6", 2, equipo4, equipo6);

            liga.crearPartido("P7", 3, equipo1, equipo5);
            liga.crearPartido("P8", 3, equipo2, equipo4);
            liga.crearPartido("P9", 3, equipo3, equipo6);

        } catch (PersonaDuplicadaException | EquipoDuplicadoException | RolNoDisponibleException | PartidoInvalidoException e) { //si algo falla al cargar datos, se muestra el mensaje de error
            System.out.println("Error cargando datos de prueba: " + e.getMessage());
        }
    }

    public static Equipo crearEquipoCompleto(Liga liga, String nombreEquipo, String ciudad, String nombreEntrenador, String nickEntrenador, String especialidad, String prefijo, int inicioId, int nivelBase) throws PersonaDuplicadaException, EquipoDuplicadoException, RolNoDisponibleException { //método que crea un equipo entero con entrenador, titulares y suplentes

        Entrenador entrenador = new Entrenador("E" + prefijo, nombreEntrenador, nickEntrenador, 35, 1800, 7, especialidad); //creo el entrenador del equipo con un id que empieza por E para diferenciarlo de los jugadores, con el nombre, nickname, edad, salario base, años de experiencia y especialidad que se le pasan al método
        liga.altaPersona(entrenador); //lo doy de alta como persona de la liga

        Equipo equipo = new Equipo(nombreEquipo, ciudad, 25000); //creo el equipo con nombre, ciudad y presupuesto
        equipo.asignarEntrenador(entrenador); //le asigno el entrenador que he creado antes

        Jugador top = new Jugador("J" + (inicioId), "Jugador " + prefijo + " TOP", prefijo + "Top", 20, 1200, Rol.TOP, nivelBase, nivelBase - 2); //creo los cinco titulares, uno por cada rol, con un id que empieza por J para diferenciarlos del entrenador, con un nombre que incluye el prefijo del equipo para identificarlos mejor, con edad, salario base, rol y niveles mecánico y estratégico relacionados entre sí (el nivel base es el mismo para los cinco jugadores pero luego cada uno tiene un nivel mecánico y estratégico diferente para que no sean todos iguales)
        Jugador jungle = new Jugador("J" + (inicioId + 1), "Jugador " + prefijo + " JUNGLE", prefijo + "Jungle", 21, 1200, Rol.JUNGLE, nivelBase + 1, nivelBase);
        Jugador mid = new Jugador("J" + (inicioId + 2), "Jugador " + prefijo + " MID", prefijo + "Mid", 19, 1300, Rol.MID, nivelBase + 3, nivelBase);
        Jugador adc = new Jugador("J" + (inicioId + 3), "Jugador " + prefijo + " ADC", prefijo + "ADC", 22, 1250, Rol.ADC, nivelBase + 2, nivelBase - 1);
        Jugador support = new Jugador("J" + (inicioId + 4), "Jugador " + prefijo + " SUPPORT", prefijo + "Support", 23, 1150, Rol.SUPPORT, nivelBase - 4, nivelBase + 5);

        Jugador suplente1 = new Jugador("J" + (inicioId + 5), "Suplente " + prefijo + " MID", prefijo + "SubMid", 20, 1000, Rol.MID, nivelBase - 5, nivelBase - 3); //creo dos suplentes para que el equipo pueda hacer cambios
        Jugador suplente2 = new Jugador("J" + (inicioId + 6), "Suplente " + prefijo + " SUPPORT", prefijo + "SubSupport", 21, 1000, Rol.SUPPORT, nivelBase - 6, nivelBase);

        liga.altaPersona(top); //doy de alta a todos los jugadores en la liga
        liga.altaPersona(jungle);
        liga.altaPersona(mid);
        liga.altaPersona(adc);
        liga.altaPersona(support);
        liga.altaPersona(suplente1);
        liga.altaPersona(suplente2);

        equipo.ficharTitular(top); //añado los jugadores titulares al array fijo de titulares
        equipo.ficharTitular(jungle);
        equipo.ficharTitular(mid);
        equipo.ficharTitular(adc);
        equipo.ficharTitular(support);

        equipo.ficharSuplente(suplente1); //añado los suplentes al arraylist de suplentes
        equipo.ficharSuplente(suplente2);

        liga.crearEquipo(equipo); //cuando el equipo ya está completo, lo registro en la liga

        return equipo; //devuelvo el equipo para poder usarlo después al crear partidos
    }
}
