public class ResultadoNutricion {

    private double metabolismoReposo;
    private double gastoTotal;
    private double caloriasObjetivo;
    private double gramosProteinas;
    private double gramosGrasas;
    private double gramosCarbohidratos;

    public ResultadoNutricion(double metabolismoReposo, double gastoTotal, double caloriasObjetivo,
            double gramosProteinas, double gramosGrasas, double gramosCarbohidratos) {
        this.metabolismoReposo = metabolismoReposo;
        this.gastoTotal = gastoTotal;
        this.caloriasObjetivo = caloriasObjetivo;
        this.gramosProteinas = gramosProteinas;
        this.gramosGrasas = gramosGrasas;
        this.gramosCarbohidratos = gramosCarbohidratos;
    }

    public double getMetabolismoReposo() {
        return metabolismoReposo;
    }

    public double getGastoTotal() {
        return gastoTotal;
    }

    public double getCaloriasObjetivo() {
        return caloriasObjetivo;
    }

    public double getGramosProteinas() {
        return gramosProteinas;
    }

    public double getGramosGrasas() {
        return gramosGrasas;
    }

    public double getGramosCarbohidratos() {
        return gramosCarbohidratos;
    }
}
