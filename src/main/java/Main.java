public class Main {
    public static final String HOST = "127.0.0.1";
    public static final int PORT = 33333;
    private final static int BUFFER_CAPACITY = 2 << 10;

    public static void main(String[] args) {
        new Thread(() -> new Client().start(HOST, PORT, BUFFER_CAPACITY), "Client").start();
    }
}
