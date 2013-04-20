package ru.nsu.fit.anisutina.recvpost;

import java.io.*;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: Diana
 * Date: 19.04.13
 * Time: 22:48
 * To change this template use File | Settings | File Templates.
 */
public class Pop3PostReceiver {
    private static String pop3Server = "ccfit.nsu.ru";
    private static Socket socket = null;
    private final static int PORT = 110;
    private static BufferedReader inputStream = null;
    private static BufferedReader inReader = null;
    private static OutputStreamWriter fileOutputStream = null;
    private static OutputStream outputStream = null;
    private static String username = "";
    private static String password = "";
    private static String num_of_letter = "";
    private static final int BUFFERSIZE = 1024;

    public static void main(String args[]) {
        if(args.length > 0) {
            pop3Server = args[0];
        }
        try {
            socket = new Socket(pop3Server, PORT);
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = socket.getOutputStream();
            String response = inputStream.readLine();
            System.out.println(response);

            inReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter your username:");
            username = inReader.readLine();

            String COMMAND = "USER " + username + "\r\n";
            outputStream.write(COMMAND.getBytes());

            response = inputStream.readLine();
            System.out.println(response);
            if('-' != response.charAt(0)) {
                System.out.println("Enter your password:");
                password = inReader.readLine();
                COMMAND = "PASS " + password + "\r\n";
                outputStream.write(COMMAND.getBytes());
                response = inputStream.readLine();
                System.out.println(response);
                if('-' != response.charAt(0)) {
                    COMMAND = "STAT" + "\r\n";
                    outputStream.write(COMMAND.getBytes());
                    response = inputStream.readLine();
                    System.out.println("STAT\n" + response);
                    if('-' != response.charAt(0)) {
                        System.out.println("Load letter number | [quit]:");
                        num_of_letter = inReader.readLine();

                        while(!num_of_letter.equals("quit")) {
                            COMMAND = "RETR " + num_of_letter + "\r\n";
                            File letter = new File("letter_" + num_of_letter + ".txt");
                            outputStream.write(COMMAND.getBytes());
                            char[] message_buf = new char[BUFFERSIZE];

                            if(!letter.exists()) {
                                fileOutputStream = new OutputStreamWriter(new FileOutputStream(letter));
                                int num = inputStream.read(message_buf);
                                fileOutputStream.write(message_buf);
                                while (BUFFERSIZE == num) {
                                    num = inputStream.read(message_buf);
                                    if(num > 0) {   fileOutputStream.write(message_buf, 0, num);    }
                                }
                            }
                            fileOutputStream.close();
                            System.out.println("Enter number of letter or 'quit':");
                            num_of_letter = inReader.readLine();
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Socket was not created [main]:Pop3PostReceiver");
        }
        finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {   System.err.println("input socket stream was not closed [main]:Pop3PostReceiver");    }
            }
            if(outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {   System.err.println("output socket stream was not closed [main]:Pop3PostReceiver");    }
            }
            if(inReader != null) {
                try {
                    inReader.close();
                } catch (IOException e) {   System.err.println("input std stream was not closed [main]:Pop3PostReceiver");    }
            }
            if(fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {   System.err.println("input file stream was not closed [main]:Pop3PostReceiver");    }
            }
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {   System.err.println("socket was not closed [main]:Pop3PostReceiver");   }
            }
        }
    }
}
