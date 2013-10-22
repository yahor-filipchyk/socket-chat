package by.yahor.chat.client;

import by.yahor.chat.server.NetPeer;
import by.yahor.chat.server.NetServer;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import my.yahorfilipchyk.console.Console;

/**
 *
 * @author Yahor
 */
public class Client {

    public static void main(String args[]) {
        NetServer server = null;
        byte[] address = new byte[4];
        int port = 0;
        try {
            server = new NetServer(new Socket("localhost", 2013));
            String username = "";
            boolean registered = false;
            while (!registered) {
                server.sendCommand(NetServer.REGISTER);
                username = Console.read("Username: ");
                server.sendMessage(username);
                int response = server.resieveCommand();
                switch (response) {
                    case NetServer.REGISTRATION_OK:
                        System.out.println("Registered.");
                        registered = true;
                        break;
                    case NetServer.USER_EXISTS:
                        System.out.println("User exists.");
                    default:
                        break;
                }
            }
            boolean gotAddress = false;
            while (!gotAddress) {
                server.sendCommand(NetServer.GET_ADDRESS);
                username = Console.read("User to connect: ");
                server.sendMessage(username);
                int response = server.resieveCommand();
                switch (response) {
                    case NetServer.PEER_EXISTS:
                        System.out.println("Getting address...");
                        System.out.print((address[0] = (byte) server.resieveCommand()) + ".");
                        System.out.print((address[1] = (byte) server.resieveCommand()) + ".");
                        System.out.print((address[2] = (byte) server.resieveCommand()) + ".");
                        System.out.print((address[3] = (byte) server.resieveCommand()) + "\n");
                        System.out.println("port: " + (port = server.resieveCommand()));
                        gotAddress = true;
                        break;
                    case NetServer.NO_PEER:
                        System.out.println("No user with such name");
                        break;
                    default:
                        break;
                }
            }
            ServerSocket listenerSocket = new ServerSocket(port);
            NetPeer peer = new NetPeer(new Socket(InetAddress.getByAddress(address), port));
            System.out.println(peer.getSocket());
            server.sendCommand(NetServer.DISCONNECT);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
