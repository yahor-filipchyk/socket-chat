/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.yahor.chat.client;

import java.util.Arrays;

/**
 *
 * @author Yahor
 */
public class ClientLocation {
    
    private byte[] ipv4;
    private int port;
    
    public ClientLocation() {        
    }
    
    public ClientLocation(byte[] ipv4, int port) {   
        this.ipv4 = ipv4;
        this.port = port;
    }

    public byte[] getIpv4() {
        return ipv4;
    }

    public void setIpv4(byte[] ipv4) {
        this.ipv4 = ipv4;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Arrays.hashCode(this.ipv4);
        hash = 89 * hash + this.port;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClientLocation other = (ClientLocation) obj;
        if (!Arrays.equals(this.ipv4, other.ipv4)) {
            return false;
        }
        if (this.port != other.port) {
            return false;
        }
        return true;
    }
}
