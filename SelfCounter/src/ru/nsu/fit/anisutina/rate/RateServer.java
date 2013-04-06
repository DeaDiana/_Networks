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
    private static final int PORT = 5554;
    private static ServerSocket serverSocket = null;
    private static InputStream inputStream = null;
    private static Socket socket = null;
    private static final int LENGTH = 3 * 1024;
    private static int readBytes[] = null;
    private static long spendTime[] = null;


    public static void main(String args[]){
        try {
            serverSocket = new ServerSocket(PORT);
            socket = serverSocket.accept();
            inputStream = socket.getInputStream();
            byte[] message_buf = new byte[LENGTH];
            readBytes = new int[10];
            spendTime = new long[10];
            int iter = 1;
            long timer = System.currentTimeMillis();
            int num = inputStream.read(message_buf);
            spendTime[0] = System.currentTimeMillis() - timer + 1;
            readBytes[0] = num;
            while (LENGTH == num)
            {
                timer = System.currentTimeMillis();
                readBytes[iter % 10] = inputStream.read(message_buf);
                spendTime[iter % 10] = System.currentTimeMillis() - timer + 1;
                num = readBytes[iter % 10];
                iter++;
            }
            for(int i = 1; i < (iter % 10); i++){
                readBytes[0] += readBytes[i];
                spendTime[0] +=spendTime[i];
            }
            System.out.println((readBytes[0] / spendTime[0]) + " b/sec");
        } catch (IOException e) {
            System.err.println("server-socket or  client-socket was not created [main]:RateServer");
        }

        finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {   System.err.println("input stream was not closed [main]:RateServer");    }
            }
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {   System.err.println("client-socket was not closed [main]:RateServer");   }
            }
            if(serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {   System.err.println("server-socket was not closed [main]:RateServer");   }
            }
        }
    }
}
