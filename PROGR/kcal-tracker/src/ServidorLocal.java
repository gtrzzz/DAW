import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class ServidorLocal {

    private final int puerto;
    private final RepositorioAtletas repositorioAtletas;
    private final CalculadoraNutricion calculadoraNutricion;
    private final DecimalFormat formatoDecimal;

    public ServidorLocal(int puerto) {
        this.puerto = puerto;
        this.repositorioAtletas = new RepositorioAtletas(Paths.get("datos", "atletas.bin"));
        this.calculadoraNutricion = new CalculadoraNutricion();
        this.formatoDecimal = new DecimalFormat("0.##");
    }

    public void iniciar() throws IOException {
        HttpServer servidor = HttpServer.create(new InetSocketAddress(puerto), 0);
        servidor.createContext("/", this::manejarPeticion);
        servidor.setExecutor(null);
        servidor.start();
        System.out.println("Kcal Tracker iniciado en http://localhost:" + puerto);
    }

    private void manejarPeticion(HttpExchange intercambio) throws IOException {
        try {
            String ruta = intercambio.getRequestURI().getPath();
            String metodo = intercambio.getRequestMethod();

            if ("GET".equals(metodo) && "/".equals(ruta)) {
                responder(intercambio, 200, construirInicio());
                return;
            }
            if ("GET".equals(metodo) && "/buscar".equals(ruta)) {
                buscarAtletaPorId(intercambio);
                return;
            }
            if ("GET".equals(metodo) && "/atleta/nuevo".equals(ruta)) {
                responder(intercambio, 200, construirFormularioAtleta());
                return;
            }
            if ("POST".equals(metodo) && "/atleta/guardar".equals(ruta)) {
                guardarAtleta(intercambio);
                return;
            }
            if ("GET".equals(metodo) && ruta.startsWith("/nutricion/cuestionario/")) {
                mostrarCuestionarioNutricion(intercambio, obtenerIdDesdeRuta(ruta));
                return;
            }
            if ("POST".equals(metodo) && ruta.startsWith("/nutricion/guardar/")) {
                guardarCuestionarioNutricion(intercambio, obtenerIdDesdeRuta(ruta));
                return;
            }
            if ("GET".equals(metodo) && ruta.startsWith("/atleta/")) {
                mostrarAtleta(intercambio, obtenerIdDesdeRuta(ruta));
                return;
            }
            if ("POST".equals(metodo) && ruta.startsWith("/medicion/guardar/")) {
                guardarMedicion(intercambio, obtenerIdDesdeRuta(ruta));
                return;
            }
            if ("POST".equals(metodo) && ruta.startsWith("/valoracion/guardar/")) {
                guardarValoracion(intercambio, obtenerIdDesdeRuta(ruta));
                return;
            }

            responder(intercambio, 404, construirPagina("No encontrado", "<p>Ruta no encontrada.</p>"));
        } catch (Exception e) {
            responder(intercambio, 500, construirPagina("Error", "<p>" + escapar(e.getMessage()) + "</p>"));
        }
    }

    private void guardarAtleta(HttpExchange intercambio) throws IOException {
        Formulario formulario = new Formulario(leerCuerpo(intercambio));
        Objetivos objetivos = new Objetivos(
                formulario.texto("objetivosCorporales"),
                formulario.texto("objetivosSalud"),
                formulario.texto("objetivosRendimiento"),
                formulario.texto("objetivosFuncionales"),
                formulario.texto("objetivosPsicologicos"),
                formulario.enumerado("tipoObjetivo", TipoObjetivo.class, TipoObjetivo.MANTENIMIENTO),
                formulario.enumerado("nivelActividad", NivelActividad.class, NivelActividad.SEDENTARIO),
                formulario.enumerado("fisicoObjetivo", FisicoObjetivo.class, FisicoObjetivo.MANTENIMIENTO),
                formulario.decimal("cambioPesoSemanalKg"));
        DatosDiaADia datosDiaADia = new DatosDiaADia(
                formulario.entero("pasosDiarios"),
                formulario.decimal("horasInactividad"),
                formulario.decimal("horasTrabajo"),
                formulario.decimal("horasEntrenamiento"),
                formulario.decimal("horasSueno"),
                formulario.entero("calidadSueno"),
                formulario.entero("estres"),
                formulario.entero("numeroComidas"),
                formulario.enumerado("tipoDieta", TipoDieta.class, TipoDieta.NORMAL),
                formulario.texto("medicacion"));
        PreferenciasNutricion preferenciasNutricion = new PreferenciasNutricion("", "", "", "", "", "", "", "", "", 0, 0, false, false);

        Atleta atleta = new Atleta(
                repositorioAtletas.obtenerSiguienteId(),
                formulario.texto("nombre"),
                formulario.fecha("fechaNacimiento"),
                formulario.enumerado("sexo", Sexo.class, Sexo.HOMBRE),
                formulario.decimal("alturaCm"),
                formulario.decimal("pesoKg"),
                formulario.decimal("porcentajeGrasa"),
                formulario.decimal("vo2Max"),
                formulario.decimal("indiceRuffier"),
                formulario.texto("trabajo"),
                formulario.enumerado("estadoCivil", EstadoCivil.class, EstadoCivil.OTRO),
                formulario.texto("experienciaPrevia"),
                objetivos,
                datosDiaADia,
                preferenciasNutricion,
                formulario.texto("observaciones"));

        agregarElementos(atleta, formulario.texto("lesiones"), "lesion");
        agregarElementos(atleta, formulario.texto("enfermedades"), "enfermedad");
        agregarElementos(atleta, formulario.texto("alergias"), "alergia");
        atleta.agregarMedicion(crearMedicionInicial(formulario));
        repositorioAtletas.guardarAtleta(atleta);
        redirigir(intercambio, "/atleta/" + atleta.getId());
    }

    private MedicionCorporal crearMedicionInicial(Formulario formulario) {
        return new MedicionCorporal(
                formulario.fecha("fechaMedicion"),
                formulario.decimal("pesoKg"),
                formulario.decimal("porcentajeGrasa"),
                formulario.decimal("bicepsCm"),
                formulario.decimal("pechoCm"),
                formulario.decimal("cinturaCm"),
                formulario.decimal("caderaCm"),
                formulario.decimal("musloCm"),
                formulario.decimal("gemeloCm"),
                "Medición inicial");
    }

    private void buscarAtletaPorId(HttpExchange intercambio) throws IOException {
        String consulta = intercambio.getRequestURI().getRawQuery();
        Formulario formulario = new Formulario(consulta);
        int id = formulario.entero("id");
        if (id <= 0 || repositorioAtletas.buscarAtleta(id) == null) {
            responder(intercambio, 404, construirPagina("Cliente no encontrado",
                    "<a class='volver' href='/'>Volver</a><section class='tarjeta'><h1>Cliente no encontrado</h1><p>No existe ningún cliente registrado con ese ID.</p></section>"));
            return;
        }
        redirigir(intercambio, "/atleta/" + id);
    }

    private void guardarMedicion(HttpExchange intercambio, int id) throws IOException {
        Atleta atleta = repositorioAtletas.buscarAtleta(id);
        if (atleta == null) {
            responder(intercambio, 404, construirPagina("No encontrado", "<p>Atleta no encontrado.</p>"));
            return;
        }
        Formulario formulario = new Formulario(leerCuerpo(intercambio));
        atleta.agregarMedicion(new MedicionCorporal(
                formulario.fecha("fecha"),
                formulario.decimal("pesoKg"),
                formulario.decimal("porcentajeGrasa"),
                formulario.decimal("bicepsCm"),
                formulario.decimal("pechoCm"),
                formulario.decimal("cinturaCm"),
                formulario.decimal("caderaCm"),
                formulario.decimal("musloCm"),
                formulario.decimal("gemeloCm"),
                formulario.texto("observaciones")));
        repositorioAtletas.actualizarAtletas();
        redirigir(intercambio, "/atleta/" + id + "#mediciones");
    }

    private void guardarValoracion(HttpExchange intercambio, int id) throws IOException {
        Atleta atleta = repositorioAtletas.buscarAtleta(id);
        if (atleta == null) {
            responder(intercambio, 404, construirPagina("No encontrado", "<p>Atleta no encontrado.</p>"));
            return;
        }
        Formulario formulario = new Formulario(leerCuerpo(intercambio));
        atleta.agregarValoracion(new ValoracionSemanal(
                formulario.fecha("fecha"),
                formulario.texto("mesociclo"),
                formulario.texto("microciclo"),
                formulario.entero("sesionesCompletadas"),
                formulario.entero("cansancio"),
                formulario.entero("recuperacion"),
                formulario.decimal("pesoEnAyunasKg"),
                formulario.entero("hambre"),
                formulario.entero("saciedad"),
                formulario.entero("hidratacion"),
                formulario.entero("seguimientoPlan"),
                formulario.decimal("horasSueno"),
                formulario.entero("calidadSueno"),
                formulario.entero("estres"),
                formulario.entero("motivacion"),
                formulario.texto("logroSemanal"),
                formulario.texto("mejoraSemanal"),
                formulario.texto("comentarioEntrenamiento"),
                formulario.texto("comentarioNutricion"),
                formulario.texto("mejoraEntrenador"),
                formulario.texto("observaciones")));
        repositorioAtletas.actualizarAtletas();
        redirigir(intercambio, "/atleta/" + id + "#valoracion");
    }

    private void mostrarCuestionarioNutricion(HttpExchange intercambio, int id) throws IOException {
        Atleta atleta = repositorioAtletas.buscarAtleta(id);
        if (atleta == null) {
            responder(intercambio, 404, construirPagina("No encontrado", "<p>Atleta no encontrado.</p>"));
            return;
        }
        responder(intercambio, 200, construirCuestionarioNutricion(atleta));
    }

    private void guardarCuestionarioNutricion(HttpExchange intercambio, int id) throws IOException {
        Atleta atleta = repositorioAtletas.buscarAtleta(id);
        if (atleta == null) {
            responder(intercambio, 404, construirPagina("No encontrado", "<p>Atleta no encontrado.</p>"));
            return;
        }
        Formulario formulario = new Formulario(leerCuerpo(intercambio));
        Map<String, String> respuestas = formulario.todos();
        PreferenciasNutricion preferencias = new PreferenciasNutricion(
                construirTop10DesdeFormulario(formulario),
                formulario.texto("alimentosExcluidosExtra"),
                formulario.texto("bebidasHabituales"),
                formulario.texto("proteina_compra") + " " + formulario.texto("proteina_sabores"),
                formulario.texto("logistica_no_cocinar") + " " + formulario.texto("hambre_hora"),
                formulario.texto("cocina_tiempo") + " " + formulario.texto("cocina_batidora"),
                formulario.texto("logistica_presupuesto"),
                formulario.texto("digestiones") + " " + formulario.texto("hambre_dificil"),
                formulario.texto("carbo_favorito") + " " + formulario.texto("cocina_preferencia"),
                formulario.entero("hambreNivel"),
                formulario.decimal("aguaLitros"),
                !"No tomo café".equals(formulario.texto("snack_cafe")),
                formulario.booleano("consumeAlcohol"));
        preferencias.guardarCuestionario(respuestas);
        atleta.actualizarPreferenciasNutricion(preferencias);
        repositorioAtletas.actualizarAtletas();
        redirigir(intercambio, "/atleta/" + id + "#nutricion");
    }

    private void mostrarAtleta(HttpExchange intercambio, int id) throws IOException {
        Atleta atleta = repositorioAtletas.buscarAtleta(id);
        if (atleta == null) {
            responder(intercambio, 404, construirPagina("No encontrado", "<p>Atleta no encontrado.</p>"));
            return;
        }
        responder(intercambio, 200, construirDetalleAtleta(atleta));
    }

    private String construirInicio() {
        StringBuilder html = new StringBuilder();
        html.append("<section class='hero'><div><p class='eyebrow'>Aplicación local</p><h1>Kcal Tracker</h1>");
        html.append("<p>Gestión de atletas, nutrición, mediciones y seguimiento semanal sin depender de Excel.</p></div>");
        html.append("<a class='boton principal' href='/atleta/nuevo'>Dar de alta atleta</a></section>");
        html.append("<section class='tarjeta acceso-id'><h2>Entrar por ID</h2><form method='get' action='/buscar' class='buscador'><label>ID del cliente<input name='id' type='number' min='1' required></label><button class='boton' type='submit'>Abrir cliente</button></form></section>");
        html.append("<section class='tarjeta'><h2>Atletas</h2>");
        List<Atleta> atletas = repositorioAtletas.listarAtletas();
        if (atletas.isEmpty()) {
            html.append("<p>Todavía no hay atletas registrados.</p>");
        } else {
            html.append("<div class='lista'>");
            for (Atleta atleta : atletas) {
                html.append("<a class='item' href='/atleta/").append(atleta.getId()).append("'><strong>")
                        .append("#").append(atleta.getId()).append(" · ").append(escapar(atleta.getNombre())).append("</strong><span>")
                        .append(escapar(etiquetaEnum(atleta.getObjetivos().getTipoObjetivo()))).append(" · ")
                        .append(escapar(etiquetaEnum(atleta.getObjetivos().getNivelActividad()))).append("</span></a>");
            }
            html.append("</div>");
        }
        html.append("</section>");
        return construirPagina("Kcal Tracker", html.toString());
    }

    private String construirFormularioAtleta() {
        StringBuilder html = new StringBuilder();
        html.append("<form class='tarjeta' method='post' action='/atleta/guardar'><h1>Cuestionario inicial</h1>");
        html.append("<div class='grid'><fieldset><legend>Datos personales</legend>");
        campo(html, "nombre", "Nombre", "text");
        campo(html, "fechaNacimiento", "Fecha de nacimiento", "date");
        select(html, "sexo", "Sexo", Sexo.values());
        campo(html, "trabajo", "Trabajo", "text");
        select(html, "estadoCivil", "Estado civil", EstadoCivil.values());
        area(html, "experienciaPrevia", "Experiencia previa");
        html.append("</fieldset><fieldset><legend>Datos físicos</legend>");
        campo(html, "alturaCm", "Altura (cm)", "number step='0.01'");
        campo(html, "pesoKg", "Peso (kg)", "number step='0.01'");
        campo(html, "porcentajeGrasa", "% grasa corporal", "number step='0.01'");
        html.append("<p class='ayuda ancho'><strong>Test de Ruffier:</strong> mide pulsaciones en reposo durante 15 s y multiplica x4 (P1), haz 30 sentadillas en 45 s, mide pulsaciones justo al terminar (P2) y tras 1 minuto (P3). Índice = (P1 + P2 + P3 - 200) / 10.</p>");
        campo(html, "indiceRuffier", "Índice Ruffier", "number step='0.01'");
        html.append("<p class='ayuda ancho'><strong>VO2 máximo sencillo:</strong> test de Cooper. Corre la máxima distancia posible en 12 minutos y estima VO2 = (metros recorridos - 504.9) / 44.73. Si haces Course Navette, introduce el VO2 estimado de la tabla/app.</p>");
        campo(html, "vo2Max", "VO2 máximo estimado", "number step='0.01'");
        area(html, "lesiones", "Lesiones (separadas por comas)");
        area(html, "enfermedades", "Enfermedades (separadas por comas)");
        area(html, "alergias", "Alergias (separadas por comas)");
        html.append("</fieldset><fieldset><legend>Medidas antropométricas</legend>");
        campo(html, "fechaMedicion", "Fecha medición", "date");
        campo(html, "bicepsCm", "Bíceps (cm)", "number step='0.01'");
        campo(html, "pechoCm", "Pecho (cm)", "number step='0.01'");
        campo(html, "cinturaCm", "Cintura (cm)", "number step='0.01'");
        campo(html, "caderaCm", "Cadera (cm)", "number step='0.01'");
        campo(html, "musloCm", "Muslo (cm)", "number step='0.01'");
        campo(html, "gemeloCm", "Gemelo (cm)", "number step='0.01'");
        html.append("</fieldset><fieldset><legend>Objetivos</legend>");
        select(html, "tipoObjetivo", "Contexto nutricional", TipoObjetivo.values());
        select(html, "nivelActividad", "Nivel de actividad", NivelActividad.values());
        select(html, "fisicoObjetivo", "Físico objetivo", FisicoObjetivo.values());
        campo(html, "cambioPesoSemanalKg", "Cambio peso semanal kg (+ ganar / - perder)", "number step='0.01'");
        area(html, "objetivosCorporales", "Corporales");
        area(html, "objetivosSalud", "Salud");
        area(html, "objetivosRendimiento", "Rendimiento");
        area(html, "objetivosFuncionales", "Funcionales");
        area(html, "objetivosPsicologicos", "Psicológicos");
        html.append("</fieldset><fieldset><legend>Día a día</legend>");
        campo(html, "pasosDiarios", "Pasos diarios", "number");
        campo(html, "horasInactividad", "Horas inactividad", "number step='0.01'");
        campo(html, "horasTrabajo", "Horas trabajo", "number step='0.01'");
        campo(html, "horasEntrenamiento", "Horas entrenamiento", "number step='0.01'");
        campo(html, "horasSueno", "Horas de sueño", "number step='0.01'");
        campo(html, "calidadSueno", "Calidad del sueño (0-10)", "number min='0' max='10'");
        campo(html, "estres", "Estrés (0-10)", "number min='0' max='10'");
        campo(html, "numeroComidas", "Número de comidas", "number");
        select(html, "tipoDieta", "Tipo de dieta", TipoDieta.values());
        campo(html, "medicacion", "Medicación", "text");
        html.append("</fieldset></div>");
        area(html, "observaciones", "Observaciones");
        html.append("<button class='boton principal' type='submit'>Guardar atleta</button></form>");
        return construirPagina("Nuevo atleta", html.toString());
    }

    private String construirDetalleAtleta(Atleta atleta) {
        ResultadoNutricion nutricion = calculadoraNutricion.calcularPara(atleta);
        StringBuilder html = new StringBuilder();
        html.append("<a class='volver' href='/'>Volver</a><section class='hero compacto'><div><p class='eyebrow'>Cliente #")
                .append(atleta.getId()).append("</p><h1>")
                .append(escapar(atleta.getNombre())).append("</h1><p>")
                .append(escapar(etiquetaEnum(atleta.getObjetivos().getTipoObjetivo()))).append(" · ")
                .append(escapar(etiquetaEnum(atleta.getObjetivos().getNivelActividad()))).append("</p></div></section>");
        html.append(construirNutricion(nutricion));
        if (atleta.getPreferenciasNutricion().isCuestionarioCompleto()) {
            html.append(construirPlanNutricion(calculadoraNutricion.generarPlanPara(atleta), atleta));
        } else {
            html.append(construirNutricionPendiente(atleta));
        }
        html.append(construirMediciones(atleta));
        html.append(construirValoraciones(atleta));
        return construirPagina(atleta.getNombre(), html.toString());
    }

    private String construirNutricion(ResultadoNutricion nutricion) {
        StringBuilder html = new StringBuilder();
        html.append("<section id='nutricion' class='tarjeta'><h2>Nutrición</h2><div class='metricas'>");
        metrica(html, "Kcal reposo", nutricion.getMetabolismoReposo());
        metrica(html, "Gasto total", nutricion.getGastoTotal());
        metrica(html, "Kcal objetivo", nutricion.getCaloriasObjetivo());
        metrica(html, "Proteínas (g)", nutricion.getGramosProteinas());
        metrica(html, "Grasas (g)", nutricion.getGramosGrasas());
        metrica(html, "Carbohidratos (g)", nutricion.getGramosCarbohidratos());
        html.append("</div></section>");
        return html.toString();
    }

    private String construirNutricionPendiente(Atleta atleta) {
        StringBuilder html = new StringBuilder();
        html.append("<section id='plan-nutricion' class='tarjeta aviso'><h2>Plan personalizado pendiente</h2>");
        html.append("<p>Para este cliente todavía no se ha completado el cuestionario de alimentos. Es obligatorio hacerlo la primera vez para poder calcular menús coherentes con gustos, logística, hambre y tolerancias.</p>");
        html.append("<a class='boton' href='/nutricion/cuestionario/").append(atleta.getId()).append("'>Abrir cuestionario nutricional</a>");
        html.append("</section>");
        return html.toString();
    }

    private String construirCuestionarioNutricion(Atleta atleta) {
        StringBuilder html = new StringBuilder();
        html.append("<a class='volver' href='/atleta/").append(atleta.getId()).append("'>Volver</a>");
        html.append("<form class='tarjeta cuestionario' method='post' action='/nutricion/guardar/").append(atleta.getId()).append("'>");
        html.append("<h1>Cuestionario nutricional de ").append(escapar(atleta.getNombre())).append("</h1>");
        html.append("<p>Completa las 14 secciones. Las respuestas se usan para priorizar alimentos, excluir lo que no encaja y construir menús sencillos que cuadren con los macros.</p>");

        seccion(html, "① Proteínas animales", "Carnes y aves");
        tablaValoracion(html, "Alimento", "Tu valoración", new String[][] {
                { "food_pechuga_pollo", "Pechuga de pollo" }, { "food_muslo_pollo", "Muslo de pollo sin piel" },
                { "food_pavo", "Pavo filete/pechuga" }, { "food_ternera_picada_magra", "Carne picada de ternera 90% magra" },
                { "food_filete_ternera", "Filete de ternera plancha" }, { "food_lomo_cerdo", "Lomo de cerdo magro" },
                { "food_jamon_york_pavo", "Jamón york/pavo en lonchas" } },
                new String[] { "Me gusta mucho", "Lo tolero", "No me gusta", "Solo si..." });
        tablaValoracion(html, "Método", "Tu valoración", new String[][] {
                { "prep_plancha", "Plancha" }, { "prep_airfryer", "Airfryer" }, { "prep_horno", "Horno" },
                { "prep_guiso", "Guiso/estofado" }, { "prep_salteado", "Salteado en sartén con verduras" } },
                new String[] { "Me encanta", "OK", "No" });
        area(html, "condicionCarnes", "Condición especial para carnes");

        seccion(html, "② Pescados y mariscos", "Pescados, mariscos y conservas");
        tablaValoracion(html, "Alimento", "Tu valoración", new String[][] {
                { "food_atun_fresco", "Atún fresco" }, { "food_atun_lata", "Atún en lata al natural" },
                { "food_salmon", "Salmón" }, { "food_caballa", "Caballa en lata" },
                { "food_sardinas", "Sardinas en lata" }, { "food_lubina", "Lubina" },
                { "food_sepia_calamar", "Sepia/calamar" }, { "food_gambas", "Gambas" },
                { "food_mejillones", "Mejillones" }, { "food_surimi", "Palitos de cangrejo/surimi" } },
                new String[] { "Me gusta mucho", "Lo tolero", "No me gusta", "Solo si..." });
        radioGrupo(html, "pescado_metodo", "¿Plancha o airfryer para pescados?", new String[] { "Plancha siempre", "Airfryer siempre", "Me da igual ambos", "Depende del pescado" });
        radioGrupo(html, "pescado_frecuencia", "¿Cada cuántos días podrías comer pescado sin cansarte?", new String[] { "Cada día sin problema", "Máx cada 2 días", "Máx 3 veces/semana", "1 vez/semana" });
        radioGrupo(html, "pescado_protagonistas", "Alternar atún/salmón como protagonistas", new String[] { "Perfecto, los prefiero", "Quiero más variedad", "Solo atún", "Solo salmón" });
        area(html, "pescadoNotas", "Algo más sobre pescados");

        seccion(html, "③ Huevos y lácteos", "Huevos y productos lácteos");
        tablaValoracion(html, "Alimento", "Tu valoración", new String[][] {
                { "food_huevos_revueltos", "Huevos revueltos" }, { "food_tortilla", "Huevos en tortilla francesa" },
                { "food_huevos_duros", "Huevos duros" }, { "food_huevos_plancha", "Huevos a la plancha" },
                { "food_claras", "Claras de huevo solas" }, { "food_yogur_griego", "Yogur griego 0%" },
                { "food_skyr", "Skyr natural" }, { "food_queso_batido", "Queso fresco batido 0%" },
                { "food_leche_desnatada", "Leche desnatada" }, { "food_requeson", "Requesón" },
                { "food_cottage", "Queso cottage" } },
                new String[] { "Me gusta mucho", "Lo tolero", "No me gusta", "Solo si..." });
        tablaValoracion(html, "Añadido", "¿Lo usarías?", new String[][] {
                { "add_edulcorante", "Edulcorante" }, { "add_fruta", "Fruta troceada" }, { "add_miel", "Miel si entra en macros" },
                { "add_mermelada0", "Mermelada 0% kcal" }, { "add_canela_cacao", "Canela + cacao puro" } },
                new String[] { "Sí", "Quizás", "No" });
        radioGrupo(html, "huevos_max", "Máximo de huevos enteros al día", new String[] { "Máx 1", "Máx 2", "Máx 3", "Más de 3" });

        seccion(html, "④ Proteína en polvo", "Uso y tolerancia");
        radioGrupo(html, "proteina_batidora", "¿Tienes batidora?", new String[] { "Batidora de vaso", "Batidora de brazo", "Solo coctelera/shaker", "No tengo batidora" });
        radioGrupo(html, "proteina_base", "Base para mezclar", new String[] { "Leche desnatada", "Agua", "Bebida vegetal", "Con yogur" });
        radioGrupo(html, "proteina_frecuencia", "Si no hubiera grumos, frecuencia", new String[] { "Prefiero evitarla", "1 vez/día", "2 veces/día" });
        radioGrupo(html, "proteina_avena", "¿Añadirla a avena/porridge?", new String[] { "Sí", "No lo he probado", "No" });
        radioGrupo(html, "proteina_yogur", "¿Mezclarla en yogur/queso batido?", new String[] { "Sí", "Quizás", "No" });
        radioGrupo(html, "proteina_compra", "¿Comprar otro sabor si hace falta?", new String[] { "Sí, sin problema", "Quizás", "Prefiero no gastar más" });
        checks(html, "proteina_sabores", "Sabores tolerados", new String[] { "Chocolate", "Vainilla", "Neutro", "Cookies & cream", "Fresa" });

        seccion(html, "⑤ Carbohidratos", "Fuentes de carbohidratos");
        tablaValoracion(html, "Alimento", "Tu valoración", new String[][] {
                { "food_arroz_blanco", "Arroz blanco cocido" }, { "food_arroz_integral", "Arroz integral" },
                { "food_patata_cocida", "Patata cocida" }, { "food_patata_asada", "Patata asada" },
                { "food_boniato", "Boniato/batata" }, { "food_pasta", "Pasta" }, { "food_avena", "Avena en copos" },
                { "food_pan_integral", "Pan integral" }, { "food_legumbres_frias", "Legumbres en ensalada fría" } },
                new String[] { "Me gusta mucho", "Lo tolero", "No me gusta" });
        radioGrupo(html, "legumbres_limite", "¿Evitas platos de cuchara o legumbres en general?", new String[] { "Solo evito guisos calientes", "Evito cualquier legumbre", "En ensalada fría sí" });
        radioGrupo(html, "carbo_favorito", "HC favorito del día a día", new String[] { "Arroz", "Pasta", "Patata", "Avena", "Pan", "Boniato" });
        radioGrupo(html, "carbo_repeticion", "¿Repetirías el mismo HC 14 días?", new String[] { "Sí, prefiero simplicidad", "Solo 2-3 opciones rotando", "Quiero variedad cada día" });

        seccion(html, "⑥ Verduras enteras y en puré", "Verduras, textura y temperatura");
        tablaValoracion(html, "Verdura", "Entera / puré", new String[][] {
                { "food_calabaza", "Calabaza" }, { "food_zanahoria", "Zanahoria" }, { "food_espinacas", "Espinacas" },
                { "food_calabacin", "Calabacín" }, { "food_pimiento", "Pimiento rojo/verde" }, { "food_champinones", "Champiñones/setas" },
                { "food_lechuga_rucula", "Lechuga/rúcula" }, { "food_pepino", "Pepino" }, { "food_tomate", "Tomate morado/negro" },
                { "food_cebolla", "Cebolla cocinada" }, { "food_brocoli", "Brócoli" } },
                new String[] { "Entera sí", "En puré sí", "Ambas", "Prefiero evitar" });
        radioGrupo(html, "pure_frecuencia", "Frecuencia de crema/puré de verduras como cena", new String[] { "Prefiero evitarlo", "1-2 veces/semana", "3-4 veces/semana", "Todos los días si es bueno" });
        radioGrupo(html, "verdura_temperatura", "Temperatura preferida", new String[] { "Siempre calientes", "También frías", "Mezcla" });

        seccion(html, "⑦ Frutas", "Frutas y momento del día");
        tablaValoracion(html, "Fruta", "Tu valoración", new String[][] {
                { "food_fresas", "Fresas/fresones" }, { "food_arandanos", "Arándanos" }, { "food_manzana", "Manzana" },
                { "food_platano", "Plátano" }, { "food_naranja", "Naranja/mandarina" }, { "food_melocoton", "Melocotón/nectarina" },
                { "food_sandia_melon", "Sandía/melón" }, { "food_pina", "Piña natural" }, { "food_kiwi", "Kiwi" } },
                new String[] { "Me gusta mucho", "Lo tolero", "No me gusta" });
        radioGrupo(html, "fruta_momento", "Momento preferido para fruta", new String[] { "Solo desayuno", "Solo merienda", "Como postre", "Cualquier momento" });

        seccion(html, "⑧ Grasas saludables", "Fuentes de grasa");
        tablaValoracion(html, "Fuente", "Tu valoración", new String[][] {
                { "food_aove", "Aceite de oliva virgen extra" }, { "food_aguacate", "Aguacate" },
                { "food_nueces", "Nueces" }, { "food_almendras", "Almendras" }, { "food_mantequilla_cacahuete", "Mantequilla de cacahuete" } },
                new String[] { "Me gusta mucho", "Lo tolero", "No me gusta", "Imprescindible" });

        seccion(html, "⑨ Salsas y condimentos", "Sabor y adherencia");
        tablaValoracion(html, "Producto", "Tu valoración", new String[][] {
                { "cond_sal_pimienta", "Sal + pimienta negra" }, { "cond_ajo", "Ajo" }, { "cond_limon", "Limón" },
                { "cond_soja", "Salsa de soja baja en sal" }, { "cond_tomate", "Salsa de tomate natural" },
                { "cond_mostaza", "Mostaza sin azúcar" }, { "cond_vinagre", "Vinagre/vinagreta ligera" },
                { "cond_picante", "Tabasco/salsa picante" }, { "cond_canela", "Canela" }, { "cond_hierbas", "Orégano/tomillo/albahaca" } },
                new String[] { "Me gusta mucho", "OK", "No" });
        radioGrupo(html, "sabor_importancia", "Importancia del sabor", new String[] { "Crítico", "Importante pero me adapto", "Funcional" });

        seccion(html, "⑩ Snacks", "Control del hambre");
        tablaValoracion(html, "Snack", "¿Lo usarías?", new String[][] {
                { "snack_gelatina", "Gelatina 0%" }, { "snack_pepino_zanahoria", "Pepino/zanahoria cruda" },
                { "snack_refrescos_zero", "Refrescos Zero" }, { "snack_infusiones", "Infusiones" },
                { "snack_chicles", "Chicles sin azúcar" }, { "snack_palomitas", "Palomitas naturales" },
                { "snack_caldo", "Caldo desgrasado" } },
                new String[] { "Sí", "Solo si tengo mucha hambre", "No" });
        radioGrupo(html, "snack_cafe", "Café", new String[] { "Cortado con leche", "Café con leche", "Solo", "No tomo café" });
        area(html, "hambreEntreComidas", "¿Qué haces cuando entra mucha hambre entre comidas?");

        seccion(html, "⑪ Cocina", "Utensilios y métodos");
        tablaValoracion(html, "Utensilio", "¿Tienes/usas?", new String[][] {
                { "cocina_airfryer", "Airfryer" }, { "cocina_plancha", "Plancha/sartén" }, { "cocina_horno", "Horno" },
                { "cocina_microondas", "Microondas" }, { "cocina_olla", "Olla/cazuela" } },
                new String[] { "Sí, lo uso", "Tengo pero poco", "No" });
        radioGrupo(html, "cocina_batidora", "Batidora", new String[] { "Vaso", "Brazo", "Ambas", "No" });
        radioGrupo(html, "cocina_tiempo", "Tiempo máximo por comida", new String[] { "5 min máx", "10-15 min", "Hasta 30 min", "Batch cooking" });
        radioGrupo(html, "cocina_preferencia", "¿Cocinas solo o compartes?", new String[] { "Solo para mí", "Familia cocina", "Yo cocino para familia", "Mixto" });
        radioGrupo(html, "logistica_no_cocinar", "Días sin poder cocinar", new String[] { "Raramente", "1-2 días/semana", "Más de 2 días/semana" });

        seccion(html, "⑫ Logística y compra", "Presupuesto, supermercado y organización");
        checks(html, "supermercados", "Supermercados cerca", new String[] { "Mercadona", "Lidl", "Día", "Carrefour", "Alcampo", "Aldi", "Eroski" });
        radioGrupo(html, "logistica_presupuesto", "Presupuesto semanal", new String[] { "Máx 30€/semana", "30-50€/semana", "50-70€/semana", "Sin límite claro" });
        radioGrupo(html, "logistica_compra", "Frecuencia de compra", new String[] { "Casi a diario", "2-3 veces/semana", "1 vez/semana" });
        selectSiNo(html, "logistica_tupper", "Tiene táper para llevar/guardar comida");
        radioGrupo(html, "logistica_congelados", "¿Comprar congelados para simplificar?", new String[] { "Sí, sin problema", "Solo algunos", "Prefiero fresco" });

        seccion(html, "⑬ Hambre y adherencia", "Saciedad y cumplimiento");
        campo(html, "hambreNivel", "Hambre habitual (0-10)", "number min='0' max='10'");
        campo(html, "aguaLitros", "Agua diaria (litros)", "number step='0.1'");
        selectSiNo(html, "consumeAlcohol", "Consume alcohol");
        radioGrupo(html, "hambre_hora", "Hora de más hambre", new String[] { "Por la mañana", "Antes de comer", "Por la tarde", "Después de cenar" });
        radioGrupo(html, "hambre_dificil", "Comida más difícil de reducir", new String[] { "Desayuno", "Almuerzo", "Cena", "Picoteo" });
        radioGrupo(html, "adherencia_dias", "Días máximos con el menú", new String[] { "3-5 días", "1 semana", "14 días si convence" });
        radioGrupo(html, "adherencia_riesgo", "Qué haría romper la dieta", new String[] { "Monotonía", "Hambre insoportable", "Antojo específico", "Situación social" });
        radioGrupo(html, "adherencia_repeticion", "Repetición tolerada", new String[] { "Lo mismo cada día", "2-3 opciones", "Cada día diferente" });
        area(html, "comidaRescate", "Comida de rescate necesaria");
        area(html, "dificultadDieta", "Qué parece más difícil de la dieta");
        area(html, "digestiones", "Digestiones, molestias o saciedad");
        area(html, "bebidasHabituales", "Bebidas habituales");

        seccion(html, "⑭ Top 10", "Tus alimentos ideales para los próximos 14 días");
        for (int i = 1; i <= 10; i++) {
            campo(html, "top" + i, i == 1 ? "1. Alimento favorito absoluto" : i + ". Alimento", "text");
        }
        area(html, "alimentosExcluidosExtra", "Alimento no preguntado que quieras incluir o excluir");
        html.append("<button class='boton principal' type='submit'>Rediseñar mi dieta con estas preferencias</button></form>");
        return construirPagina("Cuestionario nutricional", html.toString());
    }

    private String construirPlanNutricion(PlanNutricion plan, Atleta atleta) {
        StringBuilder html = new StringBuilder();
        PreferenciasNutricion preferencias = atleta.getPreferenciasNutricion();
        html.append("<section id='plan-nutricion' class='tarjeta'><h2>Plan personalizado de nutrición</h2>");
        html.append("<p>Objetivo físico: <strong>").append(escapar(etiquetaEnum(atleta.getObjetivos().getFisicoObjetivo())))
                .append("</strong>. El menú se genera con las calorías y macros del atleta, su tipo de dieta, alergias y preferencias.</p>");
        html.append("<div class='metricas'>");
        metrica(html, "Kcal menú", plan.getKcalPlan());
        metrica(html, "Proteínas menú (g)", plan.getProteinasPlan());
        metrica(html, "Grasas menú (g)", plan.getGrasasPlan());
        metrica(html, "Carbos menú (g)", plan.getCarbohidratosPlan());
        metrica(html, "Kcal restantes", plan.getKcalObjetivo() - plan.getKcalPlan());
        metrica(html, "Proteínas restantes", plan.getProteinasObjetivo() - plan.getProteinasPlan());
        metrica(html, "Grasas restantes", plan.getGrasasObjetivo() - plan.getGrasasPlan());
        metrica(html, "Carbos restantes", plan.getCarbohidratosObjetivo() - plan.getCarbohidratosPlan());
        html.append("</div>");

        html.append("<div class='plan-comidas'>");
        for (ComidaPlan comida : plan.getComidas()) {
            html.append("<article class='comida'><h3>").append(escapar(comida.getNombre())).append(" · ")
                    .append(escapar(comida.getReceta())).append("</h3>");
            html.append("<p><strong>").append(formatoDecimal.format(comida.getKcal())).append(" kcal</strong> · P ")
                    .append(formatoDecimal.format(comida.getProteinas())).append(" g · G ")
                    .append(formatoDecimal.format(comida.getGrasas())).append(" g · C ")
                    .append(formatoDecimal.format(comida.getCarbohidratos())).append(" g</p>");
            html.append("<div class='tabla'><table><tr><th>Alimento</th><th>Gramos</th><th>Kcal</th><th>P</th><th>G</th><th>C</th><th>Vitaminas/minerales destacados</th></tr>");
            for (IngredientePlan ingrediente : comida.getIngredientes()) {
                html.append("<tr><td>").append(escapar(ingrediente.getNombre())).append("</td><td>")
                        .append(formatoDecimal.format(ingrediente.getGramos())).append("</td><td>")
                        .append(formatoDecimal.format(ingrediente.getKcal())).append("</td><td>")
                        .append(formatoDecimal.format(ingrediente.getProteinas())).append("</td><td>")
                        .append(formatoDecimal.format(ingrediente.getGrasas())).append("</td><td>")
                        .append(formatoDecimal.format(ingrediente.getCarbohidratos())).append("</td><td>")
                        .append(escapar(ingrediente.getMicronutrientes())).append("</td></tr>");
            }
            html.append("</table></div></article>");
        }
        html.append("</div>");

        html.append("<h3>Cuestionario usado</h3><div class='preferencias'>");
        dato(html, "Favoritos", preferencias.getAlimentosFavoritos());
        dato(html, "Evita", preferencias.getAlimentosEvitados());
        dato(html, "Bebidas", preferencias.getBebidasHabituales());
        dato(html, "Suplementos", preferencias.getSuplementos());
        dato(html, "Horarios", preferencias.getHorariosComidas());
        dato(html, "Cocina", preferencias.getCocinaDisponible());
        dato(html, "Digestiones", preferencias.getDigestiones());
        html.append("</div>");

        html.append("<h3>Menús y plantillas</h3>");
        html.append("<div class='lista'>");
        html.append("<article class='item'><strong>Plantilla 2000 kcal equilibrada</strong><span>Desayuno 450 · comida 650 · merienda 250 · cena 650 kcal.</span></article>");
        html.append("<article class='item'><strong>Plantilla vegetariana alta en proteína</strong><span>Yogur/queso fresco, legumbres, tofu, huevos si encaja y cereales integrales.</span></article>");
        html.append("<article class='item'><strong>Plantilla definición saciante</strong><span>Proteína magra, patata/arroz medido, verduras altas y grasas controladas.</span></article>");
        html.append("<article class='item'><strong>Plantilla rendimiento/híbrido</strong><span>Más carbohidrato alrededor del entrenamiento y cenas digestivas.</span></article>");
        html.append("</div>");

        html.append("<h3>Recomendaciones</h3><ul>");
        for (String recomendacion : plan.getRecomendaciones()) {
            html.append("<li>").append(escapar(recomendacion)).append("</li>");
        }
        html.append("</ul></section>");
        return html.toString();
    }

    private String construirMediciones(Atleta atleta) {
        StringBuilder html = new StringBuilder();
        html.append("<section id='mediciones' class='tarjeta'><h2>Mediciones</h2><div class='tabla'><table><tr><th>Fecha</th><th>Peso</th><th>% grasa</th><th>Bíceps</th><th>Pecho</th><th>Cintura</th><th>Cadera</th><th>Muslo</th><th>Gemelo</th></tr>");
        for (MedicionCorporal medicion : atleta.getMediciones()) {
            html.append("<tr><td>").append(medicion.getFecha()).append("</td><td>").append(formatoDecimal.format(medicion.getPesoKg()))
                    .append("</td><td>").append(formatoDecimal.format(medicion.getPorcentajeGrasa()))
                    .append("</td><td>").append(formatoDecimal.format(medicion.getBicepsCm()))
                    .append("</td><td>").append(formatoDecimal.format(medicion.getPechoCm()))
                    .append("</td><td>").append(formatoDecimal.format(medicion.getCinturaCm()))
                    .append("</td><td>").append(formatoDecimal.format(medicion.getCaderaCm()))
                    .append("</td><td>").append(formatoDecimal.format(medicion.getMusloCm()))
                    .append("</td><td>").append(formatoDecimal.format(medicion.getGemeloCm())).append("</td></tr>");
        }
        html.append("</table></div><form method='post' action='/medicion/guardar/").append(atleta.getId()).append("' class='subform'><h3>Nueva medición</h3><div class='grid mini'>");
        campo(html, "fecha", "Fecha", "date");
        campo(html, "pesoKg", "Peso (kg)", "number step='0.01'");
        campo(html, "porcentajeGrasa", "% grasa", "number step='0.01'");
        campo(html, "bicepsCm", "Bíceps", "number step='0.01'");
        campo(html, "pechoCm", "Pecho", "number step='0.01'");
        campo(html, "cinturaCm", "Cintura", "number step='0.01'");
        campo(html, "caderaCm", "Cadera", "number step='0.01'");
        campo(html, "musloCm", "Muslo", "number step='0.01'");
        campo(html, "gemeloCm", "Gemelo", "number step='0.01'");
        html.append("</div>");
        area(html, "observaciones", "Observaciones");
        html.append("<button class='boton' type='submit'>Guardar medición</button></form></section>");
        return html.toString();
    }

    private String construirValoraciones(Atleta atleta) {
        StringBuilder html = new StringBuilder();
        html.append("<section id='valoracion' class='tarjeta'><h2>Valoración semanal</h2>");
        for (ValoracionSemanal valoracion : atleta.getValoraciones()) {
            html.append("<article class='item'><strong>").append(valoracion.getFecha()).append("</strong><span>")
                    .append("Sesiones: ").append(valoracion.getSesionesCompletadas())
                    .append(" · Peso: ").append(formatoDecimal.format(valoracion.getPesoEnAyunasKg()))
                    .append(" kg · Sueño: ").append(formatoDecimal.format(valoracion.getHorasSueno())).append(" h</span></article>");
        }
        html.append("<form method='post' action='/valoracion/guardar/").append(atleta.getId()).append("' class='subform'><h3>Nueva valoración</h3><div class='grid mini'>");
        campo(html, "fecha", "Fecha", "date");
        campo(html, "mesociclo", "Mesociclo", "text");
        campo(html, "microciclo", "Microciclo", "text");
        campo(html, "sesionesCompletadas", "Sesiones completadas", "number");
        campo(html, "cansancio", "Cansancio (0-10)", "number min='0' max='10'");
        campo(html, "recuperacion", "Recuperación (0-10)", "number min='0' max='10'");
        campo(html, "pesoEnAyunasKg", "Peso en ayunas", "number step='0.01'");
        campo(html, "hambre", "Hambre (0-10)", "number min='0' max='10'");
        campo(html, "saciedad", "Saciedad (0-10)", "number min='0' max='10'");
        campo(html, "hidratacion", "Hidratacion (0-10)", "number min='0' max='10'");
        campo(html, "seguimientoPlan", "Seguimiento plan (0-10)", "number min='0' max='10'");
        campo(html, "horasSueno", "Horas de sueño", "number step='0.01'");
        campo(html, "calidadSueno", "Calidad del sueño", "number min='0' max='10'");
        campo(html, "estres", "Estrés", "number min='0' max='10'");
        campo(html, "motivacion", "Motivación", "number min='0' max='10'");
        html.append("</div>");
        area(html, "logroSemanal", "De qué estás más orgulloso esta semana");
        area(html, "mejoraSemanal", "Qué puedes mejorar");
        area(html, "comentarioEntrenamiento", "Comentario sobre entrenamiento");
        area(html, "comentarioNutricion", "Comentario sobre nutrición");
        area(html, "mejoraEntrenador", "Qué puedo mejorar para la semana que viene");
        area(html, "observaciones", "Observaciones");
        html.append("<button class='boton' type='submit'>Guardar valoración</button></form></section>");
        return html.toString();
    }

    private void campo(StringBuilder html, String nombre, String etiqueta, String atributos) {
        String[] partes = atributos.split(" ", 2);
        html.append("<label>").append(escapar(etiqueta)).append("<input name='").append(nombre)
                .append("' type='").append(partes[0]).append("'");
        if (partes.length > 1) {
            html.append(" ").append(partes[1]);
        }
        html.append("></label>");
    }

    private void area(StringBuilder html, String nombre, String etiqueta) {
        html.append("<label class='ancho'>").append(escapar(etiqueta)).append("<textarea name='").append(nombre).append("'></textarea></label>");
    }

    private void select(StringBuilder html, String nombre, String etiqueta, Enum<?>[] valores) {
        html.append("<label>").append(escapar(etiqueta)).append("<select name='").append(nombre).append("'>");
        for (Enum<?> valor : valores) {
            html.append("<option value='").append(valor.name()).append("'>").append(escapar(etiquetaEnum(valor))).append("</option>");
        }
        html.append("</select></label>");
    }

    private void seccion(StringBuilder html, String titulo, String subtitulo) {
        html.append("<section class='seccion-nutricion'><h2>").append(escapar(titulo)).append("</h2>");
        html.append("<p class='ayuda'>").append(escapar(subtitulo)).append("</p></section>");
    }

    private void tablaValoracion(StringBuilder html, String primeraColumna, String segundaColumna,
            String[][] filas, String[] opciones) {
        html.append("<div class='tabla cuestionario-tabla'><table><tr><th>").append(escapar(primeraColumna))
                .append("</th><th>").append(escapar(segundaColumna)).append("</th></tr>");
        for (String[] fila : filas) {
            html.append("<tr><td>").append(escapar(fila[1])).append("</td><td><select name='").append(fila[0]).append("'>");
            for (String opcion : opciones) {
                html.append("<option value='").append(escapar(opcion)).append("'>").append(escapar(opcion)).append("</option>");
            }
            html.append("</select></td></tr>");
        }
        html.append("</table></div>");
    }

    private void radioGrupo(StringBuilder html, String nombre, String etiqueta, String[] opciones) {
        html.append("<label class='ancho'>").append(escapar(etiqueta)).append("<select name='").append(nombre).append("'>");
        for (String opcion : opciones) {
            html.append("<option value='").append(escapar(opcion)).append("'>").append(escapar(opcion)).append("</option>");
        }
        html.append("</select></label>");
    }

    private void checks(StringBuilder html, String nombre, String etiqueta, String[] opciones) {
        html.append("<label class='ancho'>").append(escapar(etiqueta)).append("<select name='").append(nombre).append("'>");
        html.append("<option value=''>Sin seleccionar</option>");
        for (String opcion : opciones) {
            html.append("<option value='").append(escapar(opcion)).append("'>").append(escapar(opcion)).append("</option>");
        }
        html.append("</select></label>");
    }

    private void selectSiNo(StringBuilder html, String nombre, String etiqueta) {
        html.append("<label>").append(escapar(etiqueta)).append("<select name='").append(nombre).append("'>")
                .append("<option value='no'>No</option><option value='si'>Sí</option></select></label>");
    }

    private void metrica(StringBuilder html, String etiqueta, double valor) {
        html.append("<div class='metrica'><span>").append(escapar(etiqueta)).append("</span><strong>")
                .append(formatoDecimal.format(valor)).append("</strong></div>");
    }

    private void dato(StringBuilder html, String etiqueta, String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return;
        }
        html.append("<p><strong>").append(escapar(etiqueta)).append(":</strong> ")
                .append(escapar(valor)).append("</p>");
    }

    private String construirTop10DesdeFormulario(Formulario formulario) {
        StringBuilder texto = new StringBuilder();
        for (int i = 1; i <= 10; i++) {
            String alimento = formulario.texto("top" + i);
            if (!alimento.isEmpty()) {
                if (texto.length() > 0) {
                    texto.append(", ");
                }
                texto.append(alimento);
            }
        }
        return texto.toString();
    }

    private String etiquetaEnum(Enum<?> valor) {
        String texto = valor.name().toLowerCase().replace('_', ' ');
        if (texto.isEmpty()) {
            return texto;
        }
        return Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
    }

    private String construirPagina(String titulo, String contenido) {
        return "<!doctype html><html lang='es'><head><meta charset='utf-8'><meta name='viewport' content='width=device-width, initial-scale=1'>"
                + "<title>" + escapar(titulo) + "</title><style>"
                + ":root{--rojo:#c90000;--negro:#161616;--gris:#f4f4f4;--borde:#dedede}*{box-sizing:border-box}body{margin:0;font-family:Inter,Segoe UI,Arial,sans-serif;background:#fafafa;color:var(--negro)}main{width:min(1180px,94vw);margin:28px auto 60px}.hero{display:flex;justify-content:space-between;gap:24px;align-items:center;background:linear-gradient(135deg,#1b1b1b,#c90000);color:white;border-radius:24px;padding:34px;margin-bottom:22px}.hero.compacto{padding:24px}.eyebrow{text-transform:uppercase;letter-spacing:.18em;font-size:12px;opacity:.8}h1,h2,h3{margin:0 0 14px}p{line-height:1.55}.tarjeta{background:white;border:1px solid var(--borde);border-radius:22px;padding:24px;margin:20px 0;box-shadow:0 10px 28px rgba(0,0,0,.05)}.boton{display:inline-flex;border:0;border-radius:999px;padding:12px 18px;background:#222;color:white;text-decoration:none;font-weight:700;cursor:pointer}.boton.principal{background:white;color:#c90000}.buscador{display:flex;align-items:flex-end;gap:14px;flex-wrap:wrap}.buscador label{min-width:220px;margin:0}.lista{display:grid;gap:12px}.item{display:flex;justify-content:space-between;gap:12px;padding:16px;border:1px solid var(--borde);border-radius:16px;color:inherit;text-decoration:none;background:#fff}.item span{color:#666}.grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(240px,1fr));gap:18px}.grid.mini{grid-template-columns:repeat(auto-fit,minmax(160px,1fr))}fieldset{border:2px solid #d10000;border-radius:18px;padding:18px}legend{font-weight:800;color:#c90000}label{display:flex;flex-direction:column;gap:7px;font-weight:700;font-size:14px;margin-bottom:12px}input,select,textarea{width:100%;border:1px solid #ccc;border-radius:12px;padding:10px;font:inherit;background:white}textarea{min-height:86px}.ancho{grid-column:1/-1}.metricas{display:grid;grid-template-columns:repeat(auto-fit,minmax(150px,1fr));gap:14px}.metrica{border-left:5px solid #c90000;background:#f7f7f7;border-radius:14px;padding:14px}.metrica span{display:block;color:#666;font-size:13px}.metrica strong{font-size:24px}.tabla{overflow:auto}table{border-collapse:collapse;width:100%;min-width:760px}th,td{border:1px solid var(--borde);padding:10px;text-align:left}th{background:#c90000;color:white}.subform{margin-top:22px;border-top:1px solid var(--borde);padding-top:22px}.volver{color:#c90000;font-weight:800;text-decoration:none}@media(max-width:700px){.hero{display:block}.item{display:block}}"
                + "</style></head><body><main>" + contenido + "</main></body></html>";
    }

    private void agregarElementos(Atleta atleta, String texto, String tipo) {
        if (texto.trim().isEmpty()) {
            return;
        }
        for (String elemento : texto.split(",")) {
            String limpio = elemento.trim();
            if (limpio.isEmpty()) {
                continue;
            }
            if ("lesion".equals(tipo)) {
                atleta.agregarLesion(limpio);
            } else if ("enfermedad".equals(tipo)) {
                atleta.agregarEnfermedad(limpio);
            } else if ("alergia".equals(tipo)) {
                atleta.agregarAlergia(limpio);
            }
        }
    }

    private int obtenerIdDesdeRuta(String ruta) {
        String[] partes = ruta.split("/");
        return Integer.parseInt(partes[partes.length - 1]);
    }

    private String leerCuerpo(HttpExchange intercambio) throws IOException {
        try (InputStream entrada = intercambio.getRequestBody()) {
            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int leidos;
            while ((leidos = entrada.read(buffer)) != -1) {
                salida.write(buffer, 0, leidos);
            }
            return new String(salida.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    private void redirigir(HttpExchange intercambio, String destino) throws IOException {
        intercambio.getResponseHeaders().add("Location", destino);
        intercambio.sendResponseHeaders(303, -1);
        intercambio.close();
    }

    private void responder(HttpExchange intercambio, int estado, String cuerpo) throws IOException {
        byte[] bytes = cuerpo.getBytes(StandardCharsets.UTF_8);
        intercambio.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
        intercambio.sendResponseHeaders(estado, bytes.length);
        intercambio.getResponseBody().write(bytes);
        intercambio.close();
    }

    private String escapar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }
}
