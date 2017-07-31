/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.service;

import java.util.List;
import org.chessfx.core.model.Board;
import org.chessfx.core.model.Square;
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
