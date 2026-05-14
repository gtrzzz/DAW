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
        setId(id); //uso los setters para que se validen los datos desde el principio
        setNombre(nombre); //guardo el nombre validado
        setNickname(nickname); //guardo el nickname validado
        setEdad(edad); //guardo la edad validada
        setSalarioBase(salarioBase); //guardo el salario validado
    }

    public abstract double calcularCosteMensual(); //métodos abstractos que de momento solo los he definido porque un jugador y un entrenador pueden tener distintas maneras de calcular el coste 

    public abstract void mostrarResumen(); //lo mismo que en lo anterior, un jugador tiene datos distintos a los de un entrenador

    public String getId() { //setters y getters de cada atributo
        return id;
    }

    public void setId(String id) { //setter del id
        if (id == null || id.trim().equals("")) { //compruebo que el id no sea nulo ni vacío
            throw new IllegalArgumentException("El ID no puede estar vacío.");
        }

        this.id = id.trim(); //guardo el id sin espacios al principio o al final
    }

    public String getNombre() { //getter del nombre
        return nombre; //devuelve el nombre real
    }

    public void setNombre(String nombre) { //setter del nombre
        if (nombre == null || nombre.trim().equals("")) { //compruebo que el nombre no esté vacío
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }

        this.nombre = nombre.trim(); //guardo el nombre quitando espacios innecesarios
    }

    public String getNickname() { //getter del nickname
        return nickname; //devuelve el nickname
    }

    public void setNickname(String nickname) { //setter del nickname
        if (nickname == null || nickname.trim().equals("")) { //compruebo que no esté vacío
            throw new IllegalArgumentException("El nickname no puede estar vacío.");
        }

        this.nickname = nickname.trim(); //guardo el nickname sin espacios sobrantes
    }

    public int getEdad() { //getter de la edad
        return edad; //devuelve la edad
    }

    public void setEdad(int edad) { //setter de la edad
        if (edad <= 0) { //la edad tiene que ser mayor que 0
            throw new IllegalArgumentException("La edad debe ser mayor que 0.");
        }

        this.edad = edad; //guardo la edad si es válida
    }

    public double getSalarioBase() { //getter del salario base
        return salarioBase; //devuelve el salario base
    }

    public void setSalarioBase(double salarioBase) { //setter del salario base
        if (salarioBase < 0) { //no dejo que el salario sea negativo
            throw new IllegalArgumentException("El salario base no puede ser negativo.");
        }

        this.salarioBase = salarioBase; //guardo el salario si es válido
    }

    @Override //sobreescribo el override porque ya viene predefinido por java y no hace lo que necesito, así que por eso el @override
    public String toString() { //hago el toString para poder imprimir por pantalla esa información que está abajo (lo de nombre, nickname y demás) del objeto, ya que sin ello no se podría hacer
        return "ID: " + id + " | Nombre: " + nombre + " | Nickname: " + nickname + " | Edad: " + edad + " | Salario base: " + salarioBase + "€";
    }
}
