
package by.yahor.chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;

/**
 *
 * @author Yahor
 */
public class NetServer extends NetPeer {
    
    /**
     * Server-user commands
     */
    public static final int NO_COMMAND = 0;
    public static final int DISCONNECT = 1;
    public static final int REGISTER = 2;
    public static final int USER_EXISTS = 3;
    public static final int REGISTRATION_OK = 4;
    public static final int GET_ADDRESS = 5;
    public static final int PEER_EXISTS = 6;
    public static final int NO_PEER = 7;
    
    DataInputStream commandReader;
    DataOutputStream commandWriter;
//    Reader reader;
    
    public NetServer(Socket socket) throws IOException {
        super(socket);
        commandReader = new DataInputStream(getSocket().getInputStream());
        commandWriter = new DataOutputStream(socket.getOutputStream());
    }
    
    public int resieveCommand() {
        int message = 0;
        try {
            message = commandReader.readInt();
        } catch (IOException ex) {
            System.out.println("Error resieving command: " + ex);
            return DISCONNECT;
        }
        return message;
    }
    
    public void sendCommand(int command) {
        try {
            commandWriter.writeInt(command);
            commandWriter.flush();
        } catch (IOException ex) {
            System.out.println("Error sending command: " + ex);
        }
    }
}
