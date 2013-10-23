package by.yahor.chat.client;

import by.yahor.chat.server.NetPeer;
import by.yahor.chat.server.NetServer;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import my.yahorfilipchyk.console.Console;

/**
 *
 * @author Yahor
 */
public class Client {

    private NetServer server;
    private ClientConnectionsAcceptor localServer;
    private String serverHost;
    private int serverPort;
    
    public Client(String serverHost, int serverPort) throws IOException {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        connectToServer();
    }
    
    public void addConversationListener(ConversationListener listener) {
        localServer.addConversationListener(listener);
    }
    
    public boolean register(String username) {
        boolean registered = false;
        localServer = new ClientConnectionsAcceptor();
        server.sendCommand(NetServer.REGISTER);
        server.sendMessage(username);
        // seding client address to the server
        byte[] ipv4 = localServer.getAddress();
        Console.write(((int) ipv4[0] < 0 ? 256 + ipv4[0] : ipv4[0]) + "." + 
                ((int) ipv4[1] < 0 ? 256 + ipv4[1] : ipv4[1]) + "." + 
                ((int) ipv4[2] < 0 ? 256 + ipv4[2] : ipv4[2]) + "." + 
                ((int) ipv4[3] < 0 ? 256 + ipv4[3] : ipv4[3]) + "\n");
        server.sendCommand(ipv4[0] < 0 ? 256 + ipv4[0] : ipv4[0]);
        server.sendCommand(ipv4[1] < 0 ? 256 + ipv4[1] : ipv4[1]);
        server.sendCommand(ipv4[2] < 0 ? 256 + ipv4[2] : ipv4[2]);
        server.sendCommand(ipv4[3] < 0 ? 256 + ipv4[3] : ipv4[3]);
        // sending client port to the server
        server.sendCommand(localServer.getPort());
        int serverResponse = server.resieveCommand();
        switch (serverResponse) {
            case NetServer.REGISTRATION_OK:
                registered = true;
                localServer.start();
                break;
            case NetServer.USER_EXISTS:
                registered = false;
                localServer.close();
                break;
            default:
                break;
        }
        return registered;
    }
    
    public Conversation startConversation(String username) {
        Conversation conversation = null;
        int[] address = new int[4];
        int port = 0;
        server.sendCommand(NetServer.GET_ADDRESS);
        server.sendMessage(username);
        int response = server.resieveCommand();
        switch (response) {
            case NetServer.PEER_EXISTS:
                System.out.println("Getting address...");
                System.out.print((address[0] = server.resieveCommand()) + ".");
                System.out.print((address[1] = server.resieveCommand()) + ".");
                System.out.print((address[2] = server.resieveCommand()) + ".");
                System.out.print((address[3] = server.resieveCommand()) + "\n");
                System.out.println("port: " + (port = server.resieveCommand()));
                try {
                    Socket socket = new Socket(InetAddress.getByName(address[0] + "." + address[1] + "." + address[2] + "." + address[3]), port);
                    if (socket != null && socket.isConnected() && socket.isBound()) {
                        NetPeer peer = new NetPeer(socket);
                        conversation = new Conversation(peer);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case NetServer.NO_PEER:
                System.out.println("No user with such name");
                break;
            default:
                break;
        }
        return conversation;
    }
    
    public void disconnectFromServer() {
        server.sendCommand(NetServer.DISCONNECT);
    }
    
    private void connectToServer() throws IOException {
        InetAddress serverAddress = null;
        try {
            serverAddress = InetAddress.getByName(serverHost);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
        Socket socket = new Socket(serverAddress, serverPort);
        server = new NetServer(socket);
    }
    
    public static void main(String args[]) {
        Client client = null;      
        String command = "";
        boolean connected = false;
        while (!connected) {
//            String host = Console.read("Sever host: ");
//            String port = Console.read("Server port: ");
            String host = "localhost";
            String port = "2013";
            try {
                client = new Client(host, Integer.parseInt(port));
                connected = true;
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
        boolean registered = false;
        
        while (!registered) {
            String userName = Console.read("Username: ");
            registered = client.register(userName);
        }
        
        client.addConversationListener(new ConversationListener() {

            @Override
            public void conversationCreated(final Conversation conversation) {    
                Console.writeLine("Conversation created");
                conversation.start();
                conversation.addMessageListener(new MessageListener() {

                    @Override
                    public void messageRecieved(String message) {
                        Console.writeLine(message);
                    }
                });                
                conversation.sendMessage("Hi!");
            }
        }); 
        
        Conversation conv = client.startConversation(Console.readLine());        
        conv.start();
        conv.addMessageListener(new MessageListener() {
            @Override
            public void messageRecieved(String message) {
                Console.writeLine(message);
            }
        });
        conv.sendMessage("Another Hi!");
        
        client.disconnectFromServer();
        Console.readLine();
        
        conv.close();
    }
}
