package ru.nsu.fit.anisutina.md5;

import java.io.*;
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
    private static BufferedReader inputStream = null;
    private static PrintWriter outputStream = null;
    private static final char[] ACGT = {'A', 'C', 'G', 'T'};
    private static String expected_result = null;
    private static Integer start = null;
    private static Integer end = null;
    private static String original_string = null;

    private static String encodeStringByMd5(String to_encode) {
        MessageDigest md = null;
        StringBuffer sb = new StringBuffer();
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(to_encode.getBytes());
            byte byteData[] = md.digest();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            System.err.println("did not get MessageDigest [encodeStringByMd5]:Md5HackerClient");
        }
        return sb.toString();
    }

    public static void main(String args[]) {
        if(args.length > 0){
            PORT = Integer.parseInt(args[1]);
            try {
                IP = InetAddress.getByName(args[0]);
            } catch (UnknownHostException e) {
                System.err.println("did not convert from string to InetAddress [main]:Md5HackerClient");
            }
        } else {
            try {
                IP = InetAddress.getByName("192.168.1.10");
            } catch (UnknownHostException e) {
                System.err.println("did not convert from string to InetAddress [main]:Md5HackerClient");
            }
        }

        try {

            socket = new Socket(IP, PORT);
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            if(inputStream == null) System.err.println("in is null");
            outputStream = new PrintWriter(socket.getOutputStream(), true);
            if(outputStream == null) System.err.println("out is null");
            outputStream.println("GET_WORK");
            String task_line = inputStream.readLine();
            inputStream.close();
            outputStream.close();
            socket.close();

            //String task_line = "10 20 cd6a9bd2a175104eed40f0d33a8b4020";
            String[] parameters = task_line.split(" ");
            start = new Integer(parameters[0]);
            end = new Integer(parameters[1]);
            expected_result = parameters[2];
            int devided4 = 0;
            int multed4 = 0;
            int rest = 0;
            //main loop
            for(int current_orig = start; current_orig <= end; current_orig++) {
                StringBuilder construct_code = new StringBuilder();
                int current = current_orig;
                do{
                    devided4 = (current >> 2);

                    multed4 = (devided4 << 2);
                    rest = current - multed4;
                    construct_code.append(ACGT[rest]);
                    current = devided4;

                } while (current != 0);
                String decoded_string = construct_code.reverse().toString();
                String coded_string = encodeStringByMd5(decoded_string);
                if(expected_result.equals(coded_string)) {
                    original_string = decoded_string;
                    break;
                }
            }
            if(original_string != null) {
                System.out.println(original_string);
                System.out.println(expected_result);
                socket = new Socket(IP, PORT);
                outputStream = new PrintWriter(socket.getOutputStream(), true);
                outputStream.println("LISTEN_RESULT");
                outputStream.println(original_string);
                outputStream.close();
                socket.close();
            } else {
                System.out.println("[" + start + " " + end + "] not found");
                System.out.println(expected_result);
            }
        } catch (IOException e) {
            System.err.println("socket was not created [main]:Md5HackerClient");
        }
        finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {   System.err.println("input stream was not closed [main]:Md5HackerClient");    }
            }
            if(outputStream != null) {
                    outputStream.close();
            }
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {   System.err.println("socket was not closed [main]:Md5HackerClient");    }
            }
        }
    }
}
