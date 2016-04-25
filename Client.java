import net.ddp2p.ASN1.*;
import java.util.Scanner;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option.Builder;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.Socket;

public class Client {
    public static String group = null; 
    
    public static void main(String[] args) {
        HelpFormatter formatter = new HelpFormatter();

        Option tcp_option = Option.builder("t")
            .required(false)
            .longOpt("tcp")
            .desc("start tcp connection (default)")
            .argName("tcp")
            .build();

        Option udp_option = Option.builder("u")
            .required(false)
            .longOpt("udp")
            .desc("start udp connection")
            .argName("udp")
            .build();

        Option help_option = Option.builder("h")
            .longOpt("help")
            .desc("display help menu")
            .build();

        Option port_option = Option.builder("p")
            .required(true)
            .longOpt("port")
            .desc("port of the server to connect to")
            .argName("port")
            .hasArg()
            .build();
            
        Option ip_option = Option.builder("d")
            .required(true)
            .longOpt("domain")
            .desc("domain to connect to (e.g., 'olin.fit.edu')")
            .argName("domain")
            .build();         
            
        Option group_option = Option.builder("g")
            .required(false)
            .longOpt("group")
            .desc("register to group")
            .argName("group")
            .hasArg()
            .build();   
            
        Options options = new Options();
        options.addOption(tcp_option);
        options.addOption(udp_option);
        options.addOption(help_option);
        options.addOption(port_option);
        options.addOption(ip_option);
        options.addOption(group_option);

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
            String ip = null;
            int port = 0;
            boolean isTCP = true;

            if (cmd.hasOption("h")) {
                formatter.printHelp("TaskServer", options);
                return;
            }

            if (cmd.hasOption("t")) {
                isTCP = true;
            }

            if (cmd.hasOption("u")) {
                isTCP = false;
            }
            
            if (cmd.hasOption("p")) {
                port = Integer.parseInt(cmd.getOptionValue("p"));
            }
             
            if (cmd.hasOption("d")) {
                ip = cmd.getOptionValue("d");
            }
            
            if (cmd.hasOption("g")) {
                group = cmd.getOptionValue("g");
                setup(ip, port);
                eventListener(ip, port, group);
                return;
            }

            Parser cmd_parser = new Parser(System.in);
            cmd_parser.setServer(ip, port, isTCP);
            cmd_parser.parse();

        } catch (ParseException pe) {
            System.err.println("Parsing failed: " + pe.getMessage());
            formatter.printHelp("TaskServer", options);
        } catch (NumberFormatException ne) {
            System.err.println("Invalid input for port");
            formatter.printHelp("TaskServer", options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void setup(final String ip, final int port) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
        public void run() {
            try {
            ASNLeave leave = new ASNLeave(group);
            ASNEvent leaveEvent = null;
            leaveEvent = new ASNEvent(ASNEvent.LEAVE, leave);
            byte[] msg = leaveEvent.encode();
            DatagramSocket socket = new DatagramSocket();
            InetAddress IpAddress = InetAddress.getByName(ip);
            DatagramPacket packet = null;
            packet = new DatagramPacket(msg, msg.length, IpAddress, port);
            socket.send(packet);
            } catch (Exception e) {}
        }
        });
    }
  
    public static void eventListener(String ip, int port, String group) {
        ASNRegister clientRegister = new ASNRegister(group);
        ASNEvent registerEvent = new ASNEvent(ASNEvent.REGISTER, clientRegister);
        try {
            byte[] msg = registerEvent.encode();
            DatagramSocket socket = new DatagramSocket();
            InetAddress IpAddress = InetAddress.getByName(ip);
            DatagramPacket packet = null;
            packet = new DatagramPacket(msg, msg.length, IpAddress, port);
            socket.send(packet);
            SocketAddress addr = socket.getLocalSocketAddress();
            
            while (true) {
                byte[] buffer = new byte[10000];
                packet = new DatagramPacket(buffer, buffer.length, addr);
                socket.receive(packet);
            
                Decoder decoder = new Decoder(buffer);
                ASNEvent event = new ASNEvent().decode(decoder);
                System.out.println(event.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }  
    }
    
}
