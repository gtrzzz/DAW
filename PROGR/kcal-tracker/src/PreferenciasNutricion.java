import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PreferenciasNutricion implements Serializable {

    private static final long serialVersionUID = 1L;

    private String alimentosFavoritos;
    private String alimentosEvitados;
    private String bebidasHabituales;
    private String suplementos;
    private String horariosComidas;
    private String cocinaDisponible;
    private String presupuesto;
    private String digestiones;
    private String comidasPreferidas;
    private int hambreHabitual;
    private double aguaLitros;
    private boolean consumeCafeina;
    private boolean consumeAlcohol;
    private Map<String, String> respuestasCuestionario;
    private boolean cuestionarioCompleto;

    public PreferenciasNutricion(String alimentosFavoritos, String alimentosEvitados, String bebidasHabituales,
            String suplementos, String horariosComidas, String cocinaDisponible, String presupuesto,
            String digestiones, String comidasPreferidas, int hambreHabitual, double aguaLitros,
            boolean consumeCafeina, boolean consumeAlcohol) {
        this.alimentosFavoritos = alimentosFavoritos;
        this.alimentosEvitados = alimentosEvitados;
        this.bebidasHabituales = bebidasHabituales;
        this.suplementos = suplementos;
        this.horariosComidas = horariosComidas;
        this.cocinaDisponible = cocinaDisponible;
        this.presupuesto = presupuesto;
        this.digestiones = digestiones;
        this.comidasPreferidas = comidasPreferidas;
        this.hambreHabitual = hambreHabitual;
        this.aguaLitros = aguaLitros;
        this.consumeCafeina = consumeCafeina;
        this.consumeAlcohol = consumeAlcohol;
        this.respuestasCuestionario = new HashMap<>();
        this.cuestionarioCompleto = false;
    }

    public void guardarCuestionario(Map<String, String> respuestas) {
        this.respuestasCuestionario = new HashMap<>(respuestas);
        this.cuestionarioCompleto = true;
        this.alimentosFavoritos = combinar(construirTop10(respuestas), obtenerAlimentosConValor(respuestas, "Me gusta mucho"));
        this.alimentosEvitados = combinar(respuestas.get("alimentosExcluidosExtra"), obtenerAlimentosConValor(respuestas, "No me gusta"));
        this.alimentosEvitados = combinar(this.alimentosEvitados, obtenerAlimentosConValor(respuestas, "Prefiero evitar"));
        this.bebidasHabituales = respuestas.getOrDefault("bebidasHabituales", respuestas.getOrDefault("snack_refrescos_zero", ""));
        this.suplementos = respuestas.getOrDefault("proteina_compra", "") + " " + respuestas.getOrDefault("proteina_sabores", "");
        this.horariosComidas = respuestas.getOrDefault("logistica_no_cocinar", "") + " " + respuestas.getOrDefault("hambre_hora", "");
        this.cocinaDisponible = respuestas.getOrDefault("cocina_tiempo", "") + " " + respuestas.getOrDefault("cocina_batidora", "");
        this.presupuesto = respuestas.getOrDefault("logistica_presupuesto", "");
        this.digestiones = respuestas.getOrDefault("digestiones", "") + " " + respuestas.getOrDefault("hambre_dificil", "");
        this.comidasPreferidas = respuestas.getOrDefault("carbo_favorito", "") + " " + respuestas.getOrDefault("cocina_preferencia", "");
        this.consumeCafeina = respuestas.containsKey("snack_cafe") && !"No tomo café".equals(respuestas.get("snack_cafe"));
        this.consumeAlcohol = "si".equalsIgnoreCase(respuestas.getOrDefault("consumeAlcohol", "no"));
    }

    public boolean isCuestionarioCompleto() {
        return cuestionarioCompleto;
    }

    public Map<String, String> getRespuestasCuestionario() {
        if (respuestasCuestionario == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(respuestasCuestionario);
    }

    public String respuesta(String clave) {
        if (respuestasCuestionario == null) {
            return "";
        }
        return respuestasCuestionario.getOrDefault(clave, "");
    }

    public String getAlimentosFavoritos() {
        return alimentosFavoritos;
    }

    public String getAlimentosEvitados() {
        return alimentosEvitados;
    }

    public String getBebidasHabituales() {
        return bebidasHabituales;
    }

    public String getSuplementos() {
        return suplementos;
    }

    public String getHorariosComidas() {
        return horariosComidas;
    }

    public String getCocinaDisponible() {
        return cocinaDisponible;
    }

    public String getPresupuesto() {
        return presupuesto;
    }

    public String getDigestiones() {
        return digestiones;
    }

    public String getComidasPreferidas() {
        return comidasPreferidas;
    }

    public int getHambreHabitual() {
        return hambreHabitual;
    }

    public double getAguaLitros() {
        return aguaLitros;
    }

    public boolean isConsumeCafeina() {
        return consumeCafeina;
    }

    public boolean isConsumeAlcohol() {
        return consumeAlcohol;
    }

    private String construirTop10(Map<String, String> respuestas) {
        StringBuilder texto = new StringBuilder();
        for (int i = 1; i <= 10; i++) {
            String alimento = respuestas.getOrDefault("top" + i, "").trim();
            if (!alimento.isEmpty()) {
                if (texto.length() > 0) {
                    texto.append(", ");
                }
                texto.append(alimento);
            }
        }
        return texto.toString();
    }

    private String obtenerAlimentosConValor(Map<String, String> respuestas, String valor) {
        StringBuilder texto = new StringBuilder();
        for (Map.Entry<String, String> entrada : respuestas.entrySet()) {
            if (valor.equals(entrada.getValue()) && entrada.getKey().startsWith("food_")) {
                if (texto.length() > 0) {
                    texto.append(", ");
                }
                texto.append(entrada.getKey().substring(5).replace('_', ' '));
            }
        }
        return texto.toString();
    }

    private String combinar(String primero, String segundo) {
        String a = primero == null ? "" : primero.trim();
        String b = segundo == null ? "" : segundo.trim();
        if (a.isEmpty()) {
            return b;
        }
        if (b.isEmpty()) {
            return a;
        }
        return a + ", " + b;
    }
}
