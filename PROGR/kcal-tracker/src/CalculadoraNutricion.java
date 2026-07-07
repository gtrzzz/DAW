import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalculadoraNutricion {

    private final Map<String, Alimento> alimentos;
    private final List<Receta> recetas;

    public CalculadoraNutricion() {
        this.alimentos = crearAlimentos();
        this.recetas = crearRecetas();
    }

    public ResultadoNutricion calcularPara(Atleta atleta) {
        double metabolismoReposo = calcularMetabolismoReposo(atleta);
        double gastoTotal = metabolismoReposo * obtenerFactorActividad(atleta.getObjetivos().getNivelActividad());
        gastoTotal += estimarGastoPorPasos(atleta.getDatosDiaADia().getPasosDiarios());
        gastoTotal += atleta.getDatosDiaADia().getHorasEntrenamiento() * 120;

        double caloriasObjetivo = calcularCaloriasObjetivo(gastoTotal, atleta.getObjetivos());
        double gramosProteinas = atleta.getPesoKg() * obtenerProteinasPorKg(atleta.getObjetivos());
        double gramosGrasas = atleta.getPesoKg() * obtenerGrasasPorKg(atleta.getObjetivos());
        double kcalProteinas = gramosProteinas * 4;
        double kcalGrasas = gramosGrasas * 9;
        double gramosCarbohidratos = Math.max(0, (caloriasObjetivo - kcalProteinas - kcalGrasas) / 4);

        return new ResultadoNutricion(metabolismoReposo, gastoTotal, caloriasObjetivo,
                gramosProteinas, gramosGrasas, gramosCarbohidratos);
    }

    public PlanNutricion generarPlanPara(Atleta atleta) {
        ResultadoNutricion resultado = calcularPara(atleta);
        PlanNutricion plan = new PlanNutricion(resultado.getCaloriasObjetivo(), resultado.getGramosProteinas(),
                resultado.getGramosGrasas(), resultado.getGramosCarbohidratos());

        int comidas = normalizarNumeroComidas(atleta.getDatosDiaADia().getNumeroComidas());
        double[] reparto = obtenerReparto(comidas);
        String[] nombres = obtenerNombresComidas(comidas);

        for (int i = 0; i < comidas; i++) {
            Receta receta = elegirReceta(nombres[i], atleta, i);
            double kcalComida = resultado.getCaloriasObjetivo() * reparto[i];
            plan.agregarComida(construirComida(nombres[i], receta, kcalComida));
        }

        ajustarMacros(plan, atleta);
        agregarRecomendaciones(plan, atleta);
        return plan;
    }

    public double calcularMetabolismoReposo(Atleta atleta) {
        int edad = Period.between(atleta.getFechaNacimiento(), LocalDate.now()).getYears();
        double base = 10 * atleta.getPesoKg() + 6.25 * atleta.getAlturaCm() - 5 * edad;
        if (atleta.getSexo() == Sexo.MUJER) {
            return base - 161;
        }
        return base + 5;
    }

    private double obtenerFactorActividad(NivelActividad nivelActividad) {
        switch (nivelActividad) {
            case SEDENTARIO:
                return 1.2;
            case LIGERAMENTE_ACTIVO:
                return 1.45;
            case ACTIVO:
                return 1.62;
            case MUY_ACTIVO:
                return 1.82;
            default:
                return 1.2;
        }
    }

    private double estimarGastoPorPasos(int pasosDiarios) {
        if (pasosDiarios <= 6000) {
            return 0;
        }
        return Math.min(350, (pasosDiarios - 6000) * 0.035);
    }

    private double calcularCaloriasObjetivo(double gastoTotal, Objetivos objetivos) {
        double cambioSemanal = objetivos.getCambioPesoSemanalKg();
        if (cambioSemanal != 0) {
            return Math.max(1200, gastoTotal + (cambioSemanal * 7700 / 7));
        }
        switch (objetivos.getTipoObjetivo()) {
            case PERDER_GRASA:
                return gastoTotal - 300;
            case GANAR_MASA_MUSCULAR:
                return gastoTotal + 250;
            default:
                return gastoTotal;
        }
    }

    private double obtenerProteinasPorKg(Objetivos objetivos) {
        if (objetivos.getFisicoObjetivo() == FisicoObjetivo.RESISTENCIA) {
            return 1.7;
        }
        if (objetivos.getTipoObjetivo() == TipoObjetivo.GANAR_MASA_MUSCULAR
                || objetivos.getFisicoObjetivo() == FisicoObjetivo.HIPERTROFIA
                || objetivos.getFisicoObjetivo() == FisicoObjetivo.DEFINIDO) {
            return 2.1;
        }
        return 1.9;
    }

    private double obtenerGrasasPorKg(Objetivos objetivos) {
        if (objetivos.getFisicoObjetivo() == FisicoObjetivo.RESISTENCIA) {
            return 0.75;
        }
        if (objetivos.getTipoObjetivo() == TipoObjetivo.GANAR_MASA_MUSCULAR) {
            return 1.0;
        }
        return 0.8;
    }

    private int normalizarNumeroComidas(int comidas) {
        if (comidas < 3) {
            return 3;
        }
        if (comidas > 5) {
            return 5;
        }
        return comidas;
    }

    private double[] obtenerReparto(int comidas) {
        if (comidas == 3) {
            return new double[] { 0.28, 0.40, 0.32 };
        }
        if (comidas == 4) {
            return new double[] { 0.24, 0.34, 0.15, 0.27 };
        }
        return new double[] { 0.22, 0.30, 0.12, 0.12, 0.24 };
    }

    private String[] obtenerNombresComidas(int comidas) {
        if (comidas == 3) {
            return new String[] { "Desayuno", "Comida", "Cena" };
        }
        if (comidas == 4) {
            return new String[] { "Desayuno", "Comida", "Merienda", "Cena" };
        }
        return new String[] { "Desayuno", "Comida", "Media tarde", "Merienda", "Cena" };
    }

    private Receta elegirReceta(String comida, Atleta atleta, int indice) {
        Receta mejor = null;
        int mejorPuntuacion = Integer.MIN_VALUE;
        String favoritos = normalizar(atleta.getPreferenciasNutricion().getAlimentosFavoritos() + " "
                + atleta.getPreferenciasNutricion().getComidasPreferidas());
        String evitados = normalizar(atleta.getPreferenciasNutricion().getAlimentosEvitados() + " "
                + unir(atleta.getAlergias()));
        TipoDieta tipoDieta = atleta.getDatosDiaADia().getTipoDieta();

        for (Receta receta : recetas) {
            if (!receta.comida.equals(comida) && !(comida.equals("Media tarde") && receta.comida.equals("Merienda"))) {
                continue;
            }
            if (!receta.aptaPara(tipoDieta)) {
                continue;
            }
            if (contieneAlguno(evitados, receta.textoBusqueda())) {
                continue;
            }
            int puntuacion = 0;
            if (contieneAlguno(favoritos, receta.textoBusqueda())) {
                puntuacion += 8;
            }
            puntuacion += receta.prioridad - indice;
            if (puntuacion > mejorPuntuacion) {
                mejorPuntuacion = puntuacion;
                mejor = receta;
            }
        }

        if (mejor != null) {
            return mejor;
        }
        return recetas.get(0);
    }

    private ComidaPlan construirComida(String nombre, Receta receta, double kcalObjetivoComida) {
        double kcalBase = receta.calcularKcal(alimentos);
        double factor = kcalBase == 0 ? 1 : kcalObjetivoComida / kcalBase;
        factor = Math.max(0.65, Math.min(1.85, factor));
        ComidaPlan comida = new ComidaPlan(nombre, receta.nombre);
        for (IngredienteReceta ingrediente : receta.ingredientes) {
            Alimento alimento = alimentos.get(ingrediente.alimento);
            double gramos = redondear5(ingrediente.gramos * factor);
            comida.agregarIngrediente(alimento.crearIngrediente(gramos));
        }
        return comida;
    }

    private void ajustarMacros(PlanNutricion plan, Atleta atleta) {
        ComidaPlan comidaAjuste = plan.getComidas().get(plan.getComidas().size() - 1);
        double proteinasPendientes = plan.getProteinasObjetivo() - plan.getProteinasPlan();
        double grasasPendientes = plan.getGrasasObjetivo() - plan.getGrasasPlan();
        double carbohidratosPendientes = plan.getCarbohidratosObjetivo() - plan.getCarbohidratosPlan();

        if (proteinasPendientes > 12) {
            String clave = atleta.getDatosDiaADia().getTipoDieta() == TipoDieta.VEGANA ? "tofu" : "yogur griego";
            agregarAjuste(comidaAjuste, clave, Math.min(250, proteinasPendientes / alimentos.get(clave).proteinas * 100));
        }
        if (carbohidratosPendientes > 18) {
            String clave = atleta.getDatosDiaADia().getTipoDieta() == TipoDieta.VEGANA ? "arroz" : "patata";
            agregarAjuste(comidaAjuste, clave, Math.min(260, carbohidratosPendientes / alimentos.get(clave).carbohidratos * 100));
        }
        if (grasasPendientes > 8) {
            String clave = atleta.getDatosDiaADia().getTipoDieta() == TipoDieta.VEGANA ? "aguacate" : "aceite oliva";
            agregarAjuste(comidaAjuste, clave, Math.min(30, grasasPendientes / alimentos.get(clave).grasas * 100));
        }
    }

    private void agregarAjuste(ComidaPlan comida, String claveAlimento, double gramos) {
        if (gramos <= 0) {
            return;
        }
        comida.agregarIngrediente(alimentos.get(claveAlimento).crearIngrediente(redondear5(gramos)));
    }

    private void agregarRecomendaciones(PlanNutricion plan, Atleta atleta) {
        PreferenciasNutricion preferencias = atleta.getPreferenciasNutricion();
        if (preferencias.getAguaLitros() > 0 && preferencias.getAguaLitros() < 2) {
            plan.agregarRecomendacion("Subir hidratacion progresivamente hasta 2-3 litros diarios, mas si hay cardio o sudoracion alta.");
        }
        if (preferencias.isConsumeAlcohol()) {
            plan.agregarRecomendacion("Limitar alcohol porque desplaza calorias utiles y empeora recuperacion, hambre y sueno.");
        }
        if (preferencias.getHambreHabitual() >= 7) {
            plan.agregarRecomendacion("Priorizar verduras, patata, legumbres y yogur alto en proteina para mejorar saciedad.");
        }
        if (preferencias.getSuplementos().trim().isEmpty()) {
            plan.agregarRecomendacion("Suplementacion opcional: creatina 3-5 g/dia y proteina en polvo solo si cuesta llegar a proteinas.");
        }
        plan.agregarRecomendacion("Los valores son estimaciones medias por 100 g; ajustar con peso semanal, hambre, rendimiento y adherencia.");
    }

    private Map<String, Alimento> crearAlimentos() {
        Map<String, Alimento> mapa = new HashMap<>();
        agregar(mapa, "avena", 389, 16.9, 6.9, 66.3, "fibra, magnesio, hierro, vitaminas B");
        agregar(mapa, "platano", 89, 1.1, 0.3, 22.8, "potasio, vitamina B6");
        agregar(mapa, "frutos rojos", 50, 1.0, 0.3, 11.0, "vitamina C, polifenoles");
        agregar(mapa, "yogur griego", 73, 10.0, 2.0, 3.8, "calcio, B12");
        agregar(mapa, "leche", 47, 3.4, 1.6, 4.8, "calcio, B12");
        agregar(mapa, "huevo", 143, 12.6, 9.5, 0.7, "colina, vitamina D, B12");
        agregar(mapa, "pan integral", 247, 13.0, 4.2, 41.0, "fibra, manganeso, vitaminas B");
        agregar(mapa, "pollo", 165, 31.0, 3.6, 0.0, "niacina, selenio, fosforo");
        agregar(mapa, "pavo", 135, 29.0, 1.5, 0.0, "niacina, selenio");
        agregar(mapa, "ternera magra", 170, 26.0, 7.0, 0.0, "hierro hemo, zinc, B12");
        agregar(mapa, "salmon", 208, 20.0, 13.0, 0.0, "omega 3, vitamina D, B12");
        agregar(mapa, "atun", 116, 26.0, 1.0, 0.0, "selenio, B12");
        agregar(mapa, "merluza", 82, 18.0, 1.3, 0.0, "yodo, selenio");
        agregar(mapa, "arroz", 130, 2.7, 0.3, 28.0, "manganeso, energia facil");
        agregar(mapa, "pasta", 157, 5.8, 0.9, 30.9, "selenio, vitaminas B");
        agregar(mapa, "patata", 77, 2.0, 0.1, 17.0, "potasio, vitamina C");
        agregar(mapa, "lentejas", 116, 9.0, 0.4, 20.0, "hierro, folato, fibra");
        agregar(mapa, "garbanzos", 164, 8.9, 2.6, 27.4, "folato, fibra, magnesio");
        agregar(mapa, "tofu", 144, 15.7, 8.7, 3.0, "calcio, hierro");
        agregar(mapa, "verduras", 35, 2.0, 0.3, 6.0, "vitamina C, folato, fibra");
        agregar(mapa, "ensalada", 25, 1.4, 0.2, 4.0, "vitamina K, folato, antioxidantes");
        agregar(mapa, "aceite oliva", 884, 0.0, 100.0, 0.0, "vitamina E, acido oleico");
        agregar(mapa, "aguacate", 160, 2.0, 14.7, 8.5, "potasio, vitamina E, fibra");
        agregar(mapa, "frutos secos", 607, 20.0, 54.0, 21.0, "magnesio, vitamina E, grasas saludables");
        agregar(mapa, "queso fresco", 98, 12.0, 4.0, 3.0, "calcio, B12");
        agregar(mapa, "proteina whey", 390, 78.0, 6.0, 8.0, "aminoacidos esenciales");
        return mapa;
    }

    private void agregar(Map<String, Alimento> mapa, String nombre, double kcal, double proteinas,
            double grasas, double carbohidratos, String micronutrientes) {
        mapa.put(nombre, new Alimento(nombre, kcal, proteinas, grasas, carbohidratos, micronutrientes));
    }

    private List<Receta> crearRecetas() {
        List<Receta> lista = new ArrayList<>();
        lista.add(new Receta("Desayuno", "Avena con yogur, platano y frutos rojos", false, true, 9)
                .con("avena", 60).con("yogur griego", 200).con("platano", 100).con("frutos rojos", 80));
        lista.add(new Receta("Desayuno", "Tostadas integrales con huevos y fruta", false, false, 8)
                .con("pan integral", 80).con("huevo", 120).con("platano", 100));
        lista.add(new Receta("Desayuno", "Bol vegetal de avena, fruta y frutos secos", true, true, 7)
                .con("avena", 70).con("platano", 120).con("frutos rojos", 100).con("frutos secos", 15));
        lista.add(new Receta("Comida", "Pollo con arroz, verduras y aceite de oliva", false, false, 10)
                .con("pollo", 170).con("arroz", 220).con("verduras", 200).con("aceite oliva", 10));
        lista.add(new Receta("Comida", "Pasta con atun, ensalada y aceite de oliva", false, false, 8)
                .con("pasta", 230).con("atun", 120).con("ensalada", 150).con("aceite oliva", 10));
        lista.add(new Receta("Comida", "Bowl vegano de garbanzos, arroz y verduras", true, true, 9)
                .con("garbanzos", 180).con("arroz", 180).con("verduras", 220).con("aceite oliva", 10));
        lista.add(new Receta("Comida", "Lentejas con arroz y verduras", true, true, 6)
                .con("lentejas", 220).con("arroz", 120).con("verduras", 180).con("aceite oliva", 8));
        lista.add(new Receta("Merienda", "Yogur griego con avena y frutos rojos", false, true, 9)
                .con("yogur griego", 200).con("avena", 35).con("frutos rojos", 100));
        lista.add(new Receta("Merienda", "Sandwich integral de pavo", false, false, 8)
                .con("pan integral", 80).con("pavo", 90).con("queso fresco", 60));
        lista.add(new Receta("Merienda", "Fruta con frutos secos", true, true, 7)
                .con("platano", 120).con("frutos secos", 25));
        lista.add(new Receta("Cena", "Salmon con patata y ensalada", false, false, 10)
                .con("salmon", 150).con("patata", 260).con("ensalada", 180).con("aceite oliva", 8));
        lista.add(new Receta("Cena", "Merluza con patata y verduras", false, false, 9)
                .con("merluza", 180).con("patata", 280).con("verduras", 220).con("aceite oliva", 10));
        lista.add(new Receta("Cena", "Tofu salteado con arroz y verduras", true, true, 8)
                .con("tofu", 180).con("arroz", 180).con("verduras", 240).con("aceite oliva", 10));
        lista.add(new Receta("Cena", "Ternera magra con patata y ensalada", false, false, 7)
                .con("ternera magra", 160).con("patata", 250).con("ensalada", 180).con("aceite oliva", 8));
        return lista;
    }

    private String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.toLowerCase(Locale.ROOT);
    }

    private String unir(List<String> textos) {
        StringBuilder unido = new StringBuilder();
        for (String texto : textos) {
            unido.append(' ').append(texto);
        }
        return unido.toString();
    }

    private boolean contieneAlguno(String textoUsuario, String textoReceta) {
        for (String parte : textoUsuario.split("[,; ]")) {
            String limpio = parte.trim();
            if (limpio.length() >= 4 && textoReceta.contains(limpio)) {
                return true;
            }
        }
        return false;
    }

    private double redondear5(double valor) {
        return Math.round(valor / 5.0) * 5.0;
    }

    private static class Alimento {
        private String nombre;
        private double kcal;
        private double proteinas;
        private double grasas;
        private double carbohidratos;
        private String micronutrientes;

        Alimento(String nombre, double kcal, double proteinas, double grasas, double carbohidratos,
                String micronutrientes) {
            this.nombre = nombre;
            this.kcal = kcal;
            this.proteinas = proteinas;
            this.grasas = grasas;
            this.carbohidratos = carbohidratos;
            this.micronutrientes = micronutrientes;
        }

        IngredientePlan crearIngrediente(double gramos) {
            double factor = gramos / 100;
            return new IngredientePlan(nombre, gramos, kcal * factor, proteinas * factor,
                    grasas * factor, carbohidratos * factor, micronutrientes);
        }
    }

    private static class IngredienteReceta {
        private String alimento;
        private double gramos;

        IngredienteReceta(String alimento, double gramos) {
            this.alimento = alimento;
            this.gramos = gramos;
        }
    }

    private static class Receta {
        private String comida;
        private String nombre;
        private boolean vegana;
        private boolean vegetariana;
        private int prioridad;
        private List<IngredienteReceta> ingredientes;

        Receta(String comida, String nombre, boolean vegana, boolean vegetariana, int prioridad) {
            this.comida = comida;
            this.nombre = nombre;
            this.vegana = vegana;
            this.vegetariana = vegetariana || vegana;
            this.prioridad = prioridad;
            this.ingredientes = new ArrayList<>();
        }

        Receta con(String alimento, double gramos) {
            ingredientes.add(new IngredienteReceta(alimento, gramos));
            return this;
        }

        boolean aptaPara(TipoDieta tipoDieta) {
            if (tipoDieta == TipoDieta.VEGANA) {
                return vegana;
            }
            if (tipoDieta == TipoDieta.VEGETARIANA) {
                return vegetariana;
            }
            return true;
        }

        double calcularKcal(Map<String, Alimento> alimentos) {
            double total = 0;
            for (IngredienteReceta ingrediente : ingredientes) {
                Alimento alimento = alimentos.get(ingrediente.alimento);
                total += alimento.kcal * ingrediente.gramos / 100;
            }
            return total;
        }

        String textoBusqueda() {
            StringBuilder texto = new StringBuilder(nombre.toLowerCase(Locale.ROOT));
            for (IngredienteReceta ingrediente : ingredientes) {
                texto.append(' ').append(ingrediente.alimento);
            }
            return texto.toString();
        }
    }
}
