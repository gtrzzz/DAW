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
                formulario.enumerado("nivelActividad", NivelActividad.class, NivelActividad.SEDENTARIO));
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

        Atleta atleta = new Atleta(
                repositorioAtletas.obtenerSiguienteId(),
                formulario.texto("nombre"),
                formulario.fecha("fechaNacimiento"),
                formulario.enumerado("sexo", Sexo.class, Sexo.HOMBRE),
                formulario.decimal("alturaCm"),
                formulario.decimal("pesoKg"),
                formulario.decimal("porcentajeGrasa"),
                formulario.decimal("vo2Max"),
                formulario.texto("trabajo"),
                formulario.enumerado("estadoCivil", EstadoCivil.class, EstadoCivil.OTRO),
                formulario.texto("experienciaPrevia"),
                objetivos,
                datosDiaADia,
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
                        .append(escapar(atleta.getObjetivos().getTipoObjetivo().name())).append(" · ")
                        .append(escapar(atleta.getObjetivos().getNivelActividad().name())).append("</span></a>");
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
        campo(html, "vo2Max", "VO2 max", "number step='0.01'");
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
                .append(escapar(atleta.getObjetivos().getTipoObjetivo().name())).append(" · ")
                .append(escapar(atleta.getObjetivos().getNivelActividad().name())).append("</p></div></section>");
        html.append(construirNutricion(nutricion));
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
            html.append("<option value='").append(valor.name()).append("'>").append(escapar(valor.name())).append("</option>");
        }
        html.append("</select></label>");
    }

    private void metrica(StringBuilder html, String etiqueta, double valor) {
        html.append("<div class='metrica'><span>").append(escapar(etiqueta)).append("</span><strong>")
                .append(formatoDecimal.format(valor)).append("</strong></div>");
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
