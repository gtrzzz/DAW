public class App {
    public static void main(String[] args) throws Exception {
        ServidorLocal servidorLocal = new ServidorLocal(8080);
        servidorLocal.iniciar();
    }
}
