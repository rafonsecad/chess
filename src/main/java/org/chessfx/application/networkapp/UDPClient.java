/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.application.networkapp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author rafael
 */
public class UDPClient implements Runnable {

    private final String DATAGRAM_ADDRESS = "255.255.255.255";
    private final int DATAGRAM_PORT = 4445;
    private final String CHESS_FX = "ChessFX";
    
    private Thread thread;
    private boolean alive;

    @Override
    public void run() {
        alive = true;
        try {
            while (alive) {
                DatagramSocket socket = new DatagramSocket();
                InetAddress address = InetAddress.getByName(DATAGRAM_ADDRESS);
                byte[] buffer = CHESS_FX.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, DATAGRAM_PORT);
                socket.setBroadcast(true);
                socket.send(packet);
                socket.close();
                Thread.sleep(1000);
            }
        } catch (Exception e) {

        }
    }

    public void stop() {
        alive = false;
    }
    
    public boolean isAlive(){
        return alive;
    }
    
    public void start() {
        if (thread != null) {
            return;
        }
        thread = new Thread(this, "UDPClient");
        thread.start();
    }
}
