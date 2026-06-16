import java.io.Serializable;
import java.time.LocalDate;

public class ValoracionSemanal implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDate fecha;
    private String mesociclo;
    private String microciclo;
    private int sesionesCompletadas;
    private int cansancio;
    private int recuperacion;
    private double pesoEnAyunasKg;
    private int hambre;
    private int saciedad;
    private int hidratacion;
    private int seguimientoPlan;
    private double horasSueno;
    private int calidadSueno;
    private int estres;
    private int motivacion;
    private String logroSemanal;
    private String mejoraSemanal;
    private String comentarioEntrenamiento;
    private String comentarioNutricion;
    private String mejoraEntrenador;
    private String observaciones;

    public ValoracionSemanal(LocalDate fecha, String mesociclo, String microciclo, int sesionesCompletadas,
            int cansancio, int recuperacion, double pesoEnAyunasKg, int hambre, int saciedad, int hidratacion,
            int seguimientoPlan, double horasSueno, int calidadSueno, int estres, int motivacion,
            String logroSemanal, String mejoraSemanal, String comentarioEntrenamiento,
            String comentarioNutricion, String mejoraEntrenador, String observaciones) {
        this.fecha = fecha;
        this.mesociclo = mesociclo;
        this.microciclo = microciclo;
        this.sesionesCompletadas = sesionesCompletadas;
        this.cansancio = cansancio;
        this.recuperacion = recuperacion;
        this.pesoEnAyunasKg = pesoEnAyunasKg;
        this.hambre = hambre;
        this.saciedad = saciedad;
        this.hidratacion = hidratacion;
        this.seguimientoPlan = seguimientoPlan;
        this.horasSueno = horasSueno;
        this.calidadSueno = calidadSueno;
        this.estres = estres;
        this.motivacion = motivacion;
        this.logroSemanal = logroSemanal;
        this.mejoraSemanal = mejoraSemanal;
        this.comentarioEntrenamiento = comentarioEntrenamiento;
        this.comentarioNutricion = comentarioNutricion;
        this.mejoraEntrenador = mejoraEntrenador;
        this.observaciones = observaciones;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getMesociclo() {
        return mesociclo;
    }

    public String getMicrociclo() {
        return microciclo;
    }

    public int getSesionesCompletadas() {
        return sesionesCompletadas;
    }

    public int getCansancio() {
        return cansancio;
    }

    public int getRecuperacion() {
        return recuperacion;
    }

    public double getPesoEnAyunasKg() {
        return pesoEnAyunasKg;
    }

    public int getHambre() {
        return hambre;
    }

    public int getSaciedad() {
        return saciedad;
    }

    public int getHidratacion() {
        return hidratacion;
    }

    public int getSeguimientoPlan() {
        return seguimientoPlan;
    }

    public double getHorasSueno() {
        return horasSueno;
    }

    public int getCalidadSueno() {
        return calidadSueno;
    }

    public int getEstres() {
        return estres;
    }

    public int getMotivacion() {
        return motivacion;
    }

    public String getLogroSemanal() {
        return logroSemanal;
    }

    public String getMejoraSemanal() {
        return mejoraSemanal;
    }

    public String getComentarioEntrenamiento() {
        return comentarioEntrenamiento;
    }

    public String getComentarioNutricion() {
        return comentarioNutricion;
    }

    public String getMejoraEntrenador() {
        return mejoraEntrenador;
    }

    public String getObservaciones() {
        return observaciones;
    }
}
