/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.service;

import java.util.List;
import org.chessfx.core.model.Board;
import org.chessfx.core.model.Square;
import org.chessfx.core.piece.Piece;

/**
 *
 * @author rafael
 */
public interface BoardService {
    
    void initBoard();
    
    void movePiece(Square from, Square to);
    
    Board getBoard();
    
    List<Piece> getDeadPieces();
    
    List<Square> getAllowedMovements(Square square);
    
}
