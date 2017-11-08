/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.service.movementResolver;

import java.util.List;
import java.util.stream.Collectors;
import org.chessfx.core.model.Board;
import org.chessfx.core.model.Square;
import org.chessfx.core.piece.Team;

/**
 *
 * @author rafael
 */
public class KnightMovementResolver implements PieceMovementResolver {
    
    private Board board;
    private List<Board> historic;
    
    @Override
    public void setBoard(Board board){
        this.board = board;
    }
    
    @Override
    public void setHistoricBoards (List<Board> historic){
        this.historic = historic;
    }
    
    @Override
    public List<Square> getMovements (Square selected){
        List<Square> movements = this.board.getSquares().stream()
                                 .filter(square -> isKnightMovement(square, selected))
                                 .collect(Collectors.toList());
        return movements;
    }
    
    private boolean isKnightMovement(Square square, Square selected){
        
        boolean filePlusTwo = square.getFile() == selected.getFile() + 2;
        boolean filePlusMinusTwo = square.getFile() == selected.getFile() - 2 ;
        
        boolean filePlusOne =  square.getFile() == selected.getFile() + 1;
        boolean filePlusMinusOne = square.getFile() == selected.getFile() - 1;
        
        boolean rankPlusTwo = square.getRank() == selected.getRank() + 2;
        boolean rankPlusMinusTwo = square.getRank() == selected.getRank() - 2;
        
        boolean rankPlusOne = square.getRank() == selected.getRank() + 1;
        boolean rankPlusMinusOne = square.getRank() == selected.getRank() - 1;
        
        if (filePlusMinusOne || filePlusOne ){
            if(rankPlusTwo || rankPlusMinusTwo){
                return isEnemySquare(square, selected);
            }
        }
        if (filePlusMinusTwo || filePlusTwo ){
            if(rankPlusOne || rankPlusMinusOne){
                return isEnemySquare(square, selected);
            }
        }
        return false;
    }
    
    private boolean isEnemySquare(Square square, Square selected){
        if (!square.isOcuppied()){
            return true;
        }
        Team teamSelected = selected.getPiece().getTeam();
        Team teamSquare = square.getPiece().getTeam();
        return teamSelected != teamSquare;
    }
}
