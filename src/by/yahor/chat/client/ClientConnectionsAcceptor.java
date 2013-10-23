/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.yahor.chat.client;

import by.yahor.chat.server.NetPeer;
import by.yahor.chat.utils.AddressResolverUtils;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import my.yahorfilipchyk.console.Console;

/**
 *
 * @author Yahor
 */
public class ClientConnectionsAcceptor extends Thread {
    
    private int port;
    private boolean closed = true;
    private ServerSocket connectionAcceptor;
    private List<ConversationListener> listeners = new ArrayList<>();;
    
    public ClientConnectionsAcceptor() {
        bindAcceptor();
    }
    
    @Override
    public void run() {
        while (!closed) {
            if (!connectionAcceptor.isBound()) {
                bindAcceptor();
            }
            try {
                Console.writeLine("Trying to accept connection");
                Socket client = connectionAcceptor.accept();
                Console.writeLine("Connection accepted");
                if (client != null && client.isConnected() && client.isBound()) {
                    NetPeer peer = new NetPeer(client);
                    for (ConversationListener listener : listeners) {
                        Conversation conversation = new Conversation(peer);
                        listener.conversationCreated(conversation);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();;
            }
        }
    }
    
    public byte[] getAddress() {
        InetAddress address = AddressResolverUtils.getLocalAddress();
        if (address != null) {
            return address.getAddress();
        } else {
            try {
                address = InetAddress.getLocalHost();
                return address.getAddress();
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            }
        }
        return connectionAcceptor.getInetAddress().getAddress();
    }
    
    public int getPort() {
        return connectionAcceptor.getLocalPort();
    }
    
    public void addConversationListener(ConversationListener listener) {
        this.listeners.add(listener);
    }
    
    public void close() {
        if (connectionAcceptor != null) {
            try {
                connectionAcceptor.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        closed = true;
    }
    
    private void bindAcceptor() {
        boolean binded = false;
        while (!binded) {
            try {
                Random portGenerator = new Random(System.currentTimeMillis());
                port = portGenerator.nextInt(65535 - 1024) + 1024;
                Console.writeLine("trying port: " + port);
                connectionAcceptor = new ServerSocket(port);
//                byte[] ipv4 = AddressResolverUtils.getLocalAddress().getAddress();
//                for (int i = 0; i < ipv4.length; i++) {
//                    Console.write("" + (byte) ipv4[i] + (i == ipv4.length - 1 ? "\n" : "."));
//                }
                Console.writeLine("isBound: " + connectionAcceptor.isBound());
                if (connectionAcceptor.isBound()) {
                    binded = true;
                    closed = false;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
