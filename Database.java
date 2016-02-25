import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {
    private String dataFile = null;
    private Connection con = null;
    
    public Database(String file) {
        dataFile = file;
        connect();
    } 

    public void connect() {
        File file = new File(dataFile);
        assert(file.exists());

        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + dataFile;
            con = DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("connected");
    }

    public void initDatabase() {

        ResultSet results;
            
        String project_table = "CREATE TABLE projects ( " +
                               "id      INT PRIMARY KEY     NOT NULL," +
                               "name    TEXT                NOT NULL," +
                               "start   DATE                NOT NULL," +
                               "stop    DATE                NOT NULL);";
                               
         String task_table = "CREATE TABLE tasks ( " +
                             "id        INT PRIMARY KEY     NOT NULL," +
                             "name      TEXT                NOT NULL," +
                             "start     DATE                NOT NULL," +
                             "stop      DATE                NOT NULL);";
                             
         String definition_table = "CREATE TABLE definitions ( " +
            "project_id  INT     NOT NULL," +
            "task_id     INT     NOT NULL," +
            "FOREIGN KEY (project_id) REFERENCES projects(id)," +
            "FOREIGN KEY (task_id) REFERENCES tasks(id));";                      
                                 
         String user_table = "CREATE TABLE users ( " +
                             "id    INT PRIMARY KEY     NOT NULL," +
                             "name  TEXT                NOT NULL," +
                             "ip    TEXT                NOT NULL)";
         
         String schedule_table = "CREATE TABLE schedules ( " +
            "task_id   INT     NOT NULL," +
            "user_id   INT     NOT NULL," +
            "FOREIGN KEY (task_id) REFERENCES tasks(id)," +
            "FOREIGN KEY (user_id) REFERENCES users(id));";   

        try {
            Statement statement = con.createStatement();
            statement.executeUpdate(project_table);
            statement.executeUpdate(task_table);
            statement.executeUpdate(definition_table);
            statement.executeUpdate(user_table);
            statement.executeUpdate(schedule_table);
            
            statement.close();
            con.commit();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   

    public static void main(String[] args) {
        Database db = new Database("test.db");
        db.initDatabase();
    }
}

