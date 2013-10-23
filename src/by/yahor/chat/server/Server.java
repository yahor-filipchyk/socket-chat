
package by.yahor.chat.server;

import by.yahor.chat.client.ClientLocation;
import by.yahor.chat.utils.AddressResolverUtils;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Yahor
 */
public class Server extends Thread {
    
    private static final int MAX_ATTEMPTS = 10;
    private int tryingsToConnect = 0;
    private boolean stoped = true;
    private int port;
    private ServerSocket servSocket;
    private static Map<String, ClientLocation> users = new HashMap<>();
    private List<ServerThread> serverThreads;
    
    public Server(int port) throws IOException {
        this.port = port;
        this.stoped = false;
        serverThreads = new ArrayList<>();
        servSocket = new ServerSocket(port);
        // address of server
        System.out.println(AddressResolverUtils.getLocalAddress());  
    }
    
    synchronized public static boolean userExists(String username) {
        if (users.containsKey(username)) {
            return true;
        }
        return false;
    }
    
    synchronized public static void addUser(String username, ClientLocation clientLocation) {
        users.put(username, clientLocation);
    }
    
    synchronized public static ClientLocation getUser(String username) {
        return users.get(username);
    }
    
    synchronized public static void deleteUser(String username) {
        users.remove(username);
    }
    
    public void run() {
        while (!stoped) {
            acceptRequests();
        }
    }
    
    public void acceptRequests() {
        try {
            Socket userSocket = servSocket.accept();
            ServerThread serverThread = new ServerThread(userSocket);
            serverThread.setDaemon(true);
            serverThreads.add(serverThread);
            serverThread.start();
        } catch (IOException ex) {
            if (servSocket != null) {
                try {
                    servSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                servSocket = new ServerSocket(port);
                tryingsToConnect++;
                if (tryingsToConnect > MAX_ATTEMPTS) {
                    stopServer();
                }
            } catch (IOException ne) {
                ex.printStackTrace();
            }
            ex.printStackTrace();
        }
    }
    
    public void stopServer() {
        for (ServerThread serverThread : serverThreads) {
            serverThread.stopServerThread();
        }
        serverThreads.clear();
        users.clear();
        stoped = true;
        try {
            servSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
