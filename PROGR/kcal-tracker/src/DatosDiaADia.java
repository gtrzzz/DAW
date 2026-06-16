import java.io.Serializable;

public class DatosDiaADia implements Serializable {

    private static final long serialVersionUID = 1L;

    private int pasosDiarios;
    private double horasInactividad;
    private double horasTrabajo;
    private double horasEntrenamiento;
    private double horasSueno;
    private int calidadSueno;
    private int estres;
    private int numeroComidas;
    private TipoDieta tipoDieta;
    private String medicacion;

    public DatosDiaADia(int pasosDiarios, double horasInactividad, double horasTrabajo,
            double horasEntrenamiento, double horasSueno, int calidadSueno, int estres,
            int numeroComidas, TipoDieta tipoDieta, String medicacion) {
        this.pasosDiarios = pasosDiarios;
        this.horasInactividad = horasInactividad;
        this.horasTrabajo = horasTrabajo;
        this.horasEntrenamiento = horasEntrenamiento;
        this.horasSueno = horasSueno;
        this.calidadSueno = calidadSueno;
        this.estres = estres;
        this.numeroComidas = numeroComidas;
        this.tipoDieta = tipoDieta;
        this.medicacion = medicacion;
    }

    public int getPasosDiarios() {
        return pasosDiarios;
    }

    public double getHorasInactividad() {
        return horasInactividad;
    }

    public double getHorasTrabajo() {
        return horasTrabajo;
    }

    public double getHorasEntrenamiento() {
        return horasEntrenamiento;
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

    public int getNumeroComidas() {
        return numeroComidas;
    }

    public TipoDieta getTipoDieta() {
        return tipoDieta;
    }

    public String getMedicacion() {
        return medicacion;
    }
}
