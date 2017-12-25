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
    private final SimpleStringProperty team;

    public GameServer(String ip, String team) {
        this.ip = new SimpleStringProperty(ip);
        this.team = new SimpleStringProperty(team);
    }

    public String getIp() {
        return ip.get();
    }

    public void setIp(String ip) {
        this.ip.set(ip);
    }
    
    public String getTeam(){
        return team.get();
    }
    
    public void setTeam(String team){
        this.team.set(team);
    }
}
