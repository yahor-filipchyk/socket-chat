
package socketstest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yahor
 */
public class ServerTest {
    
    private static Map<String, InetAddress> users = new HashMap<>();
    
    public static boolean register(ResourceBundle usersList, BufferedReader reader) {
        try {
            String username = reader.readLine();
            if (!usersList.containsKey(username)) {
//                usersList.
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static void main(String args[]) {
        ServerSocket servSocket = null;
        Socket socket = null;
        try {
//            ResourceBundle usersList = ResourceBundle.getBundle("users");
            servSocket = new ServerSocket(2000);
            socket = servSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Writer writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String message = null;
            while (!"disconnect".equals(message = reader.readLine())) {
                switch(message) {
                    case "register":
                        String username = reader.readLine();
                        if (users.containsKey(username)) {
                            writer.write("exists\n");
                            writer.flush();
                        } else {
                            users.put(username, socket.getInetAddress());
                            writer.write("ok\n");
                            writer.flush();
                        }
                }
            }
            
            System.out.println(socket);
            writer.write("Hi! \n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            if (servSocket != null) {
                try {
                    servSocket.close();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        }
    }
}
