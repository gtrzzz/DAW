import java.time.LocalDate;
import java.time.Period;

public class CalculadoraNutricion {

    public ResultadoNutricion calcularPara(Atleta atleta) {
        double metabolismoReposo = calcularMetabolismoReposo(atleta);
        double gastoTotal = metabolismoReposo * obtenerFactorActividad(atleta.getObjetivos().getNivelActividad());
        double caloriasObjetivo = calcularCaloriasObjetivo(gastoTotal, atleta.getObjetivos().getTipoObjetivo());
        double gramosProteinas = atleta.getPesoKg() * obtenerProteinasPorKg(atleta.getObjetivos().getTipoObjetivo());
        double gramosGrasas = atleta.getPesoKg() * obtenerGrasasPorKg(atleta.getObjetivos().getTipoObjetivo());
        double kcalProteinas = gramosProteinas * 4;
        double kcalGrasas = gramosGrasas * 9;
        double gramosCarbohidratos = Math.max(0, (caloriasObjetivo - kcalProteinas - kcalGrasas) / 4);

        return new ResultadoNutricion(metabolismoReposo, gastoTotal, caloriasObjetivo,
                gramosProteinas, gramosGrasas, gramosCarbohidratos);
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
                return 1.5;
            case ACTIVO:
                return 1.7;
            case MUY_ACTIVO:
                return 2.0;
            default:
                return 1.2;
        }
    }

    private double calcularCaloriasObjetivo(double gastoTotal, TipoObjetivo tipoObjetivo) {
        switch (tipoObjetivo) {
            case PERDER_GRASA:
                return gastoTotal - 200;
            case GANAR_MASA_MUSCULAR:
                return gastoTotal + 200;
            default:
                return gastoTotal;
        }
    }

    private double obtenerProteinasPorKg(TipoObjetivo tipoObjetivo) {
        if (tipoObjetivo == TipoObjetivo.GANAR_MASA_MUSCULAR) {
            return 2.1;
        }
        return 2.0;
    }

    private double obtenerGrasasPorKg(TipoObjetivo tipoObjetivo) {
        if (tipoObjetivo == TipoObjetivo.GANAR_MASA_MUSCULAR) {
            return 1.1;
        }
        return 0.8;
    }
}
