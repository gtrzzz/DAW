package ligaesports;

import java.util.Scanner;

public class MainEsports {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);
        Liga liga = new Liga("Liga Nexus 1GSY");

        cargarDatosPrueba(liga);

        liga.generarEventosAleatoriosDelDia();
        liga.mostrarResumenEventoInicial();

        int opcion;

        do {
            System.out.println();
            System.out.println("===== LIGA NEXUS 1GSY =====");
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
            opcion = leerEntero(teclado, "Elige una opción: ", 1, 25);

            if (opcion == 1) {
                liga.mostrarPersonas();

            } else if (opcion == 2) {
                String id = leerTexto(teclado, "ID de la persona: ");
                liga.mostrarPersonaPorId(id);

            } else if (opcion == 3) {
                modificarPersonaPorTeclado(teclado, liga);

            } else if (opcion == 4) {
                String id = leerTexto(teclado, "ID de la persona a eliminar: ");
                liga.eliminarPersona(id);

            } else if (opcion == 5) {
                liga.mostrarJugadores();

            } else if (opcion == 6) {
                liga.mostrarEquipos();

            } else if (opcion == 7) {
                for (Equipo equipo : liga.getEquipos()) {
                    equipo.mostrarPlantilla();
                    System.out.println("----------------------");
                }

            } else if (opcion == 8) {
                crearJugadorPorTeclado(teclado, liga);

            } else if (opcion == 9) {
                mercadoFichajes(teclado, liga);

            } else if (opcion == 10) {
                sustituirPorTeclado(teclado, liga);

            } else if (opcion == 11) {
                liga.mostrarCalendario();

            } else if (opcion == 12) {
                int jornada = leerEntero(teclado, "Jornada: ", 1, 10);
                liga.mostrarJornada(jornada);

            } else if (opcion == 13) {
                liga.verSiguientePartido();

            } else if (opcion == 14) {
                liga.mostrarPartidosPendientes();

            } else if (opcion == 15) {
                liga.vaciarPartidosPendientes();

            } else if (opcion == 16) {
                apostarPorTeclado(teclado, liga);

            } else if (opcion == 17) {
                registrarResultadoPorTeclado(teclado, liga);

            } else if (opcion == 18) {
                gestionarInicioPartido(teclado, liga);

            } else if (opcion == 19) {
                gestionarIncidencias(teclado, liga);

            } else if (opcion == 20) {
                liga.mostrarClasificacion();

            } else if (opcion == 21) {
                liga.mostrarEventos();

            } else if (opcion == 22) {
                liga.mostrarHistorial();

            } else if (opcion == 23) {
                liga.deshacerUltimaAccion();

            } else if (opcion == 24) {
                liga.entregarPremiosFinales();

            } else if (opcion == 25) {
                System.out.println("Saliendo del programa...");

            } else {
                System.out.println("Opción no válida.");
            }

        } while (opcion != 25);

        teclado.close();
    }

    public static void modificarPersonaPorTeclado(Scanner teclado, Liga liga) {
        String id = leerTexto(teclado, "ID de la persona a modificar: ");
        String nombre = leerTexto(teclado, "Nuevo nombre: ");
        String nickname = leerTexto(teclado, "Nuevo nickname: ");
        int edad = leerEntero(teclado, "Nueva edad: ", 1, 120);
        double salarioBase = leerDouble(teclado, "Nuevo salario base: ", 0, Double.MAX_VALUE);

        liga.modificarDatosPersona(id, nombre, nickname, edad, salarioBase);
    }

    public static void sustituirPorTeclado(Scanner teclado, Liga liga) {
        String nombreEquipo = leerTexto(teclado, "Nombre del equipo: ");
        Rol rol = pedirRol(teclado);
        String idSuplente = leerTexto(teclado, "ID del suplente que entra: ");

        liga.sustituirTitularPorSuplente(nombreEquipo, rol, idSuplente);
    }

    public static void registrarResultadoPorTeclado(Scanner teclado, Liga liga) {
        Partido partido = liga.obtenerSiguientePartido();

        if (partido == null) {
            System.out.println("No hay partidos pendientes.");
            return;
        }

        partido.mostrarPartido();
        int puntosLocal = leerEntero(teclado, "Puntos equipo local: ", 0, 999);
        int puntosVisitante = leerEntero(teclado, "Puntos equipo visitante: ", 0, 999);

        liga.registrarResultadoManualSiguientePartido(puntosLocal, puntosVisitante);
    }

    public static void gestionarIncidencias(Scanner teclado, Liga liga) {
        System.out.println();
        System.out.println("=== INCIDENCIAS Y SANCIONES ===");
        System.out.println("1. Registrar incidencia");
        System.out.println("2. Listar incidencias");
        System.out.println("3. Buscar incidencias por equipo");
        System.out.println("4. Buscar incidencias por jugador");
        int opcion = leerEntero(teclado, "Elige opción: ", 1, 4);

        if (opcion == 1) {
            registrarIncidenciaPorTeclado(teclado, liga);
        } else if (opcion == 2) {
            liga.listarIncidencias();
        } else if (opcion == 3) {
            String nombreEquipo = leerTexto(teclado, "Nombre del equipo: ");
            liga.buscarIncidenciasPorEquipo(nombreEquipo);
        } else {
            String idJugador = leerTexto(teclado, "ID del jugador: ");
            liga.buscarIncidenciasPorJugador(idJugador);
        }
    }

    public static void registrarIncidenciaPorTeclado(Scanner teclado, Liga liga) {
        String id = leerTexto(teclado, "ID de la incidencia: ");
        TipoIncidencia tipo = pedirTipoIncidencia(teclado);
        String descripcion = leerTexto(teclado, "Descripción: ");

        Equipo equipo = null;
        Jugador jugador = null;

        String nombreEquipo = leerTextoOpcional(teclado, "Equipo relacionado (Enter si no hay): ");
        if (!nombreEquipo.equals("")) {
            equipo = liga.buscarEquipoPorNombre(nombreEquipo);

            if (equipo == null) {
                System.out.println("No existe ese equipo. La incidencia se guardará sin equipo relacionado.");
            }
        }

        String idJugador = leerTextoOpcional(teclado, "Jugador relacionado (Enter si no hay): ");
        if (!idJugador.equals("")) {
            jugador = liga.buscarJugadorPorId(idJugador);

            if (jugador == null) {
                System.out.println("No existe ese jugador. La incidencia se guardará sin jugador relacionado.");
            }
        }

        liga.registrarIncidencia(new Incidencia(id, tipo, descripcion, equipo, jugador));
        System.out.println("Incidencia registrada correctamente.");
    }

    public static TipoIncidencia pedirTipoIncidencia(Scanner teclado) {
        System.out.println("Tipo de incidencia:");
        System.out.println("1. SANCION");
        System.out.println("2. EXPULSION");
        System.out.println("3. ERROR_TECNICO");
        System.out.println("4. PARTIDO_APLAZADO");
        System.out.println("5. OTRO");
        int opcion = leerEntero(teclado, "Elige tipo: ", 1, 5);

        if (opcion == 1) {
            return TipoIncidencia.SANCION;
        } else if (opcion == 2) {
            return TipoIncidencia.EXPULSION;
        } else if (opcion == 3) {
            return TipoIncidencia.ERROR_TECNICO;
        } else if (opcion == 4) {
            return TipoIncidencia.PARTIDO_APLAZADO;
        }

        return TipoIncidencia.OTRO;
    }

    public static void gestionarInicioPartido(Scanner teclado, Liga liga) {
        Partido partido = liga.obtenerSiguientePartido();

        if (partido == null) {
            System.out.println("No hay partidos pendientes.");
            return;
        }

        System.out.println();
        System.out.println("Antes de iniciar el partido se muestran las apuestas actuales:");
        partido.mostrarApuestas();

        System.out.print("¿Quieres añadir apoyo económico antes del partido? s/n: ");
        String respuesta = teclado.nextLine();

        if (respuesta.equalsIgnoreCase("s")) {
            apostarPorTeclado(teclado, liga);
        }

        liga.disputarSiguientePartido();
    }

    public static void apostarPorTeclado(Scanner teclado, Liga liga) {
        Partido partido = liga.obtenerSiguientePartido();

        if (partido == null) {
            System.out.println("No hay partidos pendientes.");
            return;
        }

        partido.mostrarApuestas();

        System.out.println("¿A qué equipo quieres sumar apoyo de comunidad?");
        System.out.println("1. Equipo local");
        System.out.println("2. Equipo visitante");
        int opcionEquipo = leerEntero(teclado, "Elige opción: ", 1, 2);
        double cantidad = leerDouble(teclado, "Cantidad a añadir: ", 0, Double.MAX_VALUE);

        liga.apostarAlSiguientePartido(opcionEquipo, cantidad);
    }

    public static void crearJugadorPorTeclado(Scanner teclado, Liga liga) {
        try {
            String id = leerTexto(teclado, "ID del jugador: ");

            String nombre = leerTexto(teclado, "Nombre real: ");

            String nickname = leerTexto(teclado, "Nickname: ");

            int edad = leerEntero(teclado, "Edad: ", 1, 120);

            double salarioBase = leerDouble(teclado, "Salario base: ", 0, Double.MAX_VALUE);

            Rol rol = pedirRol(teclado);

            int nivelMecanico = leerEntero(teclado, "Nivel mecánico 0-100: ", 0, 100);
            int nivelEstrategico = leerEntero(teclado, "Nivel estratégico 0-100: ", 0, 100);

            Jugador jugador = new Jugador(id, nombre, nickname, edad, salarioBase, rol, nivelMecanico, nivelEstrategico);

            liga.altaPersona(jugador);

            System.out.println("Jugador libre creado correctamente.");

        } catch (PersonaDuplicadaException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static Rol pedirRol(Scanner teclado) {
        System.out.println("Rol:");
        System.out.println("1. TOP");
        System.out.println("2. JUNGLE");
        System.out.println("3. MID");
        System.out.println("4. ADC");
        System.out.println("5. SUPPORT");
        int opcionRol = leerEntero(teclado, "Elige rol: ", 1, 5);

        if (opcionRol == 1) {
            return Rol.TOP;
        } else if (opcionRol == 2) {
            return Rol.JUNGLE;
        } else if (opcionRol == 3) {
            return Rol.MID;
        } else if (opcionRol == 4) {
            return Rol.ADC;
        }

        return Rol.SUPPORT;
    }

    public static void mercadoFichajes(Scanner teclado, Liga liga) {
        System.out.println();
        System.out.println("=== MERCADO DE FICHAJES ===");
        System.out.println("1. Fichar jugador libre como suplente");
        System.out.println("2. Transferir jugador entre equipos");
        int opcion = leerEntero(teclado, "Elige opción: ", 1, 2);

        String idJugador = leerTexto(teclado, "ID del jugador: ");

        String nombreEquipo = leerTexto(teclado, "Nombre del equipo destino: ");

        if (opcion == 1) {
            liga.ficharJugadorLibreComoSuplente(idJugador, nombreEquipo);
        } else if (opcion == 2) {
            liga.transferirJugador(idJugador, nombreEquipo);
        }
    }

    public static String leerTexto(Scanner teclado, String mensaje) {
        String texto;

        do {
            System.out.print(mensaje);
            texto = teclado.nextLine().trim();

            if (texto.equals("")) {
                System.out.println("El texto no puede estar vacío.");
            }
        } while (texto.equals(""));

        return texto;
    }

    public static String leerTextoOpcional(Scanner teclado, String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine().trim();
    }

    public static int leerEntero(Scanner teclado, String mensaje, int minimo, int maximo) {
        while (true) {
            System.out.print(mensaje);
            String entrada = teclado.nextLine().trim();

            try {
                int valor = Integer.parseInt(entrada);

                if (valor < minimo || valor > maximo) {
                    System.out.println("Introduce un número entre " + minimo + " y " + maximo + ".");
                } else {
                    return valor;
                }
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número entero válido.");
            }
        }
    }

    public static double leerDouble(Scanner teclado, String mensaje, double minimo, double maximo) {
        while (true) {
            System.out.print(mensaje);
            String entrada = teclado.nextLine().trim().replace(',', '.');

            try {
                double valor = Double.parseDouble(entrada);

                if (valor < minimo || valor > maximo) {
                    System.out.println("Introduce un número entre " + minimo + " y " + maximo + ".");
                } else {
                    return valor;
                }
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número válido.");
            }
        }
    }

    public static void cargarDatosPrueba(Liga liga) {
        try {
            Equipo equipo1 = crearEquipoCompleto(liga, "Nexus Wolves", "Málaga", "Carlos Gómez", "CoachNova", "Estrategia", "NW", 1, 75);
            Equipo equipo2 = crearEquipoCompleto(liga, "Pixel Dragons", "Sevilla", "Laura Ruiz", "MindCoach", "Draft y análisis", "PD", 10, 73);
            Equipo equipo3 = crearEquipoCompleto(liga, "Cyber Titans", "Madrid", "Pedro Sánchez", "MacroKing", "Juego tardío", "CT", 20, 82);
            Equipo equipo4 = crearEquipoCompleto(liga, "Quantum Foxes", "Valencia", "Marta León", "FastCoach", "Juego agresivo", "QF", 30, 72);
            Equipo equipo5 = crearEquipoCompleto(liga, "Byte Warriors", "Barcelona", "Andrés Romero", "ByteCoach", "Control de mapa", "BW", 40, 78);
            Equipo equipo6 = crearEquipoCompleto(liga, "Storm Raptors", "Granada", "Elena Torres", "StormMind", "Peleas grupales", "SR", 50, 76);

            equipo1.agregarPatrocinador(new Patrocinador("RedBull Arena", 8000));
            equipo1.agregarPatrocinador(new Patrocinador("TechZone", 5000));

            equipo2.agregarPatrocinador(new Patrocinador("HyperByte", 9000));
            equipo2.agregarPatrocinador(new Patrocinador("GameFuel", 4000));

            equipo3.agregarPatrocinador(new Patrocinador("Nexus Energy", 12000));
            equipo3.agregarPatrocinador(new Patrocinador("ProGaming Store", 7000));

            equipo4.agregarPatrocinador(new Patrocinador("FoxWear", 6000));
            equipo4.agregarPatrocinador(new Patrocinador("CloudPC", 4500));

            equipo5.agregarPatrocinador(new Patrocinador("Aorus Stage", 7500));
            equipo5.agregarPatrocinador(new Patrocinador("ClickZone", 3500));

            equipo6.agregarPatrocinador(new Patrocinador("Storm Energy", 8500));
            equipo6.agregarPatrocinador(new Patrocinador("ArenaWear", 4200));

            liga.crearPartido("P1", 1, equipo1, equipo2);
            liga.crearPartido("P2", 1, equipo3, equipo4);
            liga.crearPartido("P3", 1, equipo5, equipo6);

            liga.crearPartido("P4", 2, equipo1, equipo3);
            liga.crearPartido("P5", 2, equipo2, equipo5);
            liga.crearPartido("P6", 2, equipo4, equipo6);

            liga.crearPartido("P7", 3, equipo1, equipo5);
            liga.crearPartido("P8", 3, equipo2, equipo4);
            liga.crearPartido("P9", 3, equipo3, equipo6);

        } catch (PersonaDuplicadaException | EquipoDuplicadoException | RolNoDisponibleException | PartidoInvalidoException e) {
            System.out.println("Error cargando datos de prueba: " + e.getMessage());
        }
    }

    public static Equipo crearEquipoCompleto(Liga liga, String nombreEquipo, String ciudad,
                                             String nombreEntrenador, String nickEntrenador,
                                             String especialidad, String prefijo, int inicioId,
                                             int nivelBase)
            throws PersonaDuplicadaException, EquipoDuplicadoException, RolNoDisponibleException {

        Entrenador entrenador = new Entrenador("E" + prefijo, nombreEntrenador, nickEntrenador, 35, 1800, 7, especialidad);
        liga.altaPersona(entrenador);

        Equipo equipo = new Equipo(nombreEquipo, ciudad, 25000);
        equipo.asignarEntrenador(entrenador);

        Jugador top = new Jugador("J" + (inicioId), "Jugador " + prefijo + " TOP", prefijo + "Top", 20, 1200, Rol.TOP, nivelBase, nivelBase - 2);
        Jugador jungle = new Jugador("J" + (inicioId + 1), "Jugador " + prefijo + " JUNGLE", prefijo + "Jungle", 21, 1200, Rol.JUNGLE, nivelBase + 1, nivelBase);
        Jugador mid = new Jugador("J" + (inicioId + 2), "Jugador " + prefijo + " MID", prefijo + "Mid", 19, 1300, Rol.MID, nivelBase + 3, nivelBase);
        Jugador adc = new Jugador("J" + (inicioId + 3), "Jugador " + prefijo + " ADC", prefijo + "ADC", 22, 1250, Rol.ADC, nivelBase + 2, nivelBase - 1);
        Jugador support = new Jugador("J" + (inicioId + 4), "Jugador " + prefijo + " SUPPORT", prefijo + "Support", 23, 1150, Rol.SUPPORT, nivelBase - 4, nivelBase + 5);

        Jugador suplente1 = new Jugador("J" + (inicioId + 5), "Suplente " + prefijo + " MID", prefijo + "SubMid", 20, 1000, Rol.MID, nivelBase - 5, nivelBase - 3);
        Jugador suplente2 = new Jugador("J" + (inicioId + 6), "Suplente " + prefijo + " SUPPORT", prefijo + "SubSupport", 21, 1000, Rol.SUPPORT, nivelBase - 6, nivelBase);

        liga.altaPersona(top);
        liga.altaPersona(jungle);
        liga.altaPersona(mid);
        liga.altaPersona(adc);
        liga.altaPersona(support);
        liga.altaPersona(suplente1);
        liga.altaPersona(suplente2);

        equipo.ficharTitular(top);
        equipo.ficharTitular(jungle);
        equipo.ficharTitular(mid);
        equipo.ficharTitular(adc);
        equipo.ficharTitular(support);

        equipo.ficharSuplente(suplente1);
        equipo.ficharSuplente(suplente2);

        liga.crearEquipo(equipo);

        return equipo;
    }
}
