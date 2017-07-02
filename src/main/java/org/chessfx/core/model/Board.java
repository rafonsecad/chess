/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author rafael
 */
public class Board {
    private List<Square> squares;

    public List<Square> getSquares() {
        if(squares == null){
            return new ArrayList<>();
        }
        return squares.stream().map(s -> s).collect(Collectors.toList());
    }

    public void setSquares(List<Square> squares) {
        this.squares = squares;
    }
    
}
