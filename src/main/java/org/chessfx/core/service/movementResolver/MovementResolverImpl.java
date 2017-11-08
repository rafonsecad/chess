/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.service.movementResolver;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.chessfx.core.model.Board;
import org.chessfx.core.model.Square;
import org.chessfx.core.piece.Piece;
import org.chessfx.core.piece.Team;
import org.chessfx.core.piece.TypePiece;
import org.chessfx.core.service.MovementResolver;
import org.springframework.stereotype.Component;

/**
 *
 * @author rafael
 */
@Component
public class MovementResolverImpl implements MovementResolver {

    private Board board;
    private List<Board> historic;

    @Override
    public List<Square> getAllowedMovements(Square selected) {
        TypePiece type = selected.getPiece().getType();
        PieceMovementResolver pieceResolver = PieceResolverFactory.getPieceResolver(type);
        pieceResolver.setBoard(board);
        pieceResolver.setHistoricBoards(historic);
        return pieceResolver.getMovements(selected);
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
    
    @Override
    public void setHistoricBoards(List<Board> historic){
        this.historic = historic;
    }   
}
