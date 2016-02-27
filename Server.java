import java.lang.Thread;
import java.lang.InterruptedException;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.cli.Option;      
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option.Builder;

public class Server implements Runnable {
    private int port = 2236;        //default values
    private Socket clientSock = null;
    private Thread server = null;
    private String clientIp = null;
    int clientPort = -1;
    private Database db;
    
    private Server (Socket client, Database db) {
        clientSock = client;
        this.db = db;
    }

    public Server(int port, String dataFile, String initFile) {
        this.port = port;
        db = new Database(dataFile, initFile);
    }

    public void start() {
        ServerSocket serverSock;

        try {
            serverSock = new ServerSocket(port);
            while (true) {
                clientSock = serverSock.accept();
                Runnable runnable = new Server(clientSock, db);
                server = new Thread(runnable);
                server.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {    
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        InputStreamReader streamReader = null;
        BufferedReader bufferedReader = null;        
        OutputStreamWriter streamWriter = null;
        BufferedWriter bufferedWriter = null;
        String[] args;

        try {
            clientIp = clientSock.getInetAddress().getHostAddress();
            clientPort = clientSock.getPort();
            System.out.println("Connected to: " + clientIp +
                               " using port " + clientPort);
                 
            
        
            InputStream inputStream = clientSock.getInputStream();
            streamReader = new InputStreamReader(inputStream); 
            bufferedReader = new BufferedReader(streamReader);
   
            OutputStream outputStream = clientSock.getOutputStream();
            streamWriter = new OutputStreamWriter(outputStream);
            bufferedWriter = new BufferedWriter(streamWriter);

            Parser parser = new Parser(this, db);            

            String input = null;
            while(input == null || !input.equals("LOGOUT")) {
                if (bufferedReader.ready()) {
                    input = bufferedReader.readLine();
                    args = input.split(";");
                    System.out.println(input);
                    System.out.flush();
                    
                    String result = parser.parse(input);
                    
                    bufferedWriter.write(result + "\n"); 
                    bufferedWriter.flush();
                }
            }

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
    }
    
    public String getClientIp() {
        return clientIp;
    }
    
    public int getClientPort() {
        return clientPort;
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
