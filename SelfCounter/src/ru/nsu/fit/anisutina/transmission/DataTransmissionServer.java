package ru.nsu.fit.anisutina.transmission;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: Diana
 * Date: 24.03.13
 * Time: 23:01
 * To change this template use File | Settings | File Templates.
 */
public class DataTransmissionServer {
    private static final int PORT = 5539;
    private static ServerSocket serverSocket = null;
    private static Socket socket = null;

    public static void main(String args[]){
        ExecutorService executor = Executors.newFixedThreadPool(5);
        try {
            serverSocket = new ServerSocket(PORT);
            while (true)
            {
                socket = serverSocket.accept();
                Runnable receiver = new DataReceiver(socket);
                executor.execute(receiver);
            }
        } catch (IOException e) {
            System.err.println("server socket was not created or server-socked failed on accept [main]:DataTransmissionServer" + Object.class);
        }
        finally {
            executor.shutdown();
            while (!executor.isTerminated()){}
            if(socket != null) {
                try {
                       socket.close();
                } catch (IOException e) {   System.err.println("socket was not closed [main]:DataTransmissionServer");    }
            }
            if(serverSocket != null) {
                try {
                        serverSocket.close();
                } catch (IOException e) {   System.err.println("server-socket was not closed [main]:DataTransmissionServer");   }
            }
        }
        System.out.println("Done.");
    }
}