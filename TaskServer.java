/* ------------------------------------------------------------------------- */
/*   Copyright (C) 2016 
                Author:  name@my.fit.edu
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

import org.apache.commons.cli.Option;      
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option.Builder;
import java.sql.*;

public class TaskServer {
    private int port;
     
    public TaskServer(int p, String file) {
        port = p;
        sqlConnect(file);
    }
    
    public void sqlConnect(String dataFile) {
        Connection con;    
        try {
            Class.forName("org.sqlite.JDBC"); 
            con = DriverManager.getConnection("jdbc:sqlite:" + dataFile);
        } catch (Exception e) {
            System.err.println(e.printStackTrace());
            System.exit(0);
        }
        System.out.println("Datafile " + dataFile + " loaded");
    }
    
    public initDataFile() {
        String tasks_table = "CREATE TABLE tasks ( " +
                             "id    INT PRIMARY KEY    NOT NULL," +
                             "task  TEXT               NOT NULL)"
                     
        String project_table = "CREATE TABLE projects ( " +
                               "id      INT PRIMARY KEY     NOT NULL," +
                               "name    TEXT                NOT NULL)";
                               
        String definition = "CREATE TABLE definitions ( " +
                            "task_id    INT     NOT NULL," +
                            "project_id INT     NOT NULL," +
                            "user_id    INT     NOT NULL)";
                            
        String schedule_table = "CREATE TABLE schedule ( " +
                                "task_id INT    NOT NULL," +
                                "user    TEXT   NOT NULL," +
                                "start   TEXT   NOT NULL," +
                                "stop    TEXT   NOT NULL)";
                            
                            
        String user_table = "CREATE TABLE users ( " +
                            "id     INT PRIMARY KEY     NOT NULL," +
                            "name   TEXT                NOT NULL,"
                            "ip     TEXT                NOT NULL)"; 
                     
    }
    
      
    public static void main(String[] args) {
        HelpFormatter formatter = new HelpFormatter();
        
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
            
        Option help_option = Option.builder("h")
            .longOpt("help")
            .desc("display help menu")
            .build();
                                          
        
        Options options = new Options();
        options.addOption(port_option);
        options.addOption(file_option);
        options.addOption(help_option);
        
        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                formatter.printHelp("TaskServer", options);
                return;
            }
            
            if (cmd.hasOption("p")) {
                int port = Integer.parseInt(cmd.getOptionValue("p"));
                System.out.println("Starting sever on port " + port);    
            }
            
            if (cmd.hasOption("d")) {
                String dataFile = cmd.getOptionValue("d");
            }
                                
        } catch (ParseException pe) {
            System.err.println("Parsing failed: " + pe.getMessage());
            formatter.printHelp("TaskServer", options);
        } catch (NumberFormatException ne) {
            System.err.println("Invalid input for port");
            formatter.printHelp("TaskServer", options);       
        }
    }
}
