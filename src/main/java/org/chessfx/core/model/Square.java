/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.model;

/**
 *
 * @author rafael
 */
public class Square {
    
    private int rank;
    private char file;
    private boolean ocuppied;

    public Square(){
        
    }
    
    public Square (int rank, char file){
        setRank(rank);
        setFile(file);
        setOcuppied(false);
    }
    
    public Square (int rank, char file, boolean ocuppied){
        setRank(rank);
        setFile(file);
        setOcuppied(ocuppied);
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
