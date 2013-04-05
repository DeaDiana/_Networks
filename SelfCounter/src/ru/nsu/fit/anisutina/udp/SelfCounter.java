package ru.nsu.fit.anisutina.udp;

import com.sun.corba.se.impl.orb.ParserTable;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: Diana
 * Date: 18.02.13
 * Time: 10:59
 * To change this template use File | Settings | File Templates.
 */
public class SelfCounter {

    private static final int PORT = 1025;
    private static final int TIMEINTERVAL = 50000000;
    private static final String iborn_message = new String("IBORN");
    private static final String ilive_message = new String("ILIVE");
    private static final String idied_message = new String("IDIED");
    private static HashSet<String> listOfMachines = new HashSet<String>();

    public static void main(String[] args) throws Exception{

        ShutdownHook shutdownHook = new ShutdownHook();
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        MulticastSocket socket = new MulticastSocket(PORT);
        InetAddress group = InetAddress.getByName("230.0.0.1");
        socket.joinGroup(group);

        byte[] message = iborn_message.getBytes();
        DatagramPacket send_packet = new DatagramPacket(message, message.length, group, PORT);
        socket.send(send_packet);
        DatagramPacket rcv_packet;
        String receive_message;
        Iterator iterator;

        for(int i = 0; i < TIMEINTERVAL; i++){
            byte[] get_message = new byte[100];
            rcv_packet = new DatagramPacket(get_message, get_message.length);
            socket.receive(rcv_packet);
            receive_message = new String(rcv_packet.getData(),rcv_packet.getOffset(),rcv_packet.getLength());

            if(receive_message.equals(ilive_message)){
                listOfMachines.add(rcv_packet.getAddress().toString());
                System.out.println("got ILIVE");
            }
            if(receive_message.equals(iborn_message)){
                byte[] message_response = ilive_message.getBytes(); //is it correct to reuse message here ?
                DatagramPacket packet_about_living = new DatagramPacket(message_response, message_response.length, group, PORT);
                socket.send(packet_about_living);
                listOfMachines.add(rcv_packet.getAddress().toString());
                System.out.println("got IBORN");
            }
            if(receive_message.equals(idied_message)){
                listOfMachines.remove(rcv_packet.getAddress().toString());
                System.out.println("got IDIED");
            }
            System.out.println(listOfMachines.size());
            iterator = listOfMachines.iterator();
            while(iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        }

        socket.leaveGroup(group);
        socket.close();

    }
}
