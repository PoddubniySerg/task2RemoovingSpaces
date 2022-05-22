import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class MyServer {
    public void start(String host, int port, int bufferCapacity) {
        while (!Thread.interrupted()) {
            try (final ServerSocketChannel serverShannel = ServerSocketChannel.open()) {
                serverShannel.bind(new InetSocketAddress(host, port));

                try (SocketChannel socketChannel = serverShannel.accept()) {
                    final ByteBuffer inputBuffer = ByteBuffer.allocate(bufferCapacity);

                    while (socketChannel.isConnected()) {
                        int bytesCount = socketChannel.read(inputBuffer);
                        if (bytesCount == -1) break;
                        final String text = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                        inputBuffer.clear();
                        StringBuilder stringBuilder = new StringBuilder();
                        for (String symbol : text.split(" ")) {
                            stringBuilder.append(symbol);
                        }
                        socketChannel.write(ByteBuffer.wrap(stringBuilder.toString().getBytes(StandardCharsets.UTF_8)));
                    }
                } catch (IOException e) {
//                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + " завершил работу");
    }
}
