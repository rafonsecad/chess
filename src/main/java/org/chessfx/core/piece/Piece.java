/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.piece;

import java.util.List;
import org.chessfx.core.model.Board;
import org.chessfx.core.model.Square;

/**
 *
 * @author rafael
 */
public abstract class Piece {
    
    private boolean active;
    private Square position;
    private Board board;
    
    public abstract void move(Square position);
    
    public abstract List<Square> getPosiblePositions();
    
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Square getPosition() {
        return position;
    }

    public void setPosition(Square position) {
        this.position = position;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
    
    protected Board getBoard(){
        return board;
    }
}
