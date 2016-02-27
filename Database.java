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
    
    public Database(String file) {
        dataFile = file;
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
        System.out.println("sql connected");
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
            Path path = Paths.get("init_db.sql");
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
    
    public void createTask(String project, String task,
    String start, String end) {
        String args = project+"','"+task+"','"+start+"','"+end;
        String insert_task = "INSERT INTO tasks " +
                             "(project, task, start, end) " +
                             "VALUES ('"+args+"')";                     
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate(insert_task);
            con.commit();
            statement.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    public void assignTask(String project, String task, 
    String user, String ip, int port) {
    
        String sql = "INSERT INTO users " + 
                     "(username, ip, port, task_id)" +
                     "SELECT '"+user+"','"+ip+"',"+
                     port+", id FROM tasks WHERE " +
                     "project = '"+project+"' AND " +
                     "task = '"+task+"';";
                     
        //String update = "UPDATE tasks SET isDone=1 WHERE id IN " +
        //                "(SELECT task_id FROM users)";             
        
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate(sql);
            con.commit();
            statement.close();
        } catch (Exception e) {
            System.err.println("failed to assign task: " + e.getMessage());
        }
    }
    
    public ArrayList<String> getProjects() {
        String sql = "SELECT DISTINCT project FROM tasks";
        ArrayList<String> projects = new ArrayList<String>();
        try {
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(sql);
            while (results.next()) {
                String project = results.getString("project");
                if (project != null) {
                    projects.add(project);
                }
            }
            results.close();
            statement.close();
        } catch (Exception e) {
            System.err.println("failed to get projects: " + e.getMessage());
        }
        return projects;
    }
    
    public String getProject(String project) {
        String result = "";
        String[] cols = {"task", "start", "end", 
                         "username", "ip", "port"};
                         
        String sql = "SELECT * FROM tasks " +
                     "LEFT JOIN users ON " +
                     "tasks.id=users.task_id;";
                     
        DateFormat fmt;
        fmt = new SimpleDateFormat("yyyy-MM-dd:HH'h'mm'm'ss's'SSS'Z'");
        Date now = new Date();
        System.out.println("Today is: " + fmt.format(now));
        
        try {
            Statement statement = con.createStatement();
            ResultSet results = statement.executeQuery(sql);
            int counter = 0;
            while (results.next()) {
                for (String col: cols) {
                    result += results.getString(col) + ";";
                }
                
                Date date = fmt.parse(results.getString("end"));
                String user = results.getString("username");
                if (now.after(date) && user != null) {
                    result += "DONE;";
                } else {
                    result += "WAITING;";
                }
                counter++;
            }
            result = "OK;PROJECT_DEFINITION:"+project+";TASKS:"+ 
                     counter+";"+result;
            
        } catch (Exception e) {
            System.err.println("failed to get project: " + e.getMessage());
        }
        
        return result;
    }

    public static void main(String[] args) {
        Database db = new Database("test.db");
        String date = "2016-03-12:18h30m00s001Z";
        db.createTask("exam", "write exam", date, date);
        db.assignTask("exam", "write exam", "john", "10.0.0.0", 2232);
        ArrayList<String> projects = db.getProjects();
        if (projects != null) {
            System.out.println("Projects: " + projects.size());
            for (int i = 0; i < projects.size(); i++) {
                System.out.println(projects.get(i));
            }
        }
        System.out.println(db.getProject("exam"));
        db.disconnect();
    }
}

