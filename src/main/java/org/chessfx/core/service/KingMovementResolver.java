/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.chessfx.core.model.Board;
import org.chessfx.core.model.Square;
import org.chessfx.core.piece.Piece;
import org.chessfx.core.piece.Team;
import org.chessfx.core.piece.TypePiece;

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
        List<Square> freeMovements = getKingFreeMovements(selected);
        List<Square> squaresWithEnemies = getSquaresWithEnemies(selected);
        List<Square> attackedSquares = getAttackedSquares(squaresWithEnemies);
        List<Square> movements = getAllowedKingMovements(freeMovements, attackedSquares);
        return movements;
    }
    
    private List<Square> getKingFreeMovements(Square selected){
        return   board.getSquares().stream()
                 .filter(square -> isKingMovement(square, selected))
                 .collect(Collectors.toList());
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
    
    List<Square> getSquaresWithEnemies (Square selected){
        Team team = selected.getPiece().getTeam();
        return board.getSquares().stream()
               .filter(square -> isEnemySquare(square, team))
               .collect(Collectors.toList());
    }
    
    private boolean isEnemySquare(Square square, Team team){
        return square.isOcuppied() && square.getPiece().getTeam() != team;
    }
    
    private List<Square> getAttackedSquares(List<Square> squaresWithEnemies){
        MovementResolver resolver = new MovementResolverImpl();
        resolver.setBoard(board);
        return  squaresWithEnemies.stream()
                .map(square -> getAttackedSquaresByPiece(square, resolver))
                .flatMap(list -> list.stream())
                .distinct()
                .collect(Collectors.toList());
    }
    
    private List<Square> getAttackedSquaresByPiece(Square square, MovementResolver resolver){
        TypePiece type = square.getPiece().getType();
        if(type == TypePiece.KING){
            return getKingFreeMovements(square);
        }
        if(type == TypePiece.PAWN){
            return getPawnAttacks(square);
        }
        return resolver.getAllowedMovements(square);
    }
    
    private List<Square> getPawnAttacks(Square square){
        Piece pawn = square.getPiece();
        int direction = pawn.getTeam() == Team.WHITE ? 1 : -1;
        List<Square> squares = board.getSquares().stream()
                               .filter(s -> isSquareAttackedByPawn(s, square, direction))
                               .collect(Collectors.toList());
        return squares;
    }
    
    private boolean isSquareAttackedByPawn (Square s, Square squareWithPawn, int direction){ 
        if (s.getRank() != squareWithPawn.getRank() + direction){
            return false;
        }
        if (s.getFile() == squareWithPawn.getFile() + 1){
            return true;
        }
        if (s.getFile() == squareWithPawn.getFile() - 1){
            return true;
        }
        return false;
    }
    
    private List<Square> getAllowedKingMovements (List<Square> freeMovements, List<Square> attackedSquares){
        return  freeMovements.stream()
                .filter(square -> isAnAttackedSquare(square, attackedSquares))
                .collect(Collectors.toList());
    }
    
    private boolean isAnAttackedSquare(Square square, List<Square> attackedSquares){
        Optional<Square> squareFound = attackedSquares.stream()
                                      .filter(s -> s.equals(square))
                                      .findAny();
        return !squareFound.isPresent();
    }
}
