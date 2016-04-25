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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.lang.StringBuilder;

public class Database {
    private String dataFile = null;
    private Connection con = null;
    private String initFile = null;

    public Database(String dbFile, String initFile) {
        dataFile = dbFile;          //actual database
        this.initFile = initFile;   //Data Definition Language (DDL) file
        connect();
    }

    public void connect() {
        File file = new File(dataFile);
        boolean init = false;
        try {
            if (!file.exists()) {
                file.createNewFile();
                init = true;
            }

            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + dataFile;
            con = DriverManager.getConnection(url);
            con.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("sql connected to " + dataFile);
        if (init) initDatabase();
    }

    public void disconnect() {
        try {
            con.close();
            System.out.println("sql disconnected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initDatabase() {
        try {
            System.out.println("initializing using " + initFile);
            Path path = Paths.get(initFile);
            Charset charset = StandardCharsets.UTF_8;
            List<String> lines = Files.readAllLines(path, charset);
            StringBuilder sql = new StringBuilder();
            Iterator iterator = lines.iterator();
            while(iterator.hasNext()) {
                sql.append(iterator.next());
            }

            Statement statement = con.createStatement();
            statement.executeUpdate(sql.toString());
            con.commit();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean createProject(String proj) {
        assert(proj != null);
        String sql = "INSERT INTO projects (name) VALUES ('"+proj+"')";
        return runStatement(sql);
    }

    public boolean createTask(String proj,String task,String start,String end) {
        String sql = "INSERT INTO tasks " +
            "(task, start, end, project_id) " +
            "SELECT '"+task+"','"+start+"','"+end+
            "', id FROM projects WHERE " +
            "name = '"+proj+"';";
        return runStatement(sql);
    }

    public boolean assignTask(String project, String task, String user) {
        String sql = "INSERT INTO users " +
            "(username, task_id) " +
            "SELECT '"+user+"', id FROM tasks " +
            " WHERE task = '"+task+"' AND " +
            "project_id IN (SELECT id FROM " +
            "projects WHERE name = '"+project+"');";
        return runStatement(sql);
    }

    public boolean updateUser(String user, String ip, int port) {
        String sql = "UPDATE users SET " +
            "ip = '" + ip + "'," +
            "port = " + port + " " +
            "WHERE username = '" + user + "';";
        return runStatement(sql);
    }
    
    public boolean registerUser(String group, String ip, int port) {
            String sql = "INSERT INTO registery " +
            "(ip, port, project_id) " +
            "SELECT '" + ip + "', '" + port + "', id FROM " + 
            "projects WHERE name = '" + group + "';";
            return runStatement(sql);   
    }
    
    public boolean leave(String group, String ip, int port) {
        String sql = "DELETE FROM registery " +
        "WHERE ip = '" + ip + "' AND port = '" + port + "' " +
        "AND project_id IN (SELECT id FROM " +
        "projects WHERE name = '"+ group +"');";
        return runStatement(sql);
    }

    public boolean runStatement(String sql) {
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate(sql);
            con.commit();
            statement.close();
        } catch (Exception e) {
            System.err.println("Statement Failed: " + e.getMessage());
            return false;
        }
        return true;
    }

    public ArrayList<String> getProjects() {
        String sql = "SELECT name FROM projects";
        ArrayList<String> projects = new ArrayList<String>();
        try {
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(sql);
            while (results.next()) {
                String project = results.getString("name");
                projects.add(project);
            }
            results.close();
            statement.close();
        } catch (Exception e) {
            System.err.println("failed to get projects: " + e.getMessage());
            return null;
        }
        return projects;
    }

    public ArrayList<Task> getProject(String proj) {
        ArrayList<Task> tasks = new ArrayList<Task>();
        String sql = "SELECT * FROM projects " +
            "JOIN tasks ON " +
            "tasks.project_id=projects.id AND name='"+proj+"'" +
            "LEFT JOIN users ON " +
            "users.task_id=tasks.id;";
        try {
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(sql);

            while (results.next()) {
                String name = results.getString("task");
                String start = results.getString("start");
                String end = results.getString("end");
                Task task = new Task (name, start, end);

                String user = results.getString("username");
                String ip = results.getString("ip");
                String str_port = results.getString("port");

                if (user != null && ip != null && str_port != null) {
                    int port = Integer.parseInt(str_port);
                    task.addUser(user, ip, port);
                }
                tasks.add(task);
            }
        } catch (Exception e) {
            System.err.println("failed to get project: " + e.getMessage());
            return null;
        }
        return tasks;
    }
}

