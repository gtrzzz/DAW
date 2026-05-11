package ligaesports;

public class Entrenador extends PersonaLiga {

    private int experiencia;
    private String especialidad;
    private int victoriasTotales;

    public Entrenador() {
        super();
    }

    public Entrenador(String id, String nombre, String nickname, int edad, double salarioBase,
                      int experiencia, String especialidad) {

        super(id, nombre, nickname, edad, salarioBase);
        setExperiencia(experiencia);
        setEspecialidad(especialidad);
        this.victoriasTotales = 0;
    }

    @Override
    public double calcularCosteMensual() {
        return getSalarioBase() + (experiencia * 75) + (victoriasTotales * 50);
    }

    @Override
    public void mostrarResumen() {
        System.out.println("Entrenador: " + getNombre());
        System.out.println("Nickname: " + getNickname());
        System.out.println("Experiencia: " + experiencia + " años");
        System.out.println("Especialidad: " + especialidad);
        System.out.println("Victorias totales: " + victoriasTotales);
        System.out.println("Coste mensual: " + calcularCosteMensual() + "€");
    }

    public void sumarVictoria() {
        victoriasTotales++;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        if (experiencia < 0) {
            throw new IllegalArgumentException("La experiencia no puede ser negativa.");
        }

        this.experiencia = experiencia;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        if (especialidad == null || especialidad.trim().equals("")) {
            throw new IllegalArgumentException("La especialidad no puede estar vacía.");
        }

        this.especialidad = especialidad.trim();
    }

    public int getVictoriasTotales() {
        return victoriasTotales;
    }

    public void setVictoriasTotales(int victoriasTotales) {
        if (victoriasTotales < 0) {
            throw new IllegalArgumentException("Las victorias no pueden ser negativas.");
        }

        this.victoriasTotales = victoriasTotales;
    }

    @Override
    public String toString() { //al igual que con Jugador, este toString devuelve lo que hay en PersonaLiga (nombre, nickname, edad, salario) y lo específico de esta subclase, en este caso los años de experiencia, la especialidad y el total de victorias
        return super.toString() + " | Experiencia: " + experiencia + " años | Especialidad: " + especialidad + " | Victorias: " + victoriasTotales;
    }
}
