public class IngredientePlan {

    private String nombre;
    private double gramos;
    private double kcal;
    private double proteinas;
    private double grasas;
    private double carbohidratos;
    private String micronutrientes;

    public IngredientePlan(String nombre, double gramos, double kcal, double proteinas,
            double grasas, double carbohidratos, String micronutrientes) {
        this.nombre = nombre;
        this.gramos = gramos;
        this.kcal = kcal;
        this.proteinas = proteinas;
        this.grasas = grasas;
        this.carbohidratos = carbohidratos;
        this.micronutrientes = micronutrientes;
    }

    public String getNombre() {
        return nombre;
    }

    public double getGramos() {
        return gramos;
    }

    public double getKcal() {
        return kcal;
    }

    public double getProteinas() {
        return proteinas;
    }

    public double getGrasas() {
        return grasas;
    }

    public double getCarbohidratos() {
        return carbohidratos;
    }

    public String getMicronutrientes() {
        return micronutrientes;
    }
}
