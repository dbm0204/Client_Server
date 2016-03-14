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
