import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Date;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.lang.StringBuilder;

public class Database {
    private String dataFile = null;
    private Connection con = null;
    private String initFile = null;
    
    public Database(String dbFile, String initFile) {
        dataFile = dbFile;
        this.initFile = initFile;
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
        }
        return projects;
    }
    
    public ArrayList<Task> getProject(String proj) {
        ArrayList<Task> tasks = new ArrayList<Task>();                       
        String sql = "SELECT * FROM projects " +
                     "LEFT JOIN tasks ON " +
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
        }  
        return tasks;
    }
}

