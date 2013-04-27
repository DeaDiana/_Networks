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
    private static Integer PORT = 587;
    private static BufferedReader inputStream = null;
    private static OutputStream outputStream = null;
    private static BufferedReader inReader = null;
    private static String name = null;
    private static String mail_address = null;
    private static BASE64Encoder encoder = new BASE64Encoder();
    private static final String SUCCESS_CODE = "250";
    private static final String ERROR_CODE = "5";
    private static final String CONNECTED_CODE = "220";
    private static final String SUCCESS_AUTH_CODE = "235";
    private static final String AUTH_RESP_CODE = "334";
    private static final String DATA_RESP_CODE = "354";


    public static void main(String args[]) {
        if(args.length > 0) {
            smtpServer = args[0];
            if(args.length > 1) {
                PORT = new Integer(args[1]);
            }
        }
        try {
            socket = new Socket(smtpServer, PORT);
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = socket.getOutputStream();
            String response = inputStream.readLine();
            if(!response.startsWith(CONNECTED_CODE)) {
                System.out.println("Did not connect to server");
                return;
            }
            inReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter your name:");
            name = inReader.readLine();

            String COMMAND = "HELO " + name + "\r\n";
            outputStream.write(COMMAND.getBytes());
            response = inputStream.readLine();
            if(!response.startsWith(SUCCESS_CODE)) {
                System.out.println("Got error from server: " + response);
                return;
            }
            response = "";
            while(!response.startsWith(SUCCESS_AUTH_CODE)) {
                COMMAND = "AUTH LOGIN\r\n";
                outputStream.write(COMMAND.getBytes());
                response = inputStream.readLine();
                if(response.startsWith(AUTH_RESP_CODE)) {
                    System.out.println("Enter your login:");
                    String login =  encoder.encode(inReader.readLine().getBytes());
                    System.out.println("Enter your password:");
                    String password = encoder.encode(inReader.readLine().getBytes());

                    COMMAND = login + "\r\n";
                    outputStream.write(COMMAND.getBytes());
                    response = inputStream.readLine();
                    if(response.startsWith(AUTH_RESP_CODE)) {
                        COMMAND = password + "\r\n";
                        outputStream.write(COMMAND.getBytes());
                        response = inputStream.readLine();
                    }
                }
            }

            do {
                while (!(response.startsWith(SUCCESS_CODE) || response.startsWith(ERROR_CODE))) {
                    System.out.println("Enter your mail address:");
                    mail_address = inReader.readLine();
                    COMMAND = "MAIL FROM: " + mail_address + "\r\n";
                    outputStream.write(COMMAND.getBytes());
                    response = inputStream.readLine();
                }
                response = "response";
                while (!(response.startsWith(SUCCESS_CODE) || response.startsWith(ERROR_CODE))) {
                    System.out.println("Enter mail address to send letter to:");
                    mail_address = inReader.readLine();
                    COMMAND = "RCPT TO: " + mail_address + "\r\n";
                    outputStream.write(COMMAND.getBytes());
                    response = inputStream.readLine();
                }
                COMMAND = "DATA\r\n";
                outputStream.write(COMMAND.getBytes());
                response = inputStream.readLine();
                if(response.startsWith(DATA_RESP_CODE)) {
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
                    if(response.startsWith(SUCCESS_CODE)) {
                        System.out.println("Your letter has been sent to " + mail_address + " successfully!");
                    } else {
                        System.out.println("Your letter to " + mail_address + "was not sent.");
                    }
                }
                response = "response";
                System.out.println("Send one more letter?");
                System.out.println("[Type 'quit' to quit.]");
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
