/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.chessfx.core.model.Board;
import org.chessfx.core.model.Square;
import org.chessfx.core.piece.Piece;
import org.chessfx.core.piece.Team;

/**
 *
 * @author rafael
 */
public class PawnMovementResolver {
    
    private Board board;
    
    public void setBoard(Board board){
        this.board = board;
    }
    
    public List<Square> getPawnMovements (Square selected){
        List<Square> movements = this.board.getSquares().stream()
                                .filter(square -> isPawnMovement(square, selected))
                                .sorted(Comparator.comparing(Square::getRank))
                                .collect(Collectors.toList());
        
        List<Square> movementsWithObstacles = getPawnMovementsWithObstacles(movements, selected);
        
        List<Square> AttackingSquares = this.board.getSquares().stream()
                                        .filter(square -> canPawnAttack(square, selected))
                                        .collect(Collectors.toList());
        
        List<Square> totalAllowedSquares = Stream.concat(movementsWithObstacles.stream(), 
                                           AttackingSquares.stream())
                                          .collect(Collectors.toList());
        return totalAllowedSquares;
    }
    
    private boolean isPawnMovement(Square square, Square selected){
        if (square.getFile() != selected.getFile()){
            return false;
        }
        int advanceFactor = selected.getPiece().getTeam() == Team.WHITE ? 1 : -1;
        int rank = advanceFactor + selected.getRank();
        if(rank == square.getRank()){
            return true;
        }
        if (!selected.getPiece().isFirstMovement()){
            return false;
        }
        int secondRank = (advanceFactor*2) + selected.getRank();
        return secondRank == square.getRank();
    }
    
    private List<Square> getPawnMovementsWithObstacles (List<Square> movements, Square selected){
        List<Square> movementsSorted = movements.stream().map(square -> square).collect(Collectors.toList());
        if (selected.getPiece().getTeam() == Team.BLACK){
            Collections.reverse(movementsSorted);
        }
        
        List<Square> movementsWithObstacles = new ArrayList<>();
        for(Square movement : movementsSorted){
            if (movement.isOcuppied()){
                break;
            }
            movementsWithObstacles.add(movement);
        }
        
        if(selected.getPiece().getTeam() == Team.BLACK){
            Collections.reverse(movementsWithObstacles);
        }
        return movementsWithObstacles;
    }
    
    private boolean canPawnAttack(Square square, Square selected){
        Piece piece = selected.getPiece();
        Team oppositeTeam = (piece.getTeam() == Team.WHITE)? Team.BLACK: Team.WHITE;
        int nextRank = selected.getRank() + 1;
        if (piece.getTeam() == Team.BLACK){
            nextRank = selected.getRank() - 1;
        }
        if(square.getRank() != nextRank){
            return false;
        }
        if (square.getFile() != selected.getFile() - 1 && square.getFile() != selected.getFile() + 1){
            return false;
        }
        if (!square.isOcuppied()){
            return false;
        }
        if (square.getPiece().getTeam() != oppositeTeam){
            return false;
        }
        return true;
    }
}
