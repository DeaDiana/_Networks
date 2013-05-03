package ru.nsu.fit.anisutina.md5;

import ru.nsu.fit.anisutina.transmission.DataReceiver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.*;
import java.security.MessageDigest;

/**
 * Created with IntelliJ IDEA.
 * User: Diana
 * Date: 04.05.13
 * Time: 1:16
 * To change this template use File | Settings | File Templates.
 */
public class Md5HackerServer {
    private static final int PORT = 5500;
    private static ServerSocket serverSocket = null;
    private static Socket socket = null;
    private static String secret = "3";//"theSecret";
    private static Integer expectedResult = 33;
    private static Integer rightRange = 100000000;
    private static Integer lengthInterval = 20;
    private static byte[] secretBytes = null;
    private static StringBuffer cryptedSecretString = null;

    public static void main(String args[]) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        Integer start = 0;
        if(args.length > 0) {
            secret = args[0];
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            //md.update(secret.getBytes());
            md.update(expectedResult.byteValue());
            System.out.println(expectedResult.byteValue());
            secretBytes = md.digest();
            cryptedSecretString = new StringBuffer();
            for (int i = 0; i < secretBytes.length; i++) {
                cryptedSecretString.append(Integer.toString((secretBytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            System.out.println("secret's hash: " + cryptedSecretString);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("did not get the message digest [main]:Md5HackerServer");
        }

        try {
            serverSocket = new ServerSocket(PORT);
            while (true)
            {
                socket = serverSocket.accept();
                Callable<Integer> receiver = new Md5Employer(socket, cryptedSecretString.toString(), start, lengthInterval);
                Future<Integer> future = executor.submit(receiver);
                start += lengthInterval;
                try {
                    if(null != future.get()) {
                        System.out.println("SUCCESS" + future.get().toString());
                        break;
                    }
                } catch (InterruptedException e) {
                    System.err.println("future is not ready yet (interrupted)[main]:Md5HackerServer");
                } catch (ExecutionException e) {
                    System.err.println("future is not ready yet (execution)[main]:Md5HackerServer");
                }
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
