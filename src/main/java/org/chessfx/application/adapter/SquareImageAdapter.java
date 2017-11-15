/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.application.adapter;

import java.util.List;
import java.util.stream.Collectors;
import org.chessfx.application.model.SquareImage;
import org.chessfx.core.model.Square;

/**
 *
 * @author rafael
 */
public class SquareImageAdapter {

    public static SquareImage toSquareImage(Square square) {
        SquareImage squareImage = new SquareImage();
        squareImage.setDarkColor(square.isDarkColor());
        squareImage.setFile(square.getFile());
        squareImage.setRank(square.getRank());
        squareImage.setOcuppied(square.isOcuppied());
        squareImage.setPiece(square.getPiece());
        return squareImage;
    }

    public static List<SquareImage> toListSquareImage(List<Square> squares) {
        return squares.stream().map(s -> toSquareImage(s)).collect(Collectors.toList());
    }
}
