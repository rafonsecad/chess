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
import org.chessfx.core.piece.Team;

/**
 *
 * @author rafael
 */
public interface MovementResolver {
    
    void setBoard(Board board);
    
    void setHistoricBoards (List<Board> historic);
    
    List<Square> getAllowedMovements(Square square);
    
    Optional<Square> kingInCheck(Team team);
    
    List<Square> getAllowedMovementsWithoutCheck(List<Square> movements, Square selected);
            
}
