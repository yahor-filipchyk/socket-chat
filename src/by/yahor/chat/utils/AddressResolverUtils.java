/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.yahor.chat.utils;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 *
 * @author Yahor
 */
public class AddressResolverUtils {
    
    public static InetAddress getLocalAddress() {
        InetAddress address = null;
        List<NetworkInterface> netInts = null;
        try {
            netInts = Collections.list(NetworkInterface.getNetworkInterfaces());
            if (netInts.size() == 1) {
                return InetAddress.getLocalHost();
            }
            
            for (NetworkInterface net : netInts) {
                if (!net.isLoopback() && !net.isVirtual() && net.isUp()) {
                    Enumeration<InetAddress> addrEnum = net.getInetAddresses();
                    while (addrEnum.hasMoreElements()) {
                        InetAddress addr = addrEnum.nextElement();
                        if (!(addr instanceof Inet4Address)) {
                            continue;
                        }
                        if (!addr.isLoopbackAddress() && !addr.isAnyLocalAddress()
                                && !addr.isLinkLocalAddress() && !addr.isMulticastAddress()) {
                            address = addr;
                            break;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return address;
    }
}
