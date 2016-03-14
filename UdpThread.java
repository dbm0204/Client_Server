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


public class UdpThread implements Runnable {

    private Database db;
    private Socket serverSock = null;

    private UdpThread (Socket server, Database db) {
        serverSock = server;
        this.db = db;
    }

    @Override
    public void run() {
        BufferedReader bufferedReader = null;        
        OutputStreamWriter streamWriter = null;
        BufferedWriter bufferedWriter = null;
        ByteBuffer buffer = ByteBuffer.allocate(0);
        
        try {

            SocketAddress client = serverSock.receive(buffer);
            
            
            Parser parser = new Parser(inputStream);
            parser.setDatabase(db);
            
            while (!serverSock.isClosed()) {
                parser.parse(bufferedWriter, clientIp, clientPort);
            }

        } catch (SocketTimeoutException e1) {
            System.err.println("Server Timed out: " + e1.getMessage()); 
        } catch (IOException e2) {
            System.err.println("IO Error: " + e2.getMessage());
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        Thread.currentThread().interrupt();
    }
}
