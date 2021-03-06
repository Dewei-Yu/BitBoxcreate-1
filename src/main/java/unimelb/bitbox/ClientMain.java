package unimelb.bitbox;

import unimelb.bitbox.util.Configuration;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public class ClientMain {
    private static Logger log = Logger.getLogger(ClientMain.class.getName());
    public static void ClientMain() {
        Socket socket;
        String peer = Configuration.getConfigurationValue("peers");
        String addr = null;
        try {
            addr = InetAddress.getLocalHost().toString();
            String[] a = addr.split("/");
            addr = a[1];
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (peer.length() != 0) {
            String[] peers = peer.split(",");
            String address = "";
            int portnumber = 0;
            int i = 0;
            while (i < peers.length) {
                String[] middlepeers = peers[i].split(":");
                address = middlepeers[0];
                portnumber = Integer.parseInt(middlepeers[1]);
               log.info(" \n the address is " + address + "\n the port number is " + portnumber);
                try {
                    if (Connectionlist.contain(address) || addr.equals(address)) {
                        log.info("Already exist in the connection list (adding by server)");
                    } else {
                        socket = new Socket(address, portnumber);
                        peerworker w = new peerworker(socket);
                        Thread t = new Thread(w);
                        t.start();
                    }

                } catch (ConnectException e){
                    log.info("The ipadress cannot be reached due to the other peer is closed, try others");
                }
                catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                i++;
            }

        }
    }
    public static void reconnection(String ip,int port) {
        Socket socket = null;
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        peerworker w = new peerworker(socket);
        Thread t = new Thread(w);
        t.start();
    }



}
