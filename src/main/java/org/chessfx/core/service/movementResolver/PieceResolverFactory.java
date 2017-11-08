/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.service.movementResolver;

import org.chessfx.core.piece.TypePiece;

/**
 *
 * @author rafael
 */
public class PieceResolverFactory {
    
    public static PieceMovementResolver getPieceResolver (TypePiece type){
        switch(type){
            case PAWN:
                return new PawnMovementResolver();
            case KNIGHT:
                return new KnightMovementResolver();
            case ROOK:
                return new RookMovementResolver();
            case BISHOP:
                return new BishopMovementResolver();
            case QUEEN:
                return new QueenMovementResolver();
            case KING:
                return new KingMovementResolver();
            default:
                return new KingMovementResolver();
        }
    }
}
