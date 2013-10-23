
package by.yahor.chat.server;

import java.io.IOException;
import my.yahorfilipchyk.console.Console;

/**
 *
 * @author Yahor
 */
public class ServerStarter {
    
    public static void main(String args[]) {
        if (args.length != 1) {
            return;
        }
        int port = 0;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            Console.writeLine("Illegal port number");
            return;
        }
        Server server = null;
        try {
            server = new Server(port);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        server.setDaemon(true);
        server.start();
        String command = "";
        while (!"stop".equals(command = Console.readLine())) {            
        }
        server.stopServer();
    }
}
