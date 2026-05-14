package ligaesports;

public class Entrenador extends PersonaLiga { //clase entrenador, que hereda de PersonaLiga porque también es una persona de la liga

    private int experiencia; //años de experiencia del entrenador
    private String especialidad; //parte del juego en la que destaca como entrenador
    private int victoriasTotales; //victorias acumuladas por sus equipos

    public Entrenador() { //constructor vacío
        super(); //llama al constructor vacío de PersonaLiga
    }

    public Entrenador(String id, String nombre, String nickname, int edad, double salarioBase,
                      int experiencia, String especialidad) { //constructor con todos los datos del entrenador

        super(id, nombre, nickname, edad, salarioBase); //mando los datos comunes al constructor de PersonaLiga
        setExperiencia(experiencia); //uso el setter para validar la experiencia
        setEspecialidad(especialidad); //uso el setter para validar la especialidad
        this.victoriasTotales = 0; //empieza con 0 victorias porque todavía no ha ganado partidos en la liga
    }

    @Override //sobreescribo el método abstracto de PersonaLiga
    public double calcularCosteMensual() { //calcula lo que cuesta el entrenador al mes
        return getSalarioBase() + (experiencia * 75) + (victoriasTotales * 50); //el coste aumenta según experiencia y victorias
    }

    @Override //sobreescribo el método abstracto para mostrar datos concretos del entrenador
    public void mostrarResumen() { //muestra por pantalla un resumen del entrenador
        System.out.println("Entrenador: " + getNombre()); //muestra el nombre real
        System.out.println("Nickname: " + getNickname()); //muestra el nickname
        System.out.println("Experiencia: " + experiencia + " años"); //muestra los años de experiencia
        System.out.println("Especialidad: " + especialidad); //muestra la especialidad
        System.out.println("Victorias totales: " + victoriasTotales); //muestra las victorias acumuladas
        System.out.println("Coste mensual: " + calcularCosteMensual() + "€"); //muestra el coste calculado
    }

    public void sumarVictoria() { //método para aumentar las victorias del entrenador
        victoriasTotales++; //sumo una victoria cada vez que su equipo gana
    }

    public int getExperiencia() { //getter de experiencia
        return experiencia; //devuelve los años de experiencia
    }

    public void setExperiencia(int experiencia) { //setter de experiencia
        if (experiencia < 0) { //no dejo que la experiencia sea negativa
            throw new IllegalArgumentException("La experiencia no puede ser negativa.");
        }

        this.experiencia = experiencia; //guardo la experiencia si es válida
    }

    public String getEspecialidad() { //getter de especialidad
        return especialidad; //devuelve la especialidad del entrenador
    }

    public void setEspecialidad(String especialidad) { //setter de especialidad
        if (especialidad == null || especialidad.trim().equals("")) { //compruebo que no esté vacía
            throw new IllegalArgumentException("La especialidad no puede estar vacía.");
        }

        this.especialidad = especialidad.trim(); //guardo la especialidad quitando espacios
    }

    public int getVictoriasTotales() { //getter de victorias
        return victoriasTotales; //devuelve las victorias totales
    }

    public void setVictoriasTotales(int victoriasTotales) { //setter de victorias totales
        if (victoriasTotales < 0) { //no tendría sentido tener victorias negativas
            throw new IllegalArgumentException("Las victorias no pueden ser negativas.");
        }

        this.victoriasTotales = victoriasTotales; //guardo las victorias si son válidas
    }

    @Override //sobreescribo toString para mostrar el entrenador con datos completos
    public String toString() { //al igual que con Jugador, este toString devuelve lo que hay en PersonaLiga (nombre, nickname, edad, salario) y lo específico de esta subclase, en este caso los años de experiencia, la especialidad y el total de victorias
        return super.toString() + " | Experiencia: " + experiencia + " años | Especialidad: " + especialidad + " | Victorias: " + victoriasTotales;
    }
}
