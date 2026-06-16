import java.io.Serializable;
import java.time.LocalDate;

public class MedicionCorporal implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDate fecha;
    private double pesoKg;
    private double porcentajeGrasa;
    private double bicepsCm;
    private double pechoCm;
    private double cinturaCm;
    private double caderaCm;
    private double musloCm;
    private double gemeloCm;
    private String observaciones;

    public MedicionCorporal(LocalDate fecha, double pesoKg, double porcentajeGrasa, double bicepsCm,
            double pechoCm, double cinturaCm, double caderaCm, double musloCm, double gemeloCm,
            String observaciones) {
        this.fecha = fecha;
        this.pesoKg = pesoKg;
        this.porcentajeGrasa = porcentajeGrasa;
        this.bicepsCm = bicepsCm;
        this.pechoCm = pechoCm;
        this.cinturaCm = cinturaCm;
        this.caderaCm = caderaCm;
        this.musloCm = musloCm;
        this.gemeloCm = gemeloCm;
        this.observaciones = observaciones;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public double getPorcentajeGrasa() {
        return porcentajeGrasa;
    }

    public double getBicepsCm() {
        return bicepsCm;
    }

    public double getPechoCm() {
        return pechoCm;
    }

    public double getCinturaCm() {
        return cinturaCm;
    }

    public double getCaderaCm() {
        return caderaCm;
    }

    public double getMusloCm() {
        return musloCm;
    }

    public double getGemeloCm() {
        return gemeloCm;
    }

    public String getObservaciones() {
        return observaciones;
    }
}
