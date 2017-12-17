/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.application.controller;

import javax.servlet.http.HttpServletRequest;
import org.chessfx.application.NetworkApp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author rafael
 */
@RestController
public class ServerController {
    @GetMapping(value="/api/server/requestgame")
    public ResponseEntity<Void> requestGame(HttpServletRequest request){
        String ip = request.getRemoteAddr();
        NetworkApp.setIpClient(ip);
        NetworkApp.stopServer();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
