/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.service;

import java.util.List;
import java.util.stream.Collectors;
import org.chessfx.core.model.Board;
import org.chessfx.core.model.Square;
import org.chessfx.core.piece.Team;

/**
 *
 * @author rafael
 */
public class KingMovementResolver {
    
    private Board board;
    
    public void setBoard(Board board){
        this.board = board;
    }
    
    public List<Square> getKingMovements (Square selected){
        List<Square> movements = this.board.getSquares().stream()
                                 .filter(square -> isKingMovement(square, selected))
                                 .collect(Collectors.toList());
        return movements;
    }
    
    private boolean isKingMovement(Square square, Square selected){
        if (square.getRank() == selected.getRank() && square.getFile() == selected.getFile()){
            return false;
        }
        if (square.getRank() != selected.getRank() + 1 && square.getRank() != selected.getRank() - 1 && square.getRank() != selected.getRank()){
            return false;
        }
        if (square.getFile() != selected.getFile() + 1 && square.getFile() != selected.getFile() - 1 && square.getFile() != selected.getFile()){
            return false;
        }
        if (!square.isOcuppied()){
            return true;
        }
        Team selectedTeam = selected.getPiece().getTeam();
        Team squareTeam = square.getPiece().getTeam();
        
        if (selectedTeam == squareTeam){
            return false;
        }
        return true;
    }
}
