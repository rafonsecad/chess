/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.model;

import org.chessfx.core.piece.Piece;

/**
 *
 * @author rafael
 */
public final class Square {
    
    private int rank;
    private char file;
    private boolean ocuppied;
    private boolean darkColor;
    private Piece piece;

    public Square(){
        
    }
    
    public Square (int rank, char file){
        setRank(rank);
        setFile(file);
        setOcuppied(false);
    }
    
    public Square (int rank, char file, boolean ocuppied, boolean darkColor){
        setRank(rank);
        setFile(file);
        setOcuppied(ocuppied);
        setDarkColor(darkColor);
    }
    
    public Square (int rank, char file, boolean ocuppied, boolean darkColor, Piece piece){
        setRank(rank);
        setFile(file);
        setOcuppied(ocuppied);
        setPiece(piece);
        setDarkColor(darkColor);
    }
    
    public static Square getSquare(Square square){
        Square s = new Square (square.getRank(), square.getFile(), square.isOcuppied(), square.isDarkColor());
        Piece piece = square.getPiece();
        Piece p = new Piece(piece.getTeam(), piece.getType(), piece.isActive());
        s.setPiece(p);
        return s;
    }
    
    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public char getFile() {
        return file;
    }

    public void setFile(char file) {
        this.file = file;
    }

    public boolean isOcuppied() {
        return ocuppied;
    }

    public void setOcuppied(boolean ocuppied) {
        this.ocuppied = ocuppied;
    }

    public boolean isDarkColor() {
        return darkColor;
    }

    public void setDarkColor(boolean darkColor) {
        this.darkColor = darkColor;
    }
    
    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
    
    @Override
    public boolean equals(Object object){
        if (!(object instanceof Square)){
            return false;
        }
        Square square = (Square) object;
        if (this.getFile() != square.getFile()){
            return false;
        }
        if (this.getRank() != square.getRank()){
            return false;
        }
        return this.isOcuppied() == square.isOcuppied();
    }
}
