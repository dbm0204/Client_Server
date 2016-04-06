package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import net.ddp2p.ASN1.ASN1DecoderFail;
import net.ddp2p.ASN1.Decoder;
import server.EventOk;
import server.EventParser;
import server.RequestEvent;
import server.dbase.EventDefinition;


public final class UDP_Client {
    private UDP_Client() {}

    /**
     * Sends data over the UDP socket to the specified address
     * @param data Data to send
     * @param address Address to send to
     * @param port Port to send to
     */
    private static void sendComamnd(final DatagramSocket sock, final byte[] data, final InetAddress address, final int port) {
        try {
            sock.send(new DatagramPacket(data, data.length, address, port));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends command over UDP to the specified address and port, returning a reply
     * @param address Internet address of the server
     * @param port Port the server runs on
     * @param command Command to send to the server
     * @throws UnknownHostException
     */
    public static String send(final String address, final int port, final String command) throws UnknownHostException {
        final byte[] data = new byte[1024*4];

        final InetAddress IpAddress = InetAddress.getByName(address);
        DatagramSocket sock;
        try {
            sock = new DatagramSocket();

            EventDefinition event = null;

            try {
                event = EventParser.parse(command);
            } catch (final Exception ex) {
                sock.close();
                return "Incorrect command";
            }
            if (event == null) {
                sock.close();
                return "Incorrect command";
            }

            final int index = command.indexOf(';');
            final String keyword = command.substring(0, index);
            switch (keyword) {
                case "EVENT_DEFINITION":
                    sendComamnd(sock, event.encode(), IpAddress, port);
                    break;
                case "GET_NEXT_EVENTS":
                    sendComamnd(sock, new RequestEvent(event.getGroup(), event.getStartingDate()).encode(), IpAddress, port);
                    break;
                default:
                    sock.close();
                    return "Unknown command";
            }

            final DatagramPacket receivePacket = new DatagramPacket(data, data.length);
            sock.receive(receivePacket);

            final int inputSize = receivePacket.getLength();
            if (inputSize <= 0) {
                return "Server didn't reply";
            }

            List<EventDefinition> events = new ArrayList<EventDefinition>();
            EventOk ok = null;

            final Decoder dec = new Decoder(data, 0, inputSize);

            try {
                ok = new EventOk().decode(dec);
            } catch (final ASN1DecoderFail e) {
                try {
                    events = dec.getSequenceOfAL((byte)48, new EventDefinition());
                } catch (final ASN1DecoderFail e1) {
                }
            }

            if (ok == null && events.size() == 0) {
                return "Server: Unknown comamnd";
            }

            if (ok != null) {
                return "OK;"+command;
            } else {
                final StringBuilder str = new StringBuilder("EVENTS;" + events.size());
                for (final EventDefinition e : events) {
                    str.append(";" + e);
                }
                return str.toString();
            }

        } catch (final IOException e) {
           
            e.printStackTrace();
        }
        return null;

    }
}
