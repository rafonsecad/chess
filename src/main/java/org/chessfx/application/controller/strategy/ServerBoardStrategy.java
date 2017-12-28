/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.application.controller.strategy;

import org.chessfx.application.ChessBoardDrawer;
import org.chessfx.application.model.ChessBoard;
import org.chessfx.core.piece.Team;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author rafael
 */
public class ServerBoardStrategy implements BoardControllerStrategy{

    final private String ipClient;
    final private Team team;
    private Team lastTeamTurn;
    
    public ServerBoardStrategy(String ipClient, Team team){
        this.ipClient = ipClient;
        this.team = team;
        lastTeamTurn = team == Team.WHITE ? Team.BLACK : Team.WHITE;
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
    
    @Override
    public boolean isTurnValid(boolean local){
        if (local && lastTeamTurn != team){
            return true;
        }
        if (!local && lastTeamTurn == team){
            return true;
        }
        return false;
    }
    
    @Override
    public void updateTeamTurn(){
        if (lastTeamTurn == Team.WHITE){
            lastTeamTurn = Team.BLACK;
            return;
        }
        lastTeamTurn = Team.WHITE;
    }
}
