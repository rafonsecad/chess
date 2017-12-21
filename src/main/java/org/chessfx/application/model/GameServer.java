/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.application.model;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author rafael
 */
public class GameServer {

    private final SimpleStringProperty ip;

    public GameServer(String ip) {
        this.ip = new SimpleStringProperty(ip);
    }

    public String getIp() {
        return ip.get();
    }

    public void setIp(String ip) {
        this.ip.set(ip);
    }
}
