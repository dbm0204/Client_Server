import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {

    Server server = null;
    Database db = null;
    
    public Parser(Server server, Database db) {
        this.server = server;
        this.db = db;
    }

    public String parse (String s) {
        String[] parts = s.split(";");
        ArrayList<String> cmds;
        cmds = new ArrayList<String>(Arrays.asList(parts));
        String result = "";
        
        while(!cmds.isEmpty()) {
            String cmd = cmds.remove(0);       
            if (cmd.contains("PROJECT_DEFINITION")) {
                String proj = cmd.split(":")[1];
                int tasks = Integer.parseInt(cmds.remove(0).split(":")[1]);
                result += project_definition(proj, cmds.subList(0, tasks * 3));
                cmds.subList(0, tasks * 3).clear();
            } else if (cmd.contains("TAKE")) {
                System.out.println(cmds.toString());
                take(cmds.subList(0, 3));
                result += "OK;" + s;
                cmds.subList(0, 3).clear();
            } else if (cmd.equals("GET_PROJECTS")) {
                result += get_projects();
            } else if (cmd.equals("GET_PROJECT")) {
                String proj = cmds.remove(0);
                result += get_project(proj);
            }
        }
        return result;
    }
    
    public String project_definition(String proj, List<String> cmds) {
        assert(cmds.size() % 3 == 0);
        db.createProject(proj);
        for (int i = 0; i < (cmds.size() / 3); i++) {
            String task = cmds.get(i * 3);
            String start = cmds.get(i * 3 + 1);
            String end = cmds.get(i * 3 + 2);
            db.createTask(proj, task, start, end);
        }
        return "OK;PROJECT_DEFINITION:"+proj;
    }
    
    public void take (List<String> cmds) {
        assert(cmds.size() == 3);
        String user = cmds.get(0).split(":")[1];
        String project = cmds.get(1).split(":")[1];
        String task = cmds.get(2);
        String ip = server.getClientIp();
        int port = server.getClientPort();
        db.assignTask(project, task, user, ip, port);  
    }
    
    public String get_projects () {
        ArrayList<String> projects = db.getProjects();
        String result = "OK;PROJECTS:"+projects.size();
        for (int i = 0; i < projects.size(); i++) {
            result += ";" + projects.get(i);
        }
        return result;
    }
    
    public String get_project(String project) {
         return db.getProject(project);
    }
    
    public static void main(String[] args) {
        //Parser p = new Parser();
        //p.parse("PROJECT_DEFINITION:Exam;TASKS:2;Buy paper;2016-03-12:18h30m00s001Z;2016-03-15:18h30m00s001Z;Write exam;2016-03-15:18h30m00s001Z;2016-03-15:18h30m00s001Z;");
    }
}
