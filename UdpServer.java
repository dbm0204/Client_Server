import java.io.*;
import java.net.*;

public class UdpServer {

    public static void main (String[] args) throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(2356);
        byte[] receiveData = new byte[10];
        byte[] sendData = new byte[10];

        int port = 2356;
        SocketAddress address = InetSocketAddress(port);

        ServerSocketChannel tcpChannel = ServerSocketChannel.open();
        tcpChannel.bind(address);
        tcpChannel.configureBlocking(false);
        ServerSocket tcpSocket = tcpChannel.socket();

        DatagramChannel udpChannel = DatagramChannel.open();
        udpChannel.bind(address);
        udpChannel.configureBlocking(false);
        DatagramSocket udpSocket = udpChannel.socket();




        while(true)
        {




            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);



            serverSocket.receive(receivePacket);
            String sentence = new String( receivePacket.getData());
            System.out.println("RECEIVED: " + sentence);




            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            String capitalizedSentence = sentence.toUpperCase();
            sendData = capitalizedSentence.getBytes();
            DatagramPacket sendPacket =
                new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        }
    }
}
