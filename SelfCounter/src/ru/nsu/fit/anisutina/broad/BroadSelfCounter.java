package ru.nsu.fit.anisutina.broad;

import java.io.IOException;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: Diana
 * Date: 02.03.13
 * Time: 23:00
 * To change this template use File | Settings | File Templates.
 */
public class BroadSelfCounter {

    private static final int PORTfrom = 5345;
    private static final int PORTto = 4446;
    private static final String broad_str = new String("192.168.43.255");//10.3.71.255");
    private static InetAddress broad_addr = null;// 10.4.0.31 - broadcast address
    private static final int TIMEINTERVAL = 2 * 1000;
    private static final String ilive_message = new String("ILIVE");
    private static ListenCount listenCount = null;
    private static Timer timer = null;
    private static DatagramSocket socket = null;

    public static void main(String[] args) {
        try {
            broad_addr = InetAddress.getByName(broad_str);
        } catch (UnknownHostException e) {
            System.err.println("broad address was not gotten:" + broad_str);
        }
        listenCount = new ListenCount(PORTto);
        listenCount.start();

        try {
            socket = new DatagramSocket(PORTfrom);
        } catch (SocketException e) {
            System.err.println("[MAIN THREAD]socket was not created PORT:" + PORTfrom + "broad address" + broad_addr);
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                byte[] message_response = ilive_message.getBytes();
                DatagramPacket packet_about_living = new DatagramPacket(message_response, message_response.length, broad_addr, PORTto);
                try {
                    socket.send(packet_about_living);
                } catch (IOException e) {
                    System.err.println("socket didn't send package about living" + this.getClass());
                }
            }
        }, 0, TIMEINTERVAL);
    }
}
