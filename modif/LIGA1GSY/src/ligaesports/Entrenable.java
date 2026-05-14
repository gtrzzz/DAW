package ligaesports;

public interface Entrenable { //interfaz que obliga a las clases que la implementen a poder entrenar y calcular rendimiento
    void entrenar(); //método para entrenar, cada clase decide cómo mejora
    double calcularRendimiento(); //método para calcular el rendimiento de quien sea entrenable
}
