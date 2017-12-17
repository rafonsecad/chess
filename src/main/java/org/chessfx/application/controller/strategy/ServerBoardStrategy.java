/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.application.controller.strategy;

import org.chessfx.application.ChessBoardDrawer;
import org.chessfx.application.model.ChessBoard;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author rafael
 */
public class ServerBoardStrategy implements BoardControllerStrategy{

    final private String ipClient;
    
    public ServerBoardStrategy(String ipClient){
        this.ipClient = ipClient;
    }
    
    @Override
    public void updateView(ChessBoardDrawer drawer, ChessBoard chessBoard) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://" + ipClient + ":8080" + "/api/client/chessboard";
        ResponseEntity<Void> response = restTemplate.postForEntity(url, chessBoard, Void.class);
        if (response.getStatusCode() == HttpStatus.ACCEPTED){
            drawer.draw(chessBoard);
        }
    }
    
}
