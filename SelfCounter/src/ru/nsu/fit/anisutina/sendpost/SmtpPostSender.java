package ru.nsu.fit.anisutina.sendpost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import sun.misc.BASE64Encoder;

/**
 * Created with IntelliJ IDEA.
 * User: Diana
 * Date: 26.04.13
 * Time: 22:37
 * To change this template use File | Settings | File Templates.
 */
public class SmtpPostSender {
    private static String smtpServer = "smtp.mail.ru";
    private static Socket socket = null;
    private final static int PORT = 25;
    private static BufferedReader inputStream = null;
    private static OutputStream outputStream = null;
    private static BufferedReader inReader = null;
    private static String IP = null;
    private static String mail_address = null;
    private static BASE64Encoder encoder = new BASE64Encoder();

    public static void main(String args[]) {
        if(args.length > 0) {
            smtpServer = args[0];
        }
        try {
            socket = new Socket(smtpServer, PORT);
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = socket.getOutputStream();
            String response = inputStream.readLine();
            System.out.println(response);

            inReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter your IP:");
            IP = inReader.readLine();

            //String COMMAND = "EHLO " + IP + "\r\n";
            String COMMAND = "EHLO " + "192.168.1.1" + "\r\n";
            outputStream.write(COMMAND.getBytes());

            int num = 0;
            //do {
              //  num = inputStream.read();
            //} while(-1 != num);
            response = inputStream.readLine();
            response = inputStream.readLine();
            response = inputStream.readLine();
            response = inputStream.readLine();
            response = inputStream.readLine();
            System.out.println(response);
            response = "";
            while(!response.contains("235")) {
                COMMAND = "AUTH LOGIN\r\n";
                outputStream.write(COMMAND.getBytes());
                response = inputStream.readLine();
                System.out.println(response);
                if(response.contains("334")) {
                    System.out.println("Enter your login:");
                    String login =  encoder.encode(inReader.readLine().getBytes());
                    System.out.println("Enter your password:");
                    String password = encoder.encode(inReader.readLine().getBytes());

                    COMMAND = login + "\r\n";
                    outputStream.write(COMMAND.getBytes());
                    response = inputStream.readLine();
                    System.out.println(response);
                    if(response.contains("334")) {
                        COMMAND = password + "\r\n";
                        outputStream.write(COMMAND.getBytes());
                        response = inputStream.readLine();
                        System.out.println(response);
                    }
                }
            }

            do {
                while (!(response.contains("250") || '5' == response.charAt(0))) {
                    System.out.println("Enter your mail address:");
                    mail_address = inReader.readLine();
                    //COMMAND = "MAIL FROM: " + mail_address + "\r\n";
                    COMMAND = "MAIL FROM: " + "freilina_07@mail.ru" + "\r\n";
                    outputStream.write(COMMAND.getBytes());
                    response = inputStream.readLine();
                    System.out.println(response);
                }
                response = "response";
                while (!(response.contains("250") || '5' == response.charAt(0))) {
                    System.out.println("Enter mail address to send letter to:");
                    mail_address = inReader.readLine();
                    //COMMAND = "RCPT TO: " + mail_address + "\r\n";
                    COMMAND = "RCPT TO: " + "freilina_07@mail.ru" + "\r\n";
                    outputStream.write(COMMAND.getBytes());
                    response = inputStream.readLine();
                    System.out.println(response);
                }
                COMMAND = "DATA\r\n";
                outputStream.write(COMMAND.getBytes());
                response = inputStream.readLine();
                System.out.println(response);
                if(response.contains("354")) {
                    System.out.println("Enter your message:");
                    String part_message = "message";
                    String message = "";
                    while(!part_message.isEmpty()) {
                        part_message = inReader.readLine();
                        message = message + part_message;
                    }
                    COMMAND = message + "\r\n.\r\n";
                    outputStream.write(COMMAND.getBytes());
                    response = inputStream.readLine();
                    System.out.println(response);
                    if(response.contains("250")) {
                        System.out.println("Your letter has been sent to " + mail_address + "successfully!");
                    } else {
                        System.out.println("Your letter to " + mail_address + "was not sent.");
                    }
                }
                response = "response";
                System.out.println("Send one more letter?");
                System.out.println("Type 'quit' to quit.");
            } while (!inReader.readLine().contains("quit"));
        } catch (IOException e) {
            System.err.println("Socket was not created or streams were not extracted [main]:SmtpPostSender");
        }
        finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {   System.err.println("input socket stream was not closed [main]:SmtpPostSender");    }
            }
            if(outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {   System.err.println("output socket stream was not closed [main]:SmtpPostSender");    }
            }
            if(inReader != null) {
                try {
                    inReader.close();
                } catch (IOException e) {   System.err.println("input stream was not closed [main]:SmtpPostSender");    }
            }
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {   System.err.println("socket was not closed [main]:SmtpPostSender");   }
            }
        }
    }
}
