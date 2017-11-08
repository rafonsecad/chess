/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.service.movementResolver;

import java.util.List;
import org.chessfx.core.model.Board;
import org.chessfx.core.model.Square;

/**
 *
 * @author rafael
 */
public interface PieceMovementResolver {
    
    List<Square> getMovements(Square selected);
    
    void setBoard (Board board);
    
    void setHistoricBoards(List<Board> historic);
}
