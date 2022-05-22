import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    public void start(String host, int port, int bufferCapacity) {
        Thread server = new Thread(() -> new MyServer().start(host, port, bufferCapacity), "Server");
        server.start();
        StringBuilder stringBuilder = new StringBuilder();
        try (final SocketChannel socketChannel = SocketChannel.open();
             final Scanner scanner = new Scanner(System.in)
        ) {
            socketChannel.connect(new InetSocketAddress(host, port));
            ByteBuffer inputBuffer = ByteBuffer.allocate(bufferCapacity);
            String text;

            while (true) {
                System.out.println("Введите 'end' для прекращения или исходную строку для удаления пробелов:");
                text = scanner.nextLine();
                if (text.equals("end")) break;

                socketChannel.write(ByteBuffer.wrap(text.getBytes(StandardCharsets.UTF_8)));
                int bytesCount = socketChannel.read(inputBuffer);
                stringBuilder.append(new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8));
                inputBuffer.clear();
                System.out.println("Итоговая строка:");
                System.out.println(stringBuilder);
            }
            System.out.println(Thread.currentThread().getName() + " завершил работу");
            server.interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}