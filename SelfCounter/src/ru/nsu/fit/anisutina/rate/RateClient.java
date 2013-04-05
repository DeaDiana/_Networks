package ru.nsu.fit.anisutina.rate;

import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import java.io.*;
import java.net.InetAddress;
import java.lang.String;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created with IntelliJ IDEA.
 * User: Diana
 * Date: 24.03.13
 * Time: 19:33
 * To change this template use File | Settings | File Templates.
 */
public class RateClient {
    private static String filename = "5.txt";
    private static InetAddress IP = null;
    private static Integer PORT = 5554;
    private static Socket socket = null;
    private static FileInputStream inputStream = null;
    private static OutputStream outputStream = null;
    private static final int LENGTH = 3 * 1024;

    public static void main(String args[]){

        if(args.length > 0){
            filename = args[0];
            PORT = Integer.parseInt(args[2]);
            try {
                IP = InetAddress.getByName(args[1]);
                //IP = InetAddress.getByName(new String("10.4.0.2"));
            } catch (UnknownHostException e) {
                System.err.println("did not convert from string to InetAddress [main]:RateClient");
            }
        } else {    }

        try {
            inputStream = new FileInputStream(filename);
            socket = new Socket("wDiana", PORT);//IP, PORT);
            outputStream = socket.getOutputStream();
            byte[] message = new byte[LENGTH];
            while (LENGTH == inputStream.read(message))
            {
                outputStream.write(message);
            }
            outputStream.write(message);
        }catch (FileNotFoundException e) {
            System.err.println("file was not opened [main]:RateClient");
        }catch (IOException e) {
            System.err.println("probably socket was not created [main]:RateClient");
        }

        finally {
            try {
                if(inputStream != null)     {   inputStream.close();    }
            } catch (IOException e) {   System.err.println("input stream was not closed [main]:RateClient");    }
            finally {
                try {
                    if(outputStream != null)     {   outputStream.close();    }
                } catch (IOException e) {   System.err.println("output stream was not closed [main]:RateClient");    }
                finally {
                    try {
                        if(socket != null)     {   socket.close();   }
                    } catch (IOException e) {   System.err.println("socket was not closed [main]:RateClient");   }
                }
            }
        }
    }
}
