package ru.nsu.fit.anisutina.rate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.CharBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: Diana
 * Date: 24.03.13
 * Time: 21:34
 * To change this template use File | Settings | File Templates.
 */
public class RateServer {
    private static final int PORT = 5538;
    private static ServerSocket serverSocket = null;
    private static InputStreamReader inputStream = null;
    private static Socket socket = null;
    private static final int LENGTH = 100;
    private static final int NUMMILISEC = 1000;

    public static void main(String args[]){
        try {
            serverSocket = new ServerSocket(PORT);
            socket = serverSocket.accept();
            inputStream = new InputStreamReader(socket.getInputStream());
            char[] message_buf = new char[LENGTH];
            int iter = 1;
            long timer = System.currentTimeMillis();
            int num = inputStream.read(message_buf);
            while (LENGTH == num)
            {
                num = inputStream.read(message_buf);
                iter++;
            }
            timer = System.currentTimeMillis() - timer;
            if(0 < timer) {
            System.out.println((((iter * LENGTH + num) * NUMMILISEC / timer) / 1024) / 1024 + " Mb/sec");
            } else {    System.out.println("Too fast");    }
        } catch (IOException e) {
            System.err.println("server-socket or  client-socket was not created [main]:RateServer");
        }

        finally {
                try {
                    if(inputStream != null)     {   inputStream.close();    }
                } catch (IOException e) {   System.err.println("input stream was not closed [main]:RateServer");    }
                finally {
                    try {
                        if(socket != null)     {    socket.close();    }
                    } catch (IOException e) {   System.err.println("client-socket was not closed [main]:RateServer");   }
                    finally {
                        try {
                            if(serverSocket != null)     {   serverSocket.close();   }
                        } catch (IOException e) {   System.err.println("server-socket was not closed [main]:RateServer");   }
                    }
                }
        }
    }
}
