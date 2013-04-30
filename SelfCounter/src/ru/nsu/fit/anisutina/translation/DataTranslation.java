package ru.nsu.fit.anisutina.translation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.Runnable;

/**
 * Created with IntelliJ IDEA.
 * User: Diana
 * Date: 30.04.13
 * Time: 0:28
 * To change this template use File | Settings | File Templates.
 */
public class DataTranslation implements Runnable{
    private static final int LENGTH = 3 * 1024;
    private static final int FILENAME_LEN = 21;
    ServerSocket serverSocket = null;
    Socket socketFrom = null;
    Socket socketTo = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private InetAddress serverIP = null;
    private Integer serverPORT = null;

    private DataTranslation(){};
    public DataTranslation(Integer PORTlistenTo, InetAddress srvIP, Integer srvPORT) {
        serverIP = srvIP;
        serverPORT = srvPORT;
        try {
            serverSocket = new ServerSocket(PORTlistenTo);
        } catch (IOException e) {
            System.err.println("sockets were not created [constructor]:DataTranslation");
        }
    }
    @Override
    public void run() {
        try {
            while (true) {
                socketFrom = serverSocket.accept();
                inputStream = socketFrom.getInputStream();
                socketTo = new Socket(serverIP, serverPORT);
                outputStream = socketTo.getOutputStream();
                byte [] filename_buf = new byte[FILENAME_LEN];
                byte [] message_buf = new byte[LENGTH];
                inputStream.read(filename_buf);
                outputStream.write(filename_buf);
                int num = inputStream.read(message_buf);
                outputStream.write(message_buf);
                while (-1 != num) {
                    num = inputStream.read(message_buf);
                    if(num > 0) {   outputStream.write(message_buf, 0, num);    }
                }
                inputStream.close();
                outputStream.close();
                socketFrom.close();
                socketTo.close();
            }
        } catch (FileNotFoundException e) {
            System.err.println("file not found [run]:DataTranslator");
        } catch (IOException e) {
            System.err.println("failed on getting input stream from the socket [run]:DataTranslator");
        }
        finally {
            if(inputStream != null) {
                try {
                    inputStream.close();    System.out.println("received");
                } catch (IOException e) {   System.err.println("input stream was not closed [run]:DataTranslator");    }
            }
            if(outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {   System.err.println("file output stream was not closed [run]:DataTranslator");   }
            }
            if(socketFrom != null) {
                try {
                    socketFrom.close();
                } catch (IOException e) {   System.err.println("socketFrom was not closed [run]:DataTranslator");   }
            }
            if(socketTo != null) {
                try {
                    socketTo.close();
                } catch (IOException e) {   System.err.println("socketTo was not closed [run]:DataTranslator");   }
            }
            if(serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {   System.err.println("server socket was not closed [run]:DataTranslator");   }
            }
        }
    }
}
