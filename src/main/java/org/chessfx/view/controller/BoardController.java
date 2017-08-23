/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.view.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.chessfx.core.model.Square;
import org.chessfx.core.service.BoardService;
import org.chessfx.view.ChessBoardDrawer;
import org.chessfx.view.model.SquareImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author rafael
 */
@Component("BoardController")
public class BoardController implements EventHandler<MouseEvent>{
    
    @Autowired
    private BoardService boardService;
    
    private ChessBoardDrawer drawer;
    private final int WIDTH = 80;
    private List<SquareImage> squareImagesBoard;
    
    public void init(ChessBoardDrawer drawer){
        this.drawer = drawer;
        boardService.initBoard();
        List<Square> squares = boardService.getBoard().getSquares();
        squareImagesBoard = toListSquareImage(squares);
        this.drawer.draw(squareImagesBoard);
    }
    
    @Override
    public void handle(MouseEvent event) {
        Square square = getSquareByCoordinates(event.getX(), event.getY());
        Square squareInBoard = getSquareInBoard(square);
        Optional<SquareImage> squareSelected = squareImagesBoard.stream().filter(sI -> sI.isSelected() == true).findAny();
        Optional<SquareImage> squareToMove = getSquareToMove(squareInBoard);
        
        if(squareSelected.isPresent() && squareSelected.get().equals(squareInBoard)){
            squareImagesBoard = getBoardUnmarked();
        }
        else if(squareToMove.isPresent()){
            boardService.movePiece(squareSelected.get(), squareToMove.get());
            List<Square> squares = boardService.getBoard().getSquares();
            squareImagesBoard = toListSquareImage(squares);
        }
        else if (squareInBoard.isOcuppied()){
            List<SquareImage> squareImages = getSquarePieceSelected(squareInBoard);
            squareImagesBoard = setAllowedMovementsToSquares(squareInBoard, squareImages);
        }
        drawer.draw(squareImagesBoard);
    }
    
    private Square getSquareInBoard(Square square){
        return boardService.getBoard().getSquares().stream()
               .filter(s-> s.getFile() == square.getFile() && s.getRank() == square.getRank())
               .findFirst().get();
    }
    
    private List<SquareImage> getBoardUnmarked (){
        return  squareImagesBoard.stream()
                .map(square -> {square.setOption(false); square.setSelected(false); return square;  })
                .collect(Collectors.toList());
    }
    
    private Optional<SquareImage> getSquareToMove(Square squareInBoard){
        return squareImagesBoard.stream()
               .filter(sI-> sI.getFile() == squareInBoard.getFile() && 
                            sI.getRank() == squareInBoard.getRank() && 
                            sI.isOption() == true)
               .findAny();
    }
    
    private List<SquareImage> getSquarePieceSelected (Square squareInBoard){
        return boardService.getBoard().getSquares().stream()
             .map(s -> checkSelectedSquare(s, squareInBoard))
             .collect(Collectors.toList());
    }
    
    private SquareImage checkSelectedSquare(Square square, Square squareInBoard){
        if(square.equals(squareInBoard)){
             SquareImage si = toSquareImage(square);
             si.setSelected(true);
             return si;
         }
         return toSquareImage(square);
    }
    
    private List<SquareImage> setAllowedMovementsToSquares (Square squareInBoard, List<SquareImage> squareImages){
        List<Square> squaresAllowed = boardService.getAllowedMovements(squareInBoard);
        return squareImages.stream()
                .map(squareImage -> checkOptionSquare(squareImage, squaresAllowed))
                .collect(Collectors.toList());
    }
    
    private SquareImage checkOptionSquare (SquareImage squareImage, List<Square> squaresAllowed){
        Optional<Square> squareFound = squaresAllowed.stream()
                                       .filter(allowed -> allowed.equals(squareImage))
                                       .findAny();
        if (squareFound.isPresent()){
            squareImage.setOption(true);
        }
        return squareImage;
    }
    
    private Square getSquareByCoordinates (double x, double y){
        int rank = 8 - (int)(y/WIDTH);
        int fileLength = (int)(x/WIDTH);
        char file = (char)('a' + fileLength);
        Square square = new Square();
        square.setFile(file);
        square.setRank(rank);
        return square;
    }
    
    private SquareImage toSquareImage (Square square){
        SquareImage squareImage = new SquareImage();
        squareImage.setDarkColor(square.isDarkColor());
        squareImage.setFile(square.getFile());
        squareImage.setRank(square.getRank());
        squareImage.setOcuppied(square.isOcuppied());
        squareImage.setPiece(square.getPiece());
        return squareImage;
    }
    
    private List<SquareImage> toListSquareImage (List<Square> squares){
        return squares.stream().map(s -> toSquareImage(s)).collect(Collectors.toList());
    }
}