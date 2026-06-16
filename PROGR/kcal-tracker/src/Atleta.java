import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Atleta implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private LocalDate fechaNacimiento;
    private Sexo sexo;
    private double alturaCm;
    private double pesoKg;
    private double porcentajeGrasa;
    private double vo2Max;
    private String trabajo;
    private EstadoCivil estadoCivil;
    private String experienciaPrevia;

    private List<String> lesiones;
    private List<String> enfermedades;
    private List<String> alergias;

    private Objetivos objetivos;
    private DatosDiaADia datosDiaADia;
    private String observaciones;

    private List<MedicionCorporal> mediciones;
    private List<ValoracionSemanal> valoraciones;

    public Atleta(int id, String nombre, LocalDate fechaNacimiento, Sexo sexo,
                  double alturaCm, double pesoKg, double porcentajeGrasa,
                  double vo2Max, String trabajo, EstadoCivil estadoCivil,
                  String experienciaPrevia, Objetivos objetivos,
                  DatosDiaADia datosDiaADia, String observaciones) {

        this.id = id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.alturaCm = alturaCm;
        this.pesoKg = pesoKg;
        this.porcentajeGrasa = porcentajeGrasa;
        this.vo2Max = vo2Max;
        this.trabajo = trabajo;
        this.estadoCivil = estadoCivil;
        this.experienciaPrevia = experienciaPrevia;
        this.objetivos = objetivos;
        this.datosDiaADia = datosDiaADia;
        this.observaciones = observaciones;

        this.lesiones = new ArrayList<>();
        this.enfermedades = new ArrayList<>();
        this.alergias = new ArrayList<>();
        this.mediciones = new ArrayList<>();
        this.valoraciones = new ArrayList<>();
    }

    public void agregarLesion(String lesion) {
        lesiones.add(lesion);
    }

    public void agregarEnfermedad(String enfermedad) {
        enfermedades.add(enfermedad);
    }

    public void agregarAlergia(String alergia) {
        alergias.add(alergia);
    }

    public void agregarMedicion(MedicionCorporal medicion) {
        mediciones.add(medicion);
    }

    public void agregarValoracion(ValoracionSemanal valoracion) {
        valoraciones.add(valoracion);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public double getAlturaCm() {
        return alturaCm;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public double getPorcentajeGrasa() {
        return porcentajeGrasa;
    }

    public double getVo2Max() {
        return vo2Max;
    }

    public String getTrabajo() {
        return trabajo;
    }

    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public String getExperienciaPrevia() {
        return experienciaPrevia;
    }

    public List<String> getLesiones() {
        return lesiones;
    }

    public List<String> getEnfermedades() {
        return enfermedades;
    }

    public List<String> getAlergias() {
        return alergias;
    }

    public Objetivos getObjetivos() {
        return objetivos;
    }

    public DatosDiaADia getDatosDiaADia() {
        return datosDiaADia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public List<MedicionCorporal> getMediciones() {
        return mediciones;
    }

    public List<ValoracionSemanal> getValoraciones() {
        return valoraciones;
    }
}
