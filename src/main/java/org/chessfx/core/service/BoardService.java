/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.service;

import java.util.List;
import java.util.Optional;
import org.chessfx.core.model.Board;
import org.chessfx.core.model.Square;
import org.chessfx.core.piece.Piece;
import org.chessfx.core.piece.Team;

/**
 *
 * @author rafael
 */
public interface BoardService {
    
    void initBoard();
    
    Optional<Square> kingInCheck(Team team);
    
    void movePiece(Square from, Square to);
    
    boolean isPiecePromoted(Square square);
    
    void promotedPiece(Square square, Piece piece);
    
    Board getBoard();
    
    List<Piece> getDeadPieces();
    
    List<Square> getAllowedMovements(Square square);
    
    List<String> getNotations();
}
