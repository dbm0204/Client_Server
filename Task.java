import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

public class Task {
    private String task;
    private Date start;
    private Date end;
    
    private String user;
    private String ip;
    private int port;

    private String dateReg = "yyyy-MM-dd:HH'h'mm'm'ss's'SSS'Z'";
    private DateFormat fmt = new SimpleDateFormat(dateReg);

    public Task (String task, String start, String end) {
        this.task = task;
        try {
            this.start = fmt.parse(start);
            this.end = fmt.parse(end);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addUser(String user, String ip, int port) {
        this.user = user;
        this.ip = ip;
        this.port = port;
    }
    
    public String getStatus() {
        Date now = new Date();
        return (now.after(end) && user!=null) ? "DONE":"WAITING";
    }

    @Override
    public String toString() {
        String buffer = task + ";" + fmt.format(start) + 
                        ";" + fmt.format(end) + ";";
                        
        if (user !=null) {
            buffer += user + ";" + ip + ";" + port + ";";
        }
        return buffer + getStatus() + ";";
    }

}
