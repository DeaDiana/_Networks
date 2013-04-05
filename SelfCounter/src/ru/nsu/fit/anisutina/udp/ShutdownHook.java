package ru.nsu.fit.anisutina.udp;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created with IntelliJ IDEA.
 * User: Diana
 * Date: 18.02.13
 * Time: 12:08
 * To change this template use File | Settings | File Templates.
 */
public class ShutdownHook extends Thread {
    public void run(){

        try {
            MulticastSocket socket = new MulticastSocket(1025);
            InetAddress group = InetAddress.getByName("230.0.0.1"); //multicast for 230.0.0.1
            socket.joinGroup(group);

            byte[] message_idied = new String("IDIED").getBytes();
            DatagramPacket packet_about_dying = new DatagramPacket(message_idied, message_idied.length, group, 1025);
            socket.send(packet_about_dying);
        }catch (Exception e){
            System.err.println("Didn't sent message about dying");
        }
    }
}
