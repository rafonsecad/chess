/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.application.controller.strategy;

import org.chessfx.application.ChessBoardDrawer;
import org.chessfx.application.model.ChessBoard;

/**
 *
 * @author rafael
 */
public class SingleBoardStrategy implements BoardControllerStrategy{

    @Override
    public void updateView(ChessBoardDrawer drawer, ChessBoard chessBoard) {
        drawer.draw(chessBoard);
    }
    
}
