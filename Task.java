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
    
    public String getName() {
    	return task;
    }
    
    public String getUser() {
    	return user;
    }
    
    public String getStartTime() {
    	return fmt.format(start);
    }
    
    public String getEndTime() {
    	return fmt.format(end);
    }
    
    public String getIp() {
    	return ip;
    }
    
    public int getPort() {
    	return port;
    }
    
    public boolean isDone() {
    	 Date now = new Date();
         return (now.after(end) && user!=null) ? true:false;
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
