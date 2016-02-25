
import java.lang.Thread;
import java.lang.InterruptedException;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;

public class Server implements Runnable {
    private int port = 2236;        //default values
    private Socket clientSock = null;
    private Thread server = null;

    private Server (Socket client) {
        clientSock = client;
    }

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        ServerSocket serverSock;

        try {
            serverSock = new ServerSocket(port);
            while (true) {
                clientSock = serverSock.accept();
                Runnable runnable = new Server(clientSock);
                server = new Thread(runnable);
                server.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {    
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        InputStreamReader streamReader = null;
        BufferedReader bufferedReader = null;        
        OutputStreamWriter streamWriter = null;
        BufferedWriter bufferedWriter = null;
        String[] args;

        try {
            InputStream inputStream = clientSock.getInputStream();
            streamReader = new InputStreamReader(inputStream); 
            bufferedReader = new BufferedReader(streamReader);
   
            OutputStream outputStream = clientSock.getOutputStream();
            streamWriter = new OutputStreamWriter(outputStream);
            bufferedWriter = new BufferedWriter(streamWriter);

            String input = null;
            while(input == null || !input.equals("LOGOUT")) {
                if (bufferedReader.ready()) {
                    input = bufferedReader.readLine();
                    args = input.split(";");
                    System.out.println(input); 
                    bufferedWriter.write(input); 
                }
            }

            clientSock.close();
            bufferedWriter.close();
            bufferedReader.close();
        } catch (SocketTimeoutException e1) {
            System.err.println("Server Timed out: " + e1.getMessage()); 
        } catch (IOException e2) {
            System.err.println("IO Error: " + e2.getMessage());
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(2236);
        server.start();
    }
}
