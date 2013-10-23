package by.yahor.chat.server;

import by.yahor.chat.client.ClientLocation;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import my.yahorfilipchyk.console.Console;

/**
 *
 * @author Yahor
 */
public class ServerThread extends Thread {

    private NetServer server;
    private boolean disconnected = true;

    public ServerThread(Socket serverSocket) {
        try {
            this.server = new NetServer(serverSocket);
            disconnected = false;
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    public Socket getClientSocket() {
        return server.getSocket();
    }
    
    public void stopServerThread() {
        disconnected = true;
        try {
            server.getSocket().close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        int command = NetServer.NO_COMMAND;
        String username = "";
        while (NetServer.DISCONNECT != (command = server.resieveCommand()) && !disconnected) {
            System.out.println(command);
            switch (command) {
                case NetServer.REGISTER:
                    username = server.resieveMessage();
                    if (Server.userExists(username)) {
                        server.sendCommand(NetServer.USER_EXISTS);
                    } else {
                        register(username);
                        server.sendCommand(NetServer.REGISTRATION_OK);
                        System.out.println(username + " is online");
                    }
                    break;
                case NetServer.GET_ADDRESS:
                    String peerName = server.resieveMessage();
                    if (Server.userExists(peerName)) {
                        server.sendCommand(NetServer.PEER_EXISTS);
                        // sending ip-address;
                        ClientLocation clientLocation = Server.getUser(peerName);
                        int[] ipv4 = clientLocation.getIpv4();
                        server.sendCommand(ipv4[0]);
                        server.sendCommand(ipv4[1]);
                        server.sendCommand(ipv4[2]);
                        server.sendCommand(ipv4[3]);
                        // sending port
                        int port = clientLocation.getPort();
                        server.sendCommand(port);
                    } else {
                        server.sendCommand(NetServer.NO_PEER);
                    }
                    break;
            }
        }
        unregister(username);
    }
    
    private void unregister(String username) {
        Server.deleteUser(username);
    }
    
    private void register(String username) {
        int[] ipv4 = new int[4];
        ipv4[0] = server.resieveCommand();
        ipv4[1] = server.resieveCommand();
        ipv4[2] = server.resieveCommand();
        ipv4[3] = server.resieveCommand();
        int port = server.resieveCommand();
        Console.writeLine(username + " port: " + port);
        Server.addUser(username, new ClientLocation(ipv4, port));
    }
}
