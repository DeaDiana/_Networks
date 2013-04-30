package ru.nsu.fit.anisutina.translation;

import java.io.*;
import java.lang.String;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: Diana
 * Date: 30.04.13
 * Time: 0:03
 * To change this template use File | Settings | File Templates.
 */
public class Translator {
    private static String translationRules = "translationRules.txt";
    private static ExecutorService executor = null;

    public static void main(String args[]){
        if(args.length > 0) {
            translationRules = args[0];
        }
        try {
            Scanner rulesStream = new Scanner(new File(translationRules));
            executor = Executors.newFixedThreadPool(rulesStream.nextInt());
            while(rulesStream.hasNext()) {
                Integer PORTlistenTo = new Integer(rulesStream.next());
                Integer serverPORT = new Integer(rulesStream.nextInt());
                String str = rulesStream.next();
                InetAddress serverIP = InetAddress.getByName(str);
                Runnable translation = new DataTranslation(PORTlistenTo, serverIP, serverPORT);
                executor.execute(translation);
            }
            while(true) {
                //translator is working
            }
        } catch (FileNotFoundException e) {
            System.err.println("Did not get file with translation rules");
        } catch (UnknownHostException e) {
            System.out.println("InetAddress was not parsed");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally {
            executor.shutdown();
            while (!executor.isTerminated()){}
        }
        System.out.println("Done.");
    }
}
