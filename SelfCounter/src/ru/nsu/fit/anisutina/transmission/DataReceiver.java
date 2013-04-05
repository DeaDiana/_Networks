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
    private static final int LENGTH = 100;
    private static final int NUMMILISEC = 1000;
    private static final String default_filename = "defaultname.txt";
    Socket socket = null;
    //private InputStreamReader inputStream = null;
    private InputStream inputStream = null;
    private FileOutputStream fileOutputStream = null;

    private DataReceiver(){};
    public DataReceiver(Socket skt){
        socket = skt;
    }
    @Override
    public void run() {
        try {
            inputStream = /*new InputStreamReader(*/socket.getInputStream();//);

            byte [] message_buf = new byte[LENGTH];
            int iter = 1;
            inputStream.read(message_buf);
            File file = new File(new String(message_buf));
            if(!file.exists()){
                fileOutputStream = new FileOutputStream(file);
                int num = inputStream.read(message_buf);
                while (LENGTH == num) {
                    fileOutputStream.write(message_buf);
                    num = inputStream.read(message_buf);
                    iter++;
                }
                fileOutputStream.write(message_buf);
            } else {
                //ask to rewrite y/n
                //y -> rewrite
                //n -> no
            }
        } catch (FileNotFoundException e) {
            System.err.println("file not found [main]:DataReceiver");
        } catch (IOException e) {
            System.err.println("failed on getting input stream from the socket [main]:DataReceiver");
        }

        finally {
            try {
                if(inputStream != null)     {   inputStream.close();    }
            } catch (IOException e) {   System.err.println("input stream was not closed [main]:DataReceiver");    }
            finally {
                try {
                    if(fileOutputStream != null)     {    fileOutputStream.close();    }
                } catch (IOException e) {   System.err.println("file output stream was not closed [main]:DataReceiver");   }
                finally {
                    try {
                        if(socket != null)     {   socket.close();   }
                    } catch (IOException e) {   System.err.println("socket was not closed [main]:DataReceiver");   }
                }
            }
        }
    }
}
