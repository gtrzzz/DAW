import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComidaPlan {

    private String nombre;
    private String receta;
    private List<IngredientePlan> ingredientes;
    private double kcal;
    private double proteinas;
    private double grasas;
    private double carbohidratos;

    public ComidaPlan(String nombre, String receta) {
        this.nombre = nombre;
        this.receta = receta;
        this.ingredientes = new ArrayList<>();
    }

    public void agregarIngrediente(IngredientePlan ingrediente) {
        ingredientes.add(ingrediente);
        kcal += ingrediente.getKcal();
        proteinas += ingrediente.getProteinas();
        grasas += ingrediente.getGrasas();
        carbohidratos += ingrediente.getCarbohidratos();
    }

    public String getNombre() {
        return nombre;
    }

    public String getReceta() {
        return receta;
    }

    public List<IngredientePlan> getIngredientes() {
        return Collections.unmodifiableList(ingredientes);
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
}
