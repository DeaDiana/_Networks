package ru.nsu.fit.anisutina.broad;

import java.io.IOException;
import java.net.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Diana
 * Date: 16.03.13
 * Time: 15:23
 * To change this template use File | Settings | File Templates.
 */
public class ListenCount extends Thread {
    private static int PORT = 4446;
    private static DatagramSocket socket = null;
    private static final int TIMEINTERVAL = 10 * 1000;
    private static final String ilive_message = new String("ILIVE");
    private static Set<String> listOfMachines = Collections.synchronizedSet(new HashSet<String>());
    private static Timer timer = null;

    private ListenCount(){};

    public ListenCount(int prt_to_listen){
        PORT = prt_to_listen;
        try {
            socket = new DatagramSocket(PORT);
        }
        catch (SocketException e) {
            System.err.println("socket was not created PORT: " + PORT);
        }
    }

    @Override
    public void run() {

        DatagramPacket rcv_packet;
        String receive_message;
        Iterator iterator;
        byte[] get_message = new byte[100];

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                listOfMachines.clear();
                System.out.println("cleared");
            }
        }, 0, TIMEINTERVAL);

        for(int i = 0; i < TIMEINTERVAL; i++){
            rcv_packet = new DatagramPacket(get_message, get_message.length);
            try {
                socket.receive(rcv_packet);
            } catch (IOException e) {
                System.err.println("exception: trying receive package");
            }
            receive_message = new String(rcv_packet.getData(),rcv_packet.getOffset(),rcv_packet.getLength());

            if(receive_message.equals(ilive_message)){
                listOfMachines.add(rcv_packet.getAddress().toString());
            }
            System.out.println(listOfMachines.size());
            iterator = listOfMachines.iterator();
            while(iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        }

        socket.close();
    }
}
