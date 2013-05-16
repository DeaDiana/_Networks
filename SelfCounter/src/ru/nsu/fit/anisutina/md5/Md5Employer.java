package ru.nsu.fit.anisutina.md5;

import java.io.*;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: Diana
 * Date: 04.05.13
 * Time: 1:28
 * To change this template use File | Settings | File Templates.
 */
public class Md5Employer implements Runnable {
    private Socket socket = null;
    private BufferedReader inputStream = null;
    private PrintWriter outputStream = null;
    private Integer start = null;
    private Integer end = 20;
    private String crypted_string = null;

    private Md5Employer() {};
    public Md5Employer(Socket skt, String crpt_str, Integer strt, Integer en) {
        socket = skt;
        crypted_string = crpt_str;
        start = strt;
        end = en;
    }

    @Override
    public void run() {
        try {
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new PrintWriter(socket.getOutputStream(),true);
            String command = inputStream.readLine();
            if(command.equals("GET_WORK")) {
                String build_task = new String(start.toString() + " " + end.toString() + " " + crypted_string);
                outputStream.println(build_task);
            }
            if(command.equals("LISTEN_RESULT")) {
                String original_string = inputStream.readLine();
                System.out.println("original string is:");
                System.out.println(original_string);
            }
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("input or output stream was not gotten from socket [run]:Md5Employer");
        }
        finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {   System.err.println("input stream was not closed [run]:Md5Employer");    }
            }
            if(outputStream != null) {
                //try {
                    outputStream.close();
                //} catch (IOException e) {   System.err.println("output stream was not closed [run]:Md5Employer");    }
            }
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {   System.err.println("socket was not closed [run]:Md5Employer");    }
            }
        }
    }
}
