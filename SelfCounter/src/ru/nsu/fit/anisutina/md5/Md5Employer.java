package ru.nsu.fit.anisutina.md5;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * User: Diana
 * Date: 04.05.13
 * Time: 1:28
 * To change this template use File | Settings | File Templates.
 */
public class Md5Employer implements Callable {
    private Socket socket = null;
    private String cryptedSecretString = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private Integer start = null;
    private Integer lengthInterval = 20;

    private Md5Employer() {};
    public Md5Employer(Socket skt, String expectedResult, Integer strt, Integer lengthInterval) {
        socket = skt;
        cryptedSecretString = expectedResult;
        start = strt;
    }
    @Override
    public Integer call() throws Exception {
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        byte[] result = new byte[1024];
        Integer current = start;
        Integer successive_attempt = null;
        do {
            outputStream.write(current.byteValue());
            int num = inputStream.read(result);
            StringBuffer str_buffer = new StringBuffer();
            for (int i = 0; i < num; i++) {
                str_buffer.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }
            System.out.println(str_buffer);
            if(cryptedSecretString.equals(str_buffer.toString())) {
                System.out.println("YAHOO: " + current.toString());
                successive_attempt = current;
            }
            current++;
            result = new byte[1024];
        } while (!current.equals(start + lengthInterval + 1));
        inputStream.close();
        outputStream.close();
        socket.close();
        return successive_attempt;
    }
}
