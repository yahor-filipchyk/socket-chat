/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketstest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yahor
 */
public class SocketsTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Socket socket = null;
        try {            
//            for (int i = 0; i < 10; i++) {
                socket = new Socket(InetAddress.getLocalHost(), 2000);
                System.out.println(socket.getInetAddress());
                Writer writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                System.out.println("writer is created");
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("reader is created");
                writer.write("register\nmyname\n");
                System.out.println("register query is sent");
                writer.flush();
                System.out.println("register query is flushed");
                System.out.println(reader.readLine());
//                System.out.println(reader.readLine());
                writer.write("disconnect\n");
                writer.flush();
                reader.close();
                writer.close();
//            }
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
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
