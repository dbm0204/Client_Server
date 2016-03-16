/* ------------------------------------------------------------------------- */
/*   Copyright (C) 2016
Author:  bmathew2014@my.fit.edu
Author:  rbabbitt2014@my.fit.edu
Florida Tech, Computer Science

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published
by the Free Software Foundation; either the current version of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.              */

import java.util.Set;
import java.util.Iterator;

import java.lang.Thread;
import java.lang.InterruptedException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.Socket;

import java.nio.charset.StandardCharsets;
import java.nio.channels.DatagramChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.Channel;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.ByteBuffer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.ByteArrayInputStream;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option.Builder;

public class Server extends StringWriter implements Runnable {
    private int port = 2236;
    private Thread server = null;
    private Database db;
    
    private Socket clientSock = null;
    private String clientIp = null;
    private int clientPort = -1;
    
    private DatagramChannel udpChannel = null;
    private ServerSocketChannel tcpChannel = null;
    private SocketAddress socket = null;

    public Server(int port, String dataFile, String initFile) {
        super();
        this.port = port;
        db = new Database(dataFile, initFile);
    }
    
    public Server(Socket client, Database db) {
        clientSock = client;
        this.db = db;
    }

    public void start() {
        InputStream stream = null;
        SocketAddress address = new InetSocketAddress(port);
        BufferedWriter writer = new BufferedWriter(this);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        
        try {
            tcpChannel = ServerSocketChannel.open();
            tcpChannel.configureBlocking(false);
            ServerSocket tcpSocket = tcpChannel.socket();
            tcpSocket.bind(address);

            udpChannel = DatagramChannel.open();
            udpChannel.configureBlocking(false);
            DatagramSocket udpSocket = udpChannel.socket();
            udpSocket.bind(address);

            Selector selector = Selector.open();
            tcpChannel.register(selector, SelectionKey.OP_ACCEPT);
            udpChannel.register(selector, SelectionKey.OP_READ);

            while (true) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();

                while(it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();

                    Channel channel = (Channel)key.channel();
                    if (key.isAcceptable() && channel == tcpChannel) {
                        SocketChannel client = tcpChannel.accept();
                        Socket sock = client.socket();
                        if (client != null) {
                            Runnable thread = new Server(sock, db);
                            server = new Thread(thread);
                            server.start();
                        }
                    } else if (key.isReadable() && channel == udpChannel) {
                        stream = new ByteArrayInputStream(buffer.array());
                        socket = udpChannel.receive(buffer); 
                        InetSocketAddress inet = (InetSocketAddress)socket;  
                        if (socket != null) {
                            InetAddress ip = inet.getAddress();
                            int port = inet.getPort();
                            System.out.println(ip.getHostAddress()+":"+port);

                            Parser parser = new Parser(stream);
                            parser.setDatabase(db);
                            parser.parse(writer, ip.getHostAddress(), port);
                            write(toString());
                        }
                        buffer.clear();
                        getBuffer().setLength(0);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        BufferedReader bufferedReader = null;
        OutputStreamWriter streamWriter = null;
        BufferedWriter bufferedWriter = null;

        try {
            clientIp = clientSock.getInetAddress().getHostAddress();
            clientPort = clientSock.getPort();
            System.out.println("Connected to: "+clientIp+":"+clientPort);

            InputStream inputStream = clientSock.getInputStream();
            OutputStream outputStream = clientSock.getOutputStream();
            streamWriter = new OutputStreamWriter(outputStream);
            bufferedWriter = new BufferedWriter(streamWriter);

            Parser parser = new Parser(inputStream);
            parser.setDatabase(db);

            while (!clientSock.isClosed()) {
                parser.parse(bufferedWriter, clientIp, clientPort);
            }

            System.out.println("disconnected to: "+clientIp+":"+clientPort);
            clientSock.close();
            bufferedWriter.close();
            bufferedReader.close();
        } catch (SocketTimeoutException e1) {
            System.err.println("Server Timed out: " + e1.getMessage());
        } catch (IOException e2) {
            System.err.println("IO Error: " + e2.getMessage());
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        Thread.currentThread().interrupt();
    }
    
    @Override
    public void write(String s) {
        try {
            byte[] data = s.getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(data);
            udpChannel.send(buffer, socket);
        } catch (IOException e) {
            System.err.println("failed to send packet.");
        }
    }

    public static void main(String[] args) {
        HelpFormatter formatter = new HelpFormatter();
        int port = 2236;

        Option port_option = Option.builder("p")
            .required(true)
            .longOpt("port")
            .desc("port (i.e. 2356)")
            .argName("port")
            .hasArg()
            .build();

        Option file_option = Option.builder("d")
            .required(true)
            .longOpt("data-file")
            .desc("load data file")
            .argName("file")
            .hasArg()
            .build();

        Option ddl_option = Option.builder("ddl")
            .required(true)
            .longOpt("data-definition-file")
            .desc("file to initialize database")
            .argName("file")
            .hasArg()
            .build();

        Option help_option = Option.builder("h")
            .longOpt("help")
            .desc("display help menu")
            .build();


        Options options = new Options();
        options.addOption(port_option);
        options.addOption(file_option);
        options.addOption(ddl_option);
        options.addOption(help_option);

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
            String dataFile = null;
            String initFile = null;

            if (cmd.hasOption("h")) {
                formatter.printHelp("TaskServer", options);
                return;
            }

            if (cmd.hasOption("p")) {
                port = Integer.parseInt(cmd.getOptionValue("p"));
                System.out.println("Starting sever on port " + port);
            }

            if (cmd.hasOption("d")) {
                dataFile = cmd.getOptionValue("d");
            }

            if (cmd.hasOption("ddl")) {
                initFile = cmd.getOptionValue("ddl");
            }

            assert(dataFile != null && initFile != null);

            Server server = new Server(port, dataFile, initFile);
            server.start();

        } catch (ParseException pe) {
            System.err.println("Parsing failed: " + pe.getMessage());
            formatter.printHelp("TaskServer", options);
        } catch (NumberFormatException ne) {
            System.err.println("Invalid input for port");
            formatter.printHelp("TaskServer", options);
        }
    }
}
