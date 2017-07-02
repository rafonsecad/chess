/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.piece;

import java.util.ArrayList;
import java.util.List;
import org.chessfx.core.model.Square;

/**
 *
 * @author rafael
 */
public class Pawn extends Piece{

    private boolean firstMove;
    
    public Pawn(){
        this.firstMove = true;
    }
    
    @Override
    public void move(Square position) {
        this.setPosition(position);
    }

    @Override
    public List<Square> getPosiblePositions() {
        return new ArrayList<>();
    }
    
}
