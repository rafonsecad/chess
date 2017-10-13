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
public class PawnMovementResolver {
    
    private Board board;
    private List<Board> historic;
    
    public void setBoard(Board board){
        this.board = board;
    }
    
    public void setHistoricBoards (List<Board> historic){
        this.historic = historic;
    }
    
    public List<Square> getPawnMovements (Square selected){
        List<Square> movements = getPawnFreeMovements(selected);
        List<Square> movementsWithObstacles = getPawnMovementsWithObstacles(movements, selected);
        List<Square> attackingSquares = getAttackingSquares(selected);
        List<Square> enPassantSquare = getEnPassantSquare(selected);
        List<Square> totalAllowedSquares = getAllowedSquares(movementsWithObstacles, attackingSquares, enPassantSquare);
        return totalAllowedSquares;
    }
    
    private List<Square> getPawnFreeMovements (Square selected){
        return this.board.getSquares().stream()
            .filter(square -> isPawnMovement(square, selected))
            .sorted(Comparator.comparing(Square::getRank))
            .collect(Collectors.toList());
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
    
    private List<Square> getAttackingSquares (Square selected){
        return this.board.getSquares().stream()
            .filter(square -> canPawnAttack(square, selected))
            .collect(Collectors.toList());
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
    
    private List<Square> getEnPassantSquare (Square selected){
        Team team = selected.getPiece().getTeam();
        if (!isInEnPassantRank(team, selected)){
            return new ArrayList<>();
        }
        List<Square> enemyPawnSquares = getCloseEnemyPawns(selected);
        List<Square> enPassantMovement = getEnPassantMovement(enemyPawnSquares);
        if (enPassantMovement.isEmpty()){
            return new ArrayList<>();
        }
        return board.getSquares().stream()
            .filter(s -> isValidEnPassantSquare(s,enPassantMovement,team))
            .collect(Collectors.toList());
        
    }
    
    private boolean isInEnPassantRank(Team team, Square selected){
        if (team == Team.BLACK && selected.getRank() == 4){
            return true;
        } 
        if (team == Team.WHITE && selected.getRank() == 5){
            return true;
        }
        return false;
    }
    
    private List<Square> getCloseEnemyPawns(Square selected){
        return board.getSquares().stream()
            .filter(s -> hasCloseEnemyPawns(s, selected))
            .collect(Collectors.toList());
    }
    
    private boolean hasCloseEnemyPawns (Square s, Square selected){
        Team team = selected.getPiece().getTeam();
        Team enemyTeam = team == Team.WHITE ? Team.BLACK : Team.WHITE;
        Piece enemyPawn = new Piece(enemyTeam, TypePiece.PAWN);
        if (!(s.getFile() == selected.getFile() + 1 || s.getFile() == selected.getFile() - 1)){
            return false;
        }
        if (!s.isOcuppied()){
            return false;
        }
        if(!s.getPiece().equals(enemyPawn)){
            return false;
        }
        if (s.getRank() == 4 && team == Team.BLACK){
            return true;
        }
        if (s.getRank() == 5 && team == Team.WHITE){
            return true;
        }
        return false;
    }
    
    private List<Square> getEnPassantMovement(List<Square> enemyPawnSquares){
        return enemyPawnSquares.stream()
                .filter(s -> isEnPassantMovement(s))
                .collect(Collectors.toList());
    }
    
    private boolean isValidEnPassantSquare(Square s, List<Square> enPassantMovement, Team team){
        if (s.getFile() != enPassantMovement.get(0).getFile()){
            return false;
        }
        if (s.getRank() == enPassantMovement.get(0).getRank() - 1 && team == Team.BLACK){
            return true;
        }
        if (s.getRank() == enPassantMovement.get(0).getRank() + 1 && team == Team.WHITE){
            return true;
        }
        return false;
    }
    
    private boolean isEnPassantMovement (Square enemyPawnSquare){
        if (historic.size() <= 1){
            return false;
        }
        Piece enemyPawn = enemyPawnSquare.getPiece();
        int previousPawnRank = enemyPawnSquare.getRank() + 2;
        int blankRank = enemyPawnSquare.getRank() + 1;
        if (enemyPawn.getTeam() == Team.WHITE){
            previousPawnRank = enemyPawnSquare.getRank() - 2;
            blankRank = enemyPawnSquare.getRank() - 1;
        }
        return areEnPassantSquaresFound(enemyPawnSquare, previousPawnRank, blankRank);
    }
    
    private boolean areEnPassantSquaresFound (Square enemyPawnSquare, int previousPawnRank, int blankRank){
        Board previousBoard = historic.get(historic.size() - 2);
        Optional<Square> squareEnPassant = previousBoard.getSquares().stream()
            .filter(s -> s.getFile() == enemyPawnSquare.getFile() && s.getRank() == previousPawnRank && s.isOcuppied() && s.getPiece().isFirstMovement())
            .findFirst();
        Optional<Square> squareBlank = previousBoard.getSquares().stream()
            .filter(s -> s.getFile() == enemyPawnSquare.getFile() && s.getRank() == blankRank && !s.isOcuppied())
            .findFirst();
        if (squareEnPassant.isPresent() && squareBlank.isPresent()){
            return true;
        }
        return false;
    }
    
    private List<Square> getAllowedSquares (List<Square> movementsWithObstacles, List<Square> attackingSquares, List<Square> enPassantSquares){
        List<Square> totalSquares = movementsWithObstacles;
        totalSquares.addAll(attackingSquares);
        totalSquares.addAll(enPassantSquares);
        return totalSquares;
    }
}
