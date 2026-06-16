import java.io.Serializable;

public class Objetivos implements Serializable {

    private static final long serialVersionUID = 1L;

    private String corporales;
    private String salud;
    private String rendimiento;
    private String funcionales;
    private String psicologicos;
    private TipoObjetivo tipoObjetivo;
    private NivelActividad nivelActividad;

    public Objetivos(String corporales, String salud, String rendimiento, String funcionales,
            String psicologicos, TipoObjetivo tipoObjetivo, NivelActividad nivelActividad) {
        this.corporales = corporales;
        this.salud = salud;
        this.rendimiento = rendimiento;
        this.funcionales = funcionales;
        this.psicologicos = psicologicos;
        this.tipoObjetivo = tipoObjetivo;
        this.nivelActividad = nivelActividad;
    }

    public String getCorporales() {
        return corporales;
    }

    public String getSalud() {
        return salud;
    }

    public String getRendimiento() {
        return rendimiento;
    }

    public String getFuncionales() {
        return funcionales;
    }

    public String getPsicologicos() {
        return psicologicos;
    }

    public TipoObjetivo getTipoObjetivo() {
        return tipoObjetivo;
    }

    public NivelActividad getNivelActividad() {
        return nivelActividad;
    }
}
