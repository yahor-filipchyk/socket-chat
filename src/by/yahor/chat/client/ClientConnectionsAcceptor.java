/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.yahor.chat.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 *
 * @author Yahor
 */
public class ClientConnectionsAcceptor extends Thread {
    
    private int port;
    private ServerSocket connectionAcceptor;
    
    public ClientConnectionsAcceptor() {
        bindAcceptor();
    }
    
    @Override
    public void run() {
        try {
            Socket client = connectionAcceptor.accept();
        } catch (IOException ex) {
            ex.printStackTrace();;
        }
    }
    
    private void bindAcceptor() {
        boolean binded = false;
        while (!binded) {
            try {
                Random portGenerator = new Random(System.currentTimeMillis());
                port = portGenerator.nextInt(65535 - 1024) + 1024;
                connectionAcceptor = new ServerSocket(port);
                if (connectionAcceptor.isBound()) {
                    binded = true;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
