/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.application.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.chessfx.core.model.Square;
import org.chessfx.core.piece.Piece;
import org.chessfx.core.piece.Team;

/**
 *
 * @author rafael
 */
public class ChessBoard {
    private List<SquareImage> squareImages;
    private List<Piece> deadPieces;
    private List<String> notations;
    private boolean promotion;
    private Team teamPromoted;
    private Square squarePromoted;

    public ChessBoard(){
        squareImages = new ArrayList<>();
        deadPieces = new ArrayList<>();
        notations = new ArrayList<>();
        promotion = false;
        teamPromoted = Team.WHITE;
    }
    
    public List<SquareImage> getSquareImages() {
        return squareImages.stream().map(square -> square).collect(Collectors.toList());
    }

    public void setSquareImages(List<SquareImage> squareImages) {
        this.squareImages = squareImages;
    }

    public List<Piece> getDeadPieces() {
        return deadPieces.stream().map(piece -> piece).collect(Collectors.toList());
    }

    public void setDeadPieces(List<Piece> deadPieces) {
        this.deadPieces = deadPieces;
    }
    
    public boolean hasPromotion() {
        return promotion;
    }

    public void setPromotion(boolean promotion) {
        this.promotion = promotion;
    }

    public Team getTeamPromoted() {
        return teamPromoted;
    }

    public void setTeamPromoted(Team teamPromoted) {
        this.teamPromoted = teamPromoted;
    }

    public Square getSquarePromoted() {
        return squarePromoted;
    }

    public void setSquarePromoted(Square squarePromoted) {
        this.squarePromoted = squarePromoted;
    }

    public List<String> getNotations() {
        return notations.stream().map(n -> n).collect(Collectors.toList());
    }

    public void setNotations(List<String> notations) {
        this.notations = notations;
    }
}
