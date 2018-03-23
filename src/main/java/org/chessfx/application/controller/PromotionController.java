/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.application.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.chessfx.application.adapter.SquareImageAdapter.toListSquareImage;
import org.chessfx.application.model.ChessBoard;
import org.chessfx.application.model.SquareImage;
import org.chessfx.core.model.Square;
import org.chessfx.core.piece.Piece;
import org.chessfx.core.piece.Team;
import org.chessfx.core.piece.TypePiece;
import org.chessfx.core.service.BoardService;

/**
 *
 * @author rafael
 */
public class PromotionController {
    
    final private BoardService boardService;
    
    private final int WIDTH = 80;
    final private ChessBoard chessBoard;

    public PromotionController (BoardService boardService, ChessBoard chessBoard){
        this.boardService = boardService;
        this.chessBoard = chessBoard;
    }
 
    public List<SquareImage> getPromotionSquares (Piece piece){
        Piece fullPiece = new Piece (chessBoard.getTeamPromoted(), piece.getType());
        boardService.promotedPiece(chessBoard.getSquarePromoted(), fullPiece);
        chessBoard.setPromotion(false);
        List<Square> squares = boardService.getBoard().getSquares();
        List<SquareImage> squareImages = toListSquareImage(squares);
        return squareImages;
    }
    
    public Optional<Piece> getPiecePromoted(double x, double y){
        if (3.5*WIDTH > y || 4.5*WIDTH < y){
            return Optional.empty();
        }
        double [] xMins = new double [4];
        double [] xMaxs = new double [4];
        for (int index=0; index < xMins.length; index++){
            xMins[index] = 3.7*WIDTH + WIDTH*index + index*0.2*WIDTH;
            xMaxs[index] = xMins[index] + WIDTH;
            if (xMins[index] <= x && xMaxs[index] >= x){
                return Optional.of(selectPiecePromoted(index));
            }
        }
        return Optional.empty();
    }
    
    private Piece selectPiecePromoted(int index){
        Piece piece = new Piece(Team.WHITE, TypePiece.QUEEN);
        switch(index){
            case 0:
                piece = new Piece(Team.WHITE, TypePiece.QUEEN);
                break;
            case 1:
                piece = new Piece(Team.WHITE, TypePiece.ROOK);
                break;
            case 2:
                piece = new Piece(Team.WHITE, TypePiece.BISHOP);
                break;
            case 3:
                piece = new Piece(Team.WHITE, TypePiece.KNIGHT);
                break;
            default:
        }
        return piece;
    }
}
