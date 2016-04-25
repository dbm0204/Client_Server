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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.lang.Thread;
import java.lang.InterruptedException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.net.DatagramPacket;
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

import net.ddp2p.ASN1.*;

/*
 * Resources:
 * http://www.java2s.com/Code/Java/Network-Protocol/HandlesTCPandUDPconnectionsandprovidesexceptionhandlinganderrorlogging.htm
 */

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
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    
    private static volatile HashMap<String, ArrayList<SocketAddress>>reg = new HashMap<>();


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
        ByteBuffer buffer = ByteBuffer.allocate(10240);

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
                        socket = udpChannel.receive(buffer);
                        InetSocketAddress inet = (InetSocketAddress)socket;
                        if (socket != null) {
                            InetAddress ip = inet.getAddress();
                            int port = inet.getPort();
                            System.out.print("UDP: ");
                            System.out.println(ip.getHostAddress()+":"+port);
                            clientIp = ip.getHostAddress();
                            clientPort = port;

                            byte[] result = loadASN(buffer.array());
                            
                            DatagramSocket socket = new DatagramSocket();
                            DatagramPacket packet = null;
                            packet = new DatagramPacket(result, 
                                                        result.length, 
                                                        ip, port);
                                                        
                            socket.send(packet);
                            socket.close();
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
        OutputStreamWriter streamWriter = null;
        BufferedWriter bufferedWriter = null;

        try {
            clientIp = clientSock.getInetAddress().getHostAddress();
            clientPort = clientSock.getPort();
            System.out.println("Connected to: "+clientIp+":"+clientPort);

            inputStream = clientSock.getInputStream();
            outputStream = clientSock.getOutputStream();
            streamWriter = new OutputStreamWriter(outputStream);
            bufferedWriter = new BufferedWriter(streamWriter);

            while(!clientSock.isClosed()) {
                byte[] buffer = new byte[10000];

                int readBytes = inputStream.read(buffer);
                if (readBytes == -1) {
                    break;
                }
                
                Decoder decoder = new Decoder(buffer);
                if (!decoder.fetchAll(inputStream)) {
                    System.out.println("Error: buffer too small");
                    continue;
                }
                
                byte[] result = loadASN(Arrays.copyOfRange(buffer,0,readBytes));    
                outputStream.write(result);
                outputStream.flush();
            }

            System.out.println("disconnected to: "+clientIp+":"+clientPort);
            clientSock.close();
            bufferedWriter.close();
        } catch (SocketTimeoutException e1) {
            System.err.println("Server Timed out: " + e1.getMessage());
        } catch (IOException e2) {
            System.err.println("IO Error: " + e2.getMessage());
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        Thread.currentThread().interrupt();
    }

    public byte[] loadASN(byte[] msg) {
    	try {
    		Decoder decoder = new Decoder(msg);
    		ASNEvent event = new ASNEvent().decode(decoder);
    		System.out.println(event.getEventId());
    		switch(event.getEventId()) {
    			case ASNEvent.PROJECT:
    				return createProject((ASNProject)event.getEvent());
    			case ASNEvent.GETPROJECT:
    				return getProject((ASNProject)event.getEvent());
    			case ASNEvent.PROJECTS:
    				return getProjects();
    			case ASNEvent.TAKE:
    				return assignProject((ASNTake)event.getEvent());
    		    case ASNEvent.REGISTER:
    		        return register((ASNRegister)event.getEvent());
    		    case ASNEvent.LEAVE:
    		        return leave((ASNLeave)event.getEvent());
    		}
    	} catch (Exception e) {
    	    e.printStackTrace();	
    	}
    	return null;
    }

    public byte[] createProject(ASNProject project) {
        String name = project.getName();
        ASNTask[] tasks = project.getTasks();
        boolean success = db.createProject(name);
        ASNProjectOK ok = null;

        if (!success) {
            ok = new ASNProjectOK(-1);
        } else {
            for (int i = 0; i < tasks.length; i++) {
                success = db.createTask(name,
                	tasks[i].getName(),
                    tasks[i].getStartTime(),
                    tasks[i].getEndTime());
                if (!success) {
                    ok = new ASNProjectOK(-1);
                    break;
                }
            }
        }
        if (ok == null)
            ok = new ASNProjectOK(0);

        ASNEvent answer = new ASNEvent(ASNEvent.PROJECTOK,  ok);
        return answer.encode();
    }

    public byte[] getProject(ASNProject project) {
        String name = project.getName();
        
        ArrayList<Task> tmp = db.getProject(name);
        ASNTask[] tasks = new ASNTask[tmp.size()];
        for (int i = 0; i < tasks.length; i++) {
        	Task t = tmp.get(i);
        	tasks[i] = new ASNTask(t.getName(), 
        	                       t.getStartTime(),
        	                       t.getEndTime());
        	
            tasks[i].assign(t.getUser(), t.getIp(), t.getPort());
            tasks[i].setStatus(t.isDone());
        }
        
        ASNProject proj = new ASNProject(name, tasks);
        ASNEvent answer = new ASNEvent(ASNEvent.PROJECT, proj);
        updateClients(name, answer.encode());
        return answer.encode();
    }

    public byte[] getProjects() {
        ArrayList<String> names = db.getProjects();
        if (names != null) {
            ASNProject[] projects = new ASNProject[names.size()];
            for (int i = 0; i < projects.length; i++) {           
                ArrayList<Task> tmp = db.getProject(names.get(i));
                ASNTask[] tasks = new ASNTask[tmp.size()];
                for (int j = 0; j < tmp.size(); j++) {
                	Task t = tmp.get(j);	
                	tasks[j] = new ASNTask(t.getName(), 
                	                       t.getStartTime(),
                	                       t.getEndTime());
                	
                	
                    tasks[j].assign(t.getUser(), t.getIp(), t.getPort());
                    tasks[j].setStatus(t.isDone());
                }
                projects[i] = new ASNProject(names.get(i), tasks);
            }
            ASNProjectsAnswer answer = new ASNProjectsAnswer(projects);
            ASNEvent event = new ASNEvent(ASNEvent.PROJECTSANSWER, answer);
            
            for (String name: names) {
                updateClients(name, event.encode());
            }
            
            return event.encode();
        }

        ASNEvent answer= new ASNEvent(ASNEvent.PROJECTOK, new ASNProjectOK(-1));
        return answer.encode();
    }

    public byte[] assignProject(ASNTake take) {
        boolean success = false;
        String proj = take.getProject();
        String task = take.getTask();
        String user = take.getUser();
        success = db.assignTask(proj, task, user);
        success = success && db.updateUser(user, clientIp, clientPort);
        ASNProjectOK ok = null;
        if (success) {
            ok = new ASNProjectOK(0);
        } else {
            ok = new ASNProjectOK(-1);
        }
                  
        ASNEvent answer = new ASNEvent(ASNEvent.PROJECTOK, ok);
        updateClients(proj, answer.encode());  
        return answer.encode();
    }
    
    public byte[] register(ASNRegister r) {
        String proj = r.getGroup();
        ASNProjectOK ok = null;
        
        if (proj != null) {
            ok = new ASNProjectOK(0);
        
            if (!reg.containsKey(proj)) {
               reg.put(proj, new ArrayList<SocketAddress>());
            }
        
            ArrayList<SocketAddress> sockets = reg.get(proj);
        
             if (!sockets.contains(socket)) {
                sockets.add(socket);
             }
        } else {
            ok = new ASNProjectOK(-1);
        }

        ASNEvent answer = new ASNEvent(ASNEvent.PROJECTOK, ok);
        
        if (proj != null) {
            updateClients(proj, answer.encode());
        }
        return answer.encode();
    }
    
    public byte[] leave(ASNLeave l) {
        String proj = l.getGroup();
        ASNProjectOK ok = new ASNProjectOK(0);
        
        reg.remove(proj);
        
        System.out.println(((InetSocketAddress)socket).getPort() + " left");
        ASNEvent answer = new ASNEvent(ASNEvent.PROJECTOK, ok);
        updateClients(proj, answer.encode());
        return answer.encode();
    }
    
    public void updateClients(String key, byte[] msg) {
        if (reg.get(key) == null) return;
        
        for (SocketAddress sock: reg.get(key)) {
             try {
                DatagramSocket socket = new DatagramSocket();
                DatagramPacket packet = null;
                packet = new DatagramPacket(msg, msg.length, sock);
                socket.send(packet);
                socket.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }      
        }
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
