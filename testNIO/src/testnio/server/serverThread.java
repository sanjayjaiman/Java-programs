/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testnio.server;

import java.io.IOException;
import static java.lang.System.in;
import java.nio.channels.SocketChannel;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.util.*;
import java.nio.*;

/**
 *
 * @author sanjay
 */
public class serverThread extends Thread {

    private int connectionNumber = 0;
    private boolean running;
    private Selector selector;
    SocketChannel socket;
    
    public serverThread(SocketChannel socket, int number) {
        this.socket = socket;
        connectionNumber = number;
        try {
            socket.configureBlocking(false);
            selector = Selector.open();
            socket.register(selector, SelectionKey.OP_READ);
//            socket.register(selector, SelectionKey.OP_WRITE);
            System.out.println("New client connected from " + socket.getRemoteAddress());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        running = true;
        start();
    }

    public void send(String str) {
    }
    private int BUFF_SIZE = 100;
    
    public void stopThread() {
        System.out.println("Stopping thread");
        running = false;
    }
    public void run() {
        System.out.println("ServerThread::Running; " + ((running) ? "TRUE" : "FALSE"));
        try {
            String request;
            while (running) {
//                System.out.println("ServerThread::Entering select");
                int num = selector.select(1000);  //  1 second
                if (num == 0) {                    
//                    System.out.println("ServerThread::1 sec timeout");
                    continue;
                }
//                System.out.println("ServerThread::select popped");
                Set keys = selector.selectedKeys();
                Iterator it = keys.iterator();
                while (it.hasNext()) {
                    // Get a key representing one of bits of I/O
                    // activity.
                    SelectionKey key = (SelectionKey)it.next();
                    SocketChannel sc = (SocketChannel)key.channel();
//                    System.out.println("\tHas data");
                    byte[] bytes = new byte [BUFF_SIZE];
                    ByteBuffer bb = ByteBuffer.wrap(bytes);
                    bb.clear();
                    int bytesRead = sc.read(bb);
                    if (bytesRead == -1) {
                        running = false;
                    }
                    else {
                        String str = new String(bb.array(), 0, bytesRead);
                        StringTokenizer st = new StringTokenizer(str);
                        while (st.hasMoreTokens()) {
                            System.out.println("num  bytes = " + bytesRead + "; data = " + st.nextToken());
                        }
                    }
                    if (key.isReadable()) {
//                        System.out.println("Socket readable:");
                    }
                }
                keys.clear();
            }
        } catch (IOException ex) {
            System.out.println("Unable to get streams from client");
        } finally {
            try {
                System.out.println("Client has disconnected " + socket.getRemoteAddress());
                in.close();
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("Exiting thread for connection " + Integer.toString(connectionNumber));
        serverMain.clientDisconnected(connectionNumber);
    }   
}
