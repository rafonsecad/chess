/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.chessfx.core.model.Board;
import org.chessfx.core.model.Square;
import org.chessfx.core.piece.Piece;
import org.chessfx.core.piece.Team;
import org.chessfx.core.piece.TypePiece;
import org.springframework.stereotype.Component;

/**
 *
 * @author rafael
 */
@Component
public class MovementResolverImpl implements MovementResolver {

    private Board board;

    @Override
    public List<Square> getAllowedMovements(Square selected) {
        List<Square> movements;
        TypePiece type = selected.getPiece().getType();
        switch(type){
            case PAWN:
                movements = getPawnMovements(selected);
                break;
            case KNIGHT:
                movements = getKnightMovements(selected);
                break;
            case ROOK:
                movements = getRookMovements(selected);
                break;
            case BISHOP:
                movements = getBishopMovements(selected);
                break;
            case QUEEN:
                movements = getQueenMovements(selected);
                break;
            case KING:
                movements = getKingMovements(selected);
                break;
            default:
                movements = getKingMovements(selected);
                break;
        }
        return movements;
    }

    @Override
    public List<Square> getAllowedMovementsWithoutCheck(List<Square> movements, Square selected){
        Team team = selected.getPiece().getTeam();
        if (!isKingPresent(team)){
            return movements;
        }
        return movements.stream()
                   .filter(m -> isMovementOutOfCheck(selected, m))
                   .collect(Collectors.toList());
    }
    
    private boolean isKingPresent (Team team){
       return board.getSquares().stream()
              .filter(square -> hasAKing(square, team))
              .findAny()
              .isPresent();
    } 
    
    private boolean hasAKing(Square square, Team team){
        if (!square.isOcuppied()){
            return false;
        }
        if (square.getPiece().getTeam() != team){
            return false;
        }
        if (square.getPiece().getType() != TypePiece.KING){
            return false;
        }
        return true;
    }
    
    private boolean isMovementOutOfCheck(Square selected, Square movement){
        List<Square> squaresWithMovement = getSquaresWithMovement(selected, movement);
        Board boardWithMovement = new Board();
        boardWithMovement.setSquares(squaresWithMovement);
        KingMovementResolver kingResolver = new KingMovementResolver();
        kingResolver.setBoard(boardWithMovement);
        Optional <Square> kingSquare = kingResolver.kingInCheck(selected.getPiece().getTeam());
        if(kingSquare.isPresent()){
            return false;
        }
        return true;
    }
    
    private List<Square> getSquaresWithMovement(Square selected, Square movement){
        return board.getSquares().stream()
              .map(s -> movePiece(s, selected, movement))
              .collect(Collectors.toList());
    }
    
    private Square movePiece(Square s, Square selected, Square movement){
        if(s.equals(selected)){
          return new Square(selected.getRank(), selected.getFile(), false, selected.isDarkColor());
        }
        if(s.equals(movement)){
            Piece fromPiece = selected.getPiece();
            Piece piece = new Piece(fromPiece.getTeam(), fromPiece.getType(), true, false);
            return new Square(movement.getRank(), movement.getFile(), true, movement.isDarkColor(), piece);
        }
        return s;
    }
    
    @Override
    public Optional<Square> kingInCheck(Team team){
        KingMovementResolver kingResolver = new KingMovementResolver();
        kingResolver.setBoard(board);
        return kingResolver.kingInCheck(team);
    }
    
    @Override
    public void setBoard(Board board) {
        this.board = board;
    }
    
    private List<Square> getKingMovements(Square selected){
        KingMovementResolver kingResolver = new KingMovementResolver();
        kingResolver.setBoard(board);
        return kingResolver.getKingMovements(selected);
    }
    
    private List<Square> getRookMovements(Square selected){
        GeneralMovementResolver generalResolver = new GeneralMovementResolver();
        generalResolver.setBoard(board);
        return generalResolver.getRookMovements(selected);
    }
    
    private List<Square> getBishopMovements(Square selected){
        GeneralMovementResolver generalResolver = new GeneralMovementResolver();
        generalResolver.setBoard(board);
        return generalResolver.getBishopMovements(selected);
    }
    
    private List<Square> getQueenMovements(Square selected){
        GeneralMovementResolver generalResolver = new GeneralMovementResolver();
        generalResolver.setBoard(board);
        return generalResolver.getQueenMovements(selected);
    }
    
    private List<Square> getKnightMovements(Square selected){
        KnightMovementResolver knightResolver = new KnightMovementResolver();
        knightResolver.setBoard(board);
        return knightResolver.getKnightMovements(selected);
    }
    
    private List<Square> getPawnMovements(Square selected){
        PawnMovementResolver pawnResolver = new PawnMovementResolver();
        pawnResolver.setBoard(board);
        return pawnResolver.getPawnMovements(selected);
    }
    
}
