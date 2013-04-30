package ru.nsu.fit.anisutina.transmission;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created with IntelliJ IDEA.
 * User: Diana
 * Date: 24.03.13
 * Time: 23:01
 * To change this template use File | Settings | File Templates.
 */
public class DataTransmissionClient {
    private static String filename_toread = "5.txt";
    private static String filename_tocreate = "5.txt";
    private static InetAddress IP = null;
    private static Integer PORT = 5539;
    private static Socket socket = null;
    private static FileInputStream inputStream = null;
    private static OutputStream outputStream = null;
    private static final int LENGTH = 3 * 1024;

    public static void main(String args[]){
        if(args.length > 0){
            filename_toread = args[0];
            filename_tocreate = args[0];
            PORT = Integer.parseInt(args[2]);
            try {
                IP = InetAddress.getByName(args[1]);
            } catch (UnknownHostException e) {
                System.err.println("did not convert from string to InetAddress [main]:DataTransmissionClient");
            }
        } else {
            try {
                IP = InetAddress.getByName("192.168.1.4");
            } catch (UnknownHostException e) {
                System.err.println("did not convert from string to InetAddress [main]:RateClient");
            }
        }

        try {
            inputStream = new FileInputStream(filename_toread);
            socket = new Socket(IP, PORT);
            outputStream = socket.getOutputStream();
            byte[] message = new byte[LENGTH];

            outputStream.write(filename_tocreate.getBytes());
            while (LENGTH == inputStream.read(message)) {
                outputStream.write(message);
            }
            outputStream.write(message);
        }catch (FileNotFoundException e) {
            System.err.println("file was not opened [main]:DataTransmissionClient");
        }catch (IOException e) {
            System.err.println("probably socket was not created [main]:DataTransmissionClient");
        }

        finally {
            if(inputStream != null) {
                try {
                       inputStream.close();
                } catch (IOException e) {   System.err.println("input stream was not closed [main]:DataTransmissionClient");    }
            }
            if(outputStream != null) {
                try {
                        outputStream.close();
                } catch (IOException e) {   System.err.println("output stream was not closed [main]:DataTransmissionClient");   }
            }
            if(socket != null) {
                    try {
                           socket.close();
                    } catch (IOException e) {   System.err.println("socket was not closed [main]:DataTransmissionClient");   }
                }
            }
        }
}
