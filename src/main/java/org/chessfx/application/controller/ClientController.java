/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.application.controller;

import javax.servlet.http.HttpServletRequest;
import org.chessfx.application.ClientApp;
import org.chessfx.application.NetworkApp;
import org.chessfx.application.model.ChessBoard;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author rafael
 */
@RestController
public class ClientController {
    @GetMapping("/api/client/server")
    public ResponseEntity<Void> getServer(HttpServletRequest request){
        if (!NetworkApp.address.contains(request.getRemoteAddr())){
            NetworkApp.address.add(request.getRemoteAddr());
            NetworkApp.updateServerList();
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    
    @PostMapping("/api/client/chessboard")
    public ResponseEntity<Void> drawChessBoard(@RequestBody ChessBoard board){
        ClientApp.setChessBoard(board);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
