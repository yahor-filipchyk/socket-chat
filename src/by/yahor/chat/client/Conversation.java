
package by.yahor.chat.client;

import by.yahor.chat.server.NetPeer;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import my.yahorfilipchyk.console.Console;

/**
 *
 * @author Yahor
 */
public class Conversation extends Thread {
    
    private NetPeer peer;
    private boolean finished = false;
    private List<MessageListener> listeners = new ArrayList<>();
    
    public Conversation(NetPeer peer) {
        this.peer = peer;
    }
    
    public void addMessageListener(MessageListener listener) {
        this.listeners.add(listener);
    }
    
    public void sendMessage(String message) {
        peer.sendMessage(message);
    }
    
    public void close() {
        peer.close();
        finished = true;
    }

    @Override
    public void run() {
        while(!finished) {
            String message = peer.resieveMessage();
            // null means connection was lost
            if (message == null) {
                close();
                continue;
            }
            for (MessageListener listener : listeners) {
                listener.messageRecieved(message);
            }
        }
    }
    
}
