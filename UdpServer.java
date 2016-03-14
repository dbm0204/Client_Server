import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.nio.*;
import java.util.*;
import java.nio.charset.*;

/*
 * Sources
 *
 */

public class UdpServer {

    public static void main (String[] args) throws Exception {
        byte[] receiveData = new byte[10];
        byte[] sendData = new byte[10];

        int port = 2356;
        SocketAddress address = new InetSocketAddress(port);

        ServerSocketChannel tcpChannel = ServerSocketChannel.open();
        ServerSocket tcpSocket = tcpChannel.socket();
        tcpSocket.bind(address);

        DatagramChannel udpChannel = DatagramChannel.open();
        DatagramSocket udpSocket = udpChannel.socket();
        udpSocket.bind(address);
        
        tcpChannel.configureBlocking(false);
        udpChannel.configureBlocking(false);

        Selector selector = Selector.open();
        tcpChannel.register(selector, SelectionKey.OP_ACCEPT);
        udpChannel.register(selector, SelectionKey.OP_READ);

        CharsetEncoder encoder = Charset.forName("US-ASCII").newEncoder();

        while(true) {
            try {
                selector.select();
                ByteBuffer response = encoder.encode(CharBuffer.wrap("TEST"));
                Set keys = selector.selectedKeys();

                Iterator it = keys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = (SelectionKey) it.next();
                    it.remove();

                    Channel channel = (Channel)key.channel();
                    if (key.isAcceptable() && channel == tcpChannel) {
                        SocketChannel client = tcpChannel.accept();
                        if (client != null) {
                            client.write(response);
                            client.close();
                        }
                    } else if (key.isReadable() && channel == udpChannel) {
                        char[] buffer = new byte[1024];
                        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                        SocketAddress clientAddress = udpChannel.receive(receivePacket);
                        System.out.println(receivePacket.getData());
                        if (clientAddress != null) {
                            udpChannel.send(response, clientAddress);
                        }
                    }
                }
            } catch (Exception e) {
            
            }
        }
    }
}
