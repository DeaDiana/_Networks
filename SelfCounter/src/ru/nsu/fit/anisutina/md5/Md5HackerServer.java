package ru.nsu.fit.anisutina.md5;

import ru.nsu.fit.anisutina.transmission.DataReceiver;

import java.io.*;
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
    private static String file_name = "";
    private static String cryptedSecretString = "0f1577caba02be3034205b3ab59a97e2";
    private static final Integer STEP = 10000;

    public static void main(String args[]) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        if(args.length > 0) {
            file_name = args[0];
            File secret_file = new File(file_name);
            if(secret_file.exists()) {
                try {
                    BufferedReader fileInputStream = new BufferedReader(new InputStreamReader(new FileInputStream(secret_file)));
                    cryptedSecretString = fileInputStream.readLine();
                } catch (IOException e) {
                    System.err.println("did not read md5-hash from " + file_name + " [main]:Md5HackerServer");
                }
            }
        }
        System.out.println("trying to hack:");
        System.out.println(cryptedSecretString);
        try {
            serverSocket = new ServerSocket(PORT);
            Integer start = 0;
            Integer end = STEP;
            while (true)
            {
                socket = serverSocket.accept();
                Runnable receiver = new Md5Employer(socket, cryptedSecretString, start, end);
                executor.execute(receiver);
                start = end;
                end += STEP;
            }
        } catch (IOException e) {
            System.err.println("server socket was not created or server-socked failed on accept [main]:Md5HackerServer" + Object.class);
        }
        finally {
            executor.shutdown();
            while (!executor.isTerminated()){}
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {   System.err.println("socket was not closed [main]:Md5HackerServer");    }
            }
            if(serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {   System.err.println("server-socket was not closed [main]:Md5HackerServer");   }
            }
        }
        System.out.println("Done.");
    }
}
