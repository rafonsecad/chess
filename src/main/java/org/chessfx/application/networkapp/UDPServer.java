/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.application.networkapp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author rafael
 */
public class UDPServer implements Runnable {

    private final int DATAGRAM_PORT = 4445;
    private final String HTTP_PORT = ":8080";
    private final String HTTP = "http://";
    private final String ENDPOINT = "/api/client/server";
    
    private Thread thread;
    private boolean alive;

    @Override
    public void run() {
        try {
            alive = true;
            while (alive) {
                byte[] buffer = new byte[256];
                DatagramSocket socket;
                socket = new DatagramSocket(DATAGRAM_PORT);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                InetAddress address = packet.getAddress();
                String url = HTTP + address.getHostAddress() + HTTP_PORT + ENDPOINT;
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            }
        } catch (Exception e) {

        }
    }

    public void start() {
        if (thread != null) {
            return;
        }
        thread = new Thread(this, "UDPServer");
        thread.start();
    }
    
    public void stop() {
        alive = false;
    }
}
