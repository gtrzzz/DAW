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
            System.out.println("2. Mostrar jugadores");
            System.out.println("3. Mostrar equipos");
            System.out.println("4. Mostrar plantillas completas");
            System.out.println("5. Crear jugador libre");
            System.out.println("6. Mercado de fichajes");
            System.out.println("7. Mostrar calendario");
            System.out.println("8. Ver siguiente partido");
            System.out.println("9. Apostar al siguiente partido");
            System.out.println("10. Disputar siguiente partido");
            System.out.println("11. Mostrar clasificación");
            System.out.println("12. Mostrar eventos");
            System.out.println("13. Mostrar historial");
            System.out.println("14. Deshacer última acción");
            System.out.println("15. Entregar premios finales");
            System.out.println("16. Salir");
            opcion = leerEntero(teclado, "Elige una opción: ", 1, 16);

            if (opcion == 1) {
                liga.mostrarPersonas();

            } else if (opcion == 2) {
                liga.mostrarJugadores();

            } else if (opcion == 3) {
                liga.mostrarEquipos();

            } else if (opcion == 4) {
                for (Equipo equipo : liga.getEquipos()) {
                    equipo.mostrarPlantilla();
                    System.out.println("----------------------");
                }

            } else if (opcion == 5) {
                crearJugadorPorTeclado(teclado, liga);

            } else if (opcion == 6) {
                mercadoFichajes(teclado, liga);

            } else if (opcion == 7) {
                liga.mostrarCalendario();

            } else if (opcion == 8) {
                liga.verSiguientePartido();

            } else if (opcion == 9) {
                apostarPorTeclado(teclado, liga);

            } else if (opcion == 10) {
                gestionarInicioPartido(teclado, liga);

            } else if (opcion == 11) {
                liga.mostrarClasificacion();

            } else if (opcion == 12) {
                liga.mostrarEventos();

            } else if (opcion == 13) {
                liga.mostrarHistorial();

            } else if (opcion == 14) {
                liga.deshacerUltimaAccion();

            } else if (opcion == 15) {
                liga.entregarPremiosFinales();

            } else if (opcion == 16) {
                System.out.println("Saliendo del programa...");

            } else {
                System.out.println("Opción no válida.");
            }

        } while (opcion != 16);

        teclado.close();
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
