/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.piece;

/**
 *
 * @author rafael
 */
public enum TypePiece {
    KING("K"),
    QUEEN("Q"),
    ROOK("R"),
    BISHOP("B"),
    KNIGHT("N"),
    PAWN("")
    ;
    private String letter;
    
    private TypePiece(String letter){
        this.letter = letter;
    }
    
    public String getPieceLetter(){
        return this.letter;
    }
}
