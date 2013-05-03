package ru.nsu.fit.anisutina.translation;

import java.lang.String;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: Diana
 * Date: 30.04.13
 * Time: 0:04
 * To change this template use File | Settings | File Templates.
 */
public class TranslatorServer {
    private static Vector<Integer> portListListenTo = null;
    private static ExecutorService executor = null;

    public static void main(String args[]){
        portListListenTo = new Vector<Integer>();
        portListListenTo.add(7001);
        portListListenTo.add(7002);
        portListListenTo.add(7003);
        try {
            executor = Executors.newFixedThreadPool(portListListenTo.size());
            for(int i = 0; i < portListListenTo.size(); i++) {
                Runnable translation = new DataReceiver(portListListenTo.elementAt(i));
                executor.execute(translation);
            }
            while(true) {
                //server is working
            }
        }
        finally {
            executor.shutdown();
            while (!executor.isTerminated()){}
        }
    }
}
