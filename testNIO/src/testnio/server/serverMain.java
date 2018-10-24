/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testnio.server;

import java.net.ServerSocket;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.net.InetSocketAddress;

/**
 *
 * @author sanjay
 */
public class serverMain {
    public static final int PORT_NUMBER = 10001;
    private static int numConnections = 0;
    private static HashMap<Integer, serverThread> connections;
    private static boolean running = true;
    
    public static void clientDisconnected(int number) {
        connections.remove(number);
        numConnections--;
        if (numConnections > 0) {
            int n = (number + 1) % 2;  // get other thread
            System.out.println("Other thread = " + n);
            serverThread th = connections.get(n);
            th.stopThread();
        }
        running = false;
    }
    public static void runMainLoop() {
        connections = new HashMap<Integer, serverThread>();
        
        System.out.println("SocketServer Example");
        ServerSocketChannel ssc = null;
        try {
            ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            // Get the Socket connected to this channel, and bind it to the
            // listening port
            ServerSocket ss = ssc.socket();
            
//            InetAddress addr = InetAddress.getLocalHost();
//            System.out.println(addr);
//            InetSocketAddress sockaddr = new InetSocketAddress(addr, PORT_NUMBER);
//            System.out.println(sockaddr);
//            ss.bind(sockaddr);

            InetSocketAddress isa = new InetSocketAddress( PORT_NUMBER );
            System.out.println(isa);
            ss.bind(isa);
            
            Selector selector= Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            while (running) {
                int num = selector.select(1000);  //  1 second
                if (num == 0) {
//                    System.out.println("1 sec timeout");
                    continue;
                }
//                System.out.println("Select poped");
                Set keys = selector.selectedKeys();
                Iterator it = keys.iterator();
                while (it.hasNext()) {
                    // Get a key representing one of bits of I/O
                    // activity.
                    SelectionKey key = (SelectionKey)it.next();
                    if ((key.readyOps() & SelectionKey.OP_ACCEPT) ==
                        SelectionKey.OP_ACCEPT) {
                        // Accept the incoming connection.
                        SocketChannel s = ssc.accept();
                        serverThread th = new serverThread(s, numConnections);
                        connections.put(numConnections++, th);
                        System.out.println("Num connections = " + Integer.toString(numConnections));
                    }
                }
                keys.clear();
                Thread.sleep(1000);                
            }
        } catch (IOException ex) {
            System.out.println("Unable to start server.");
        }
        catch (InterruptedException e) {
            System.out.println(e);
        }
        finally {
            try {
            if (ssc != null)
               ssc.close();
            }
            catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}