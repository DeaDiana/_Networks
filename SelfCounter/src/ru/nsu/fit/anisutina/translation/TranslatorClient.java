package ru.nsu.fit.anisutina.translation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.String;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created with IntelliJ IDEA.
 * User: Diana
 * Date: 30.04.13
 * Time: 0:03
 * To change this template use File | Settings | File Templates.
 */
public class TranslatorClient {
    private static String filename_toread = "ThinkingInJava4th.pdf";
    private static String filename_tocreate = "ThinkingInJava4th.pdf";
    private static InetAddress translatorIP = null;
    private static Integer translatorPORT = 6001;
    private static Socket socket = null;
    private static FileInputStream inputStream = null;
    private static OutputStream outputStream = null;
    private static final int LENGTH = 3 * 1024;

    public static void main(String args[]) {
        if(args.length > 0){
            filename_toread = args[0];
            filename_tocreate = args[0];
            translatorPORT = Integer.parseInt(args[2]);
            try {
                translatorIP = InetAddress.getByName(args[1]);
            } catch (UnknownHostException e) {
                System.err.println("did not convert from string to InetAddress [main]:TranslationClient");
            }
        } else {
            try {
                translatorIP = InetAddress.getByName("192.168.1.4");
            } catch (UnknownHostException e) {
                System.err.println("did not convert from string to InetAddress [main]:TranslatorClient");
            }
        }
        try {
            inputStream = new FileInputStream(filename_toread);
            socket = new Socket(translatorIP, translatorPORT);
            outputStream = socket.getOutputStream();
            byte[] message = new byte[LENGTH];

            outputStream.write(filename_tocreate.getBytes());
            while (-1 != inputStream.read(message)) {
                outputStream.write(message);
            }
        }catch (FileNotFoundException e) {
            System.err.println("file was not opened [main]:TranslatorClient");
        }catch (IOException e) {
            System.err.println("probably socket was not created [main]:TranslatorClient");
        }
        finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {   System.err.println("input stream was not closed [main]:TranslatorClient");    }
            }
            if(outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {   System.err.println("output stream was not closed [main]:TranslatorClient");   }
            }
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {   System.err.println("socket was not closed [main]:TranslatorClient");   }
            }
        }
    }
}
