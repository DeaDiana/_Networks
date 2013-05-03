package ru.nsu.fit.anisutina.md5;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: Diana
 * Date: 04.05.13
 * Time: 1:16
 * To change this template use File | Settings | File Templates.
 */
public class Md5HackerClient {
    private static int PORT = 5500;
    private static InetAddress IP = null;
    private static Socket socket = null;
    private static InputStream inputStream = null;
    private static OutputStream outputStream = null;
    private static Integer lengthInteval = 20;

    public static void main(String args[]) {
        if(args.length > 0){
            PORT = Integer.parseInt(args[1]);
            try {
                IP = InetAddress.getByName(args[0]);
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
            socket = new Socket(IP, PORT);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            byte[] byte_buffer = new byte[100];
            int num = inputStream.read(byte_buffer);
            Integer start = new Integer(byte_buffer[0]);
            Integer current = start;
            Integer last = start + lengthInteval;
            MessageDigest md = MessageDigest.getInstance("MD5");
            do {
                md.update(current.byteValue());
                System.out.println("current:" + current.byteValue());
                outputStream.write(md.digest());
                current++;
            } while (!last.equals(current));
        } catch (IOException e) {
            System.err.println("socket was not created");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("did not create messge digest[main]:Md5HackerClient");
        }
        finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {   System.err.println("input stream was not closed [main]:Md5HackerClient");    }
            }
            if(outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {   System.err.println("output stream was not closed [main]:Md5HackerClient");    }
            }
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {   System.err.println("socket was not closed [main]:Md5HackerClient");    }
            }
        }
    }
}
