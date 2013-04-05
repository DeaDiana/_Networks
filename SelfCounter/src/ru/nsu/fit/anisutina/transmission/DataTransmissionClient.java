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
    private static String filename_toread = "C:\\Users\\Diana\\Networks_projects\\SelfCounter\\5.txt";
    private static String filename_tocreate = "C:\\Users\\Diana\\Networks_projects\\SelfCounter\\6.txt";
    private static InetAddress IP = null;
    private static Integer PORT = 5538;
    private static Socket socket = null;
    private static FileInputStream inputStream = null;
    private static OutputStream outputStream = null;
    private static final int LENGTH = 100;

    public static void main(String args[]){
        if(args.length > 0){
            filename_toread = args[0];
            PORT = Integer.parseInt(args[2]);
            try {
                IP = InetAddress.getByName(args[1]);
            } catch (UnknownHostException e) {
                System.err.println("did not convert from string to InetAddress [main]:DataTransmissionClient");
            }
        }

        try {
            inputStream = new FileInputStream(filename_toread);
            socket = new Socket("wDiana", PORT);//(IP, PORT);
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
            try {
                if(inputStream != null)     {   inputStream.close();    }
            } catch (IOException e) {   System.err.println("input stream was not closed [main]:DataTransmissionClient");    }
            finally {
                try {
                    if(outputStream != null)     {    outputStream.close();    }
                } catch (IOException e) {   System.err.println("output stream was not closed [main]:DataTransmissionClient");   }
                finally {
                    try {
                        if(socket != null)     {   socket.close();   }
                    } catch (IOException e) {   System.err.println("socket was not closed [main]:DataTransmissionClient");   }
                }
            }
        }
    }
}
