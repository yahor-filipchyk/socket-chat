
package by.yahor.chat.server;

import my.yahorfilipchyk.console.Console;

/**
 *
 * @author Yahor
 */
public class ServerStarter {
    
    public static void main(String args[]) {
        Server server = new Server(2013);
        server.setDaemon(true);
        server.start();
        String command = "";
        while (!"stop".equals(command = Console.readLine())) {
            
        }
        server.stopServer();
    }
}
