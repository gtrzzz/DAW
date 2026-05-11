package ligaesports;

public abstract class PersonaLiga { //creo la clase abstracta PersonaLiga

    private String id; //defino los atributos de la clase abstracta
    private String nombre;
    private String nickname;
    private int edad;
    private double salarioBase;

    public PersonaLiga() { //constructor vacío 
    }

    public PersonaLiga(String id, String nombre, String nickname, int edad, double salarioBase) { //constructor con parámetros de entrada para polimorfismo
        setId(id);
        setNombre(nombre);
        setNickname(nickname);
        setEdad(edad);
        setSalarioBase(salarioBase);
    }

    public abstract double calcularCosteMensual(); //métodos abstractos que de momento solo los he definido porque un jugador y un entrenador pueden tener distintas maneras de calcular el coste 

    public abstract void mostrarResumen(); //lo mismo que en lo anterior, un jugador tiene datos distintos a los de un entrenador

    public String getId() { //setters y getters de cada atributo
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().equals("")) {
            throw new IllegalArgumentException("El ID no puede estar vacío.");
        }

        this.id = id.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().equals("")) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }

        this.nombre = nombre.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        if (nickname == null || nickname.trim().equals("")) {
            throw new IllegalArgumentException("El nickname no puede estar vacío.");
        }

        this.nickname = nickname.trim();
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        if (edad <= 0) {
            throw new IllegalArgumentException("La edad debe ser mayor que 0.");
        }

        this.edad = edad;
    }

    public double getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(double salarioBase) {
        if (salarioBase < 0) {
            throw new IllegalArgumentException("El salario base no puede ser negativo.");
        }

        this.salarioBase = salarioBase;
    }

    @Override //sobreescribo el override porque ya viene predefinido por java y no hace lo que necesito, así que por eso el @override
    public String toString() { //hago el toString para poder imprimir por pantalla esa información que está abajo (lo de nombre, nickname y demás) del objeto, ya que sin ello no se podría hacer
        return "ID: " + id + " | Nombre: " + nombre + " | Nickname: " + nickname + " | Edad: " + edad + " | Salario base: " + salarioBase + "€";
    }
}
