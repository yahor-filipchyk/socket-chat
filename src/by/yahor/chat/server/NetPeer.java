
package by.yahor.chat.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Yahor
 */
public class NetPeer {
    
//    private InetAddress address;
    private Socket socket;
    private BufferedReader in;
    private Writer out;
    
    public NetPeer(Socket socket) {
        this.socket = socket;
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    public void close() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
        if (in != null) {
            try {
                in.close();
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }

    public BufferedReader getIn() {
        return in;
    }

    public Writer getOut() {
        return out;
    }

    public Socket getSocket() {
        return socket;
    }   
    
    public String resieveMessage() {
        String message = null;
        try {
            message = in.readLine();
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return message;
    }
    
    public void sendMessage(String message) {
        try {
            out.write(message + "\n");
            out.flush();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
