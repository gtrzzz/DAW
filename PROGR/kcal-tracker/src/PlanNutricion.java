import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlanNutricion {

    private double kcalObjetivo;
    private double proteinasObjetivo;
    private double grasasObjetivo;
    private double carbohidratosObjetivo;
    private List<ComidaPlan> comidas;
    private List<String> recomendaciones;

    public PlanNutricion(double kcalObjetivo, double proteinasObjetivo, double grasasObjetivo,
            double carbohidratosObjetivo) {
        this.kcalObjetivo = kcalObjetivo;
        this.proteinasObjetivo = proteinasObjetivo;
        this.grasasObjetivo = grasasObjetivo;
        this.carbohidratosObjetivo = carbohidratosObjetivo;
        this.comidas = new ArrayList<>();
        this.recomendaciones = new ArrayList<>();
    }

    public void agregarComida(ComidaPlan comida) {
        comidas.add(comida);
    }

    public void agregarRecomendacion(String recomendacion) {
        recomendaciones.add(recomendacion);
    }

    public double getKcalObjetivo() {
        return kcalObjetivo;
    }

    public double getProteinasObjetivo() {
        return proteinasObjetivo;
    }

    public double getGrasasObjetivo() {
        return grasasObjetivo;
    }

    public double getCarbohidratosObjetivo() {
        return carbohidratosObjetivo;
    }

    public List<ComidaPlan> getComidas() {
        return Collections.unmodifiableList(comidas);
    }

    public List<String> getRecomendaciones() {
        return Collections.unmodifiableList(recomendaciones);
    }

    public double getKcalPlan() {
        double total = 0;
        for (ComidaPlan comida : comidas) {
            total += comida.getKcal();
        }
        return total;
    }

    public double getProteinasPlan() {
        double total = 0;
        for (ComidaPlan comida : comidas) {
            total += comida.getProteinas();
        }
        return total;
    }

    public double getGrasasPlan() {
        double total = 0;
        for (ComidaPlan comida : comidas) {
            total += comida.getGrasas();
        }
        return total;
    }

    public double getCarbohidratosPlan() {
        double total = 0;
        for (ComidaPlan comida : comidas) {
            total += comida.getCarbohidratos();
        }
        return total;
    }
}
