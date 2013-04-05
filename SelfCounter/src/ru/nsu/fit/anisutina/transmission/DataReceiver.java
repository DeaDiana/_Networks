package ru.nsu.fit.anisutina.transmission;

import java.io.*;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: Diana
 * Date: 24.03.13
 * Time: 23:07
 * To change this template use File | Settings | File Templates.
 */
public class DataReceiver implements Runnable{
    private static final int LENGTH = 3 * 1024;
    private static final int FILENAME_LEN = 5;
    Socket socket = null;
    private InputStream inputStream = null;
    private FileOutputStream fileOutputStream = null;

    private DataReceiver(){};
    public DataReceiver(Socket skt){
        socket = skt;
    }
    @Override
    public void run() {
        try {
            inputStream = socket.getInputStream();
            byte [] filename_buf = new byte[FILENAME_LEN];
            byte [] message_buf = new byte[LENGTH];
            inputStream.read(filename_buf);
            File file = new File("[TIME][" + Long.toString(System.currentTimeMillis()) + "][THREAD][" + Thread.currentThread().getName() + "]_" + new String(filename_buf));
            if(!file.exists()){
                fileOutputStream = new FileOutputStream(file);
                int num = inputStream.read(message_buf);
                fileOutputStream.write(message_buf);
                while (LENGTH == num) {
                    num = inputStream.read(message_buf);
                    if(num > 0) {   fileOutputStream.write(message_buf, 0, num);    }
                }

            }
        } catch (FileNotFoundException e) {
            System.err.println("file not found [run]:DataReceiver");
        } catch (IOException e) {
            System.err.println("failed on getting input stream from the socket [main]:DataReceiver");
        }

        finally {
            try {
                if(inputStream != null)     {   inputStream.close();    }
            } catch (IOException e) {   System.err.println("input stream was not closed [run]:DataReceiver");    }
            finally {
                try {
                    if(fileOutputStream != null)     {    fileOutputStream.close();    }
                } catch (IOException e) {   System.err.println("file output stream was not closed [run]:DataReceiver");   }
                finally {
                    try {
                        if(socket != null)     {   socket.close();   }
                    } catch (IOException e) {   System.err.println("socket was not closed [run]:DataReceiver");   }
                }
            }
        }
    }
}
