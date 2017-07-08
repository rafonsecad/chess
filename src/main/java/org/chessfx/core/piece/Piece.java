/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.piece;

import java.util.Objects;

/**
 *
 * @author rafael
 */
public class Piece {
    
    private boolean firstMovement;
    private boolean active;
    private TypePiece type;
    private Team team;
    
    public Piece(){
        
    }
    
    public Piece (Team team, TypePiece type){
        this.setTeam(team);
        this.setType(type);
    }
    
    public Piece (Team team, TypePiece type, boolean active){
        this.setTeam(team);
        this.setType(type);
        this.setActive(active);
    }

    public boolean isFirstMovement() {
        return firstMovement;
    }

    public void setFirstMovement(boolean firstMovement) {
        this.firstMovement = firstMovement;
    }
    
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public TypePiece getType() {
        return type;
    }

    public void setType(TypePiece type) {
        this.type = type;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.type);
        hash = 97 * hash + Objects.hashCode(this.team);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Piece other = (Piece) obj;
        if (this.type != other.type) {
            return false;
        }
        if (this.team != other.team) {
            return false;
        }
        return true;
    }
        
}
