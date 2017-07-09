/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.service;

import java.util.ArrayList;
import java.util.List;
import org.chessfx.core.model.Board;
import org.chessfx.core.model.Square;
import org.springframework.stereotype.Component;

/**
 *
 * @author rafael
 */
@Component
public class MovementResolverImpl implements MovementResolver{

    private Board board;
    
    @Override
    public List<Square> getAllowedMovements(Square square) {
        List<Square> result = new ArrayList<>();
        if (square.getPiece().isFirstMovement()){
            Square s1 = this.board.getSquares().stream()
                            .filter(s -> s.getFile() == square.getFile() && s.getRank() == (square.getRank() + 1)).findFirst().get();
            Square s2 = this.board.getSquares().stream()
                            .filter(s -> s.getFile() == square.getFile() && s.getRank() == (square.getRank() + 2)).findFirst().get();
            result.add(s1);
            result.add(s2);
        }
        else{
            Square s1 = this.board.getSquares().stream()
                        .filter(s -> s.getFile() == square.getFile() && s.getRank() == (square.getRank() + 1))
                        .findFirst().get();
            result.add(s1);
        }
        return result;
    }
    
    @Override
    public void setBoard (Board board){
        this.board = board;
    }
}
