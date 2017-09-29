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
import org.chessfx.core.piece.Piece;
import org.chessfx.core.piece.Team;
import org.chessfx.core.piece.TypePiece;
import org.chessfx.core.service.BoardService;
import org.chessfx.view.ChessBoardDrawer;
import org.chessfx.view.model.ChessBoard;
import org.chessfx.view.model.SquareImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author rafael
 */
@Component("BoardController")
public class BoardController implements EventHandler<MouseEvent> {

    @Autowired
    private BoardService boardService;

    private ChessBoardDrawer drawer;
    private final int WIDTH = 80;
    private ChessBoard chessBoard;

    public void init(ChessBoardDrawer drawer) {
        this.drawer = drawer;
        boardService.initBoard();
        chessBoard = new ChessBoard();
        List<Square> squares = boardService.getBoard().getSquares();
        chessBoard.setSquareImages(toListSquareImage(squares));
        this.drawer.draw(chessBoard);
    }

    @Override
    public void handle(MouseEvent event) {
        if (chessBoard.hasPromotion()){
            Optional<Piece> piecePromoted = getPiecePromoted(event.getX(), event.getY());
            promotePiece(piecePromoted);
            return;
        }
        Optional<Square> square = getSquareByCoordinates(event.getX(), event.getY());
        if (!square.isPresent()) {
            return;
        }
        List<SquareImage> squaresImages = handleChessBoardRequests(square);
        chessBoard.setDeadPieces(boardService.getDeadPieces());
        chessBoard.setSquareImages(getSquareImagesWithCheck(squaresImages));
        drawer.draw(chessBoard);
    }
    
    private void promotePiece (Optional<Piece> piece){
        if (!piece.isPresent()){
            return;
        }
        Piece fullPiece = new Piece (chessBoard.getTeamPromoted(), piece.get().getType());
        boardService.promotedPiece(chessBoard.getSquarePromoted(), fullPiece);
        chessBoard.setPromotion(false);
        List<Square> squares = boardService.getBoard().getSquares();
        List<SquareImage> squaresImages = toListSquareImage(squares);
        chessBoard.setSquareImages(getSquareImagesWithCheck(squaresImages));
        drawer.draw(chessBoard);
    }

    private List<SquareImage> handleChessBoardRequests(Optional<Square> square){
        List<SquareImage> squareImagesBoard = chessBoard.getSquareImages();
        Square squareInBoard = getSquareInBoard(square.get());
        Optional<SquareImage> squareSelected = squareImagesBoard.stream().filter(sI -> sI.isSelected() == true).findAny();
        Optional<SquareImage> squareToMove = getSquareToMove(squareInBoard);
        List<SquareImage> squaresImages = squareImagesBoard.stream().map(s -> s).collect(Collectors.toList());
        
        if (squareSelected.isPresent() && squareSelected.get().equals(squareInBoard)) {
            squaresImages = getBoardUnmarked();
        } else if (squareToMove.isPresent()) {
            squaresImages = MovePiece(squareSelected.get(), squareToMove.get());
        } else if (squareInBoard.isOcuppied()) {
            List<SquareImage> squareImages = getSquarePieceSelected(squareInBoard);
            squaresImages = setAllowedMovementsToSquares(squareInBoard, squareImages);
        }
        return squaresImages;
    }
    
    private List<SquareImage> MovePiece(Square squareSelected, Square squareToMove) {
        boardService.movePiece(squareSelected, squareToMove);
        List<Square> squares = boardService.getBoard().getSquares();
        List<SquareImage> squaresImages = toListSquareImage(squares);
        Square squareWithPiece = new Square(squareToMove.getRank(), squareToMove.getFile(), true, squareToMove.isDarkColor(), squareSelected.getPiece());
        boolean promotionFlag = boardService.isPiecePromoted(squareWithPiece);
        Team teamPromoted = squareWithPiece.getPiece().getTeam();
        chessBoard.setPromotion(promotionFlag);
        chessBoard.setTeamPromoted(teamPromoted);
        chessBoard.setSquarePromoted(squareWithPiece);
        return squaresImages;
    }

    private Square getSquareInBoard(Square square) {
        return boardService.getBoard().getSquares().stream()
                .filter(s -> s.getFile() == square.getFile() && s.getRank() == square.getRank())
                .findFirst().get();
    }

    private List<SquareImage> getBoardUnmarked() {
        return chessBoard.getSquareImages().stream()
                .map(square -> {
                    square.setOption(false);
                    square.setSelected(false);
                    return square;
                })
                .collect(Collectors.toList());
    }

    private Optional<SquareImage> getSquareToMove(Square squareInBoard) {
        return chessBoard.getSquareImages().stream()
                .filter(sI -> sI.getFile() == squareInBoard.getFile()
                && sI.getRank() == squareInBoard.getRank()
                && sI.isOption() == true)
                .findAny();
    }

    private List<SquareImage> getSquarePieceSelected(Square squareInBoard) {
        return boardService.getBoard().getSquares().stream()
                .map(s -> checkSelectedSquare(s, squareInBoard))
                .collect(Collectors.toList());
    }

    private SquareImage checkSelectedSquare(Square square, Square squareInBoard) {
        if (square.equals(squareInBoard)) {
            SquareImage si = toSquareImage(square);
            si.setSelected(true);
            return si;
        }
        return toSquareImage(square);
    }

    private List<SquareImage> setAllowedMovementsToSquares(Square squareInBoard, List<SquareImage> squareImages) {
        List<Square> squaresAllowed = boardService.getAllowedMovements(squareInBoard);
        return squareImages.stream()
                .map(squareImage -> checkOptionSquare(squareImage, squaresAllowed))
                .collect(Collectors.toList());
    }

    private SquareImage checkOptionSquare(SquareImage squareImage, List<Square> squaresAllowed) {
        Optional<Square> squareFound = squaresAllowed.stream()
                .filter(allowed -> allowed.equals(squareImage))
                .findAny();
        if (squareFound.isPresent()) {
            squareImage.setOption(true);
        }
        return squareImage;
    }

    private Optional<Square> getSquareByCoordinates(double x, double y) {
        if (x < 2 * WIDTH || 10 * WIDTH < x) {
            return Optional.empty();
        }
        x = x - 2 * WIDTH;
        int rank = 8 - (int) (y / WIDTH);
        int fileLength = (int) (x / WIDTH);
        char file = (char) ('a' + fileLength);
        Square square = new Square();
        square.setFile(file);
        square.setRank(rank);
        return Optional.of(square);
    }

    private Optional<Piece> getPiecePromoted(double x, double y){
        if (3.5*WIDTH > y || 4.5*WIDTH < y){
            return Optional.empty();
        }
        double [] xMins = new double [4];
        double [] xMaxs = new double [4];
        for (int index=0; index < xMins.length; index++){
            xMins[index] = 3.7*WIDTH + WIDTH*index + index*0.2*WIDTH;
            xMaxs[index] = xMins[index] + WIDTH;
            if (xMins[index] <= x && xMaxs[index] >= x){
                return selectPiecePromoted(index);
            }
        }
        return Optional.empty();
    }
    
    private Optional<Piece> selectPiecePromoted(int index){
        Piece piece = new Piece(Team.WHITE, TypePiece.QUEEN);
        switch(index){
            case 0:
                piece = new Piece(Team.WHITE, TypePiece.QUEEN);
                return Optional.of(piece);
            case 1:
                piece = new Piece(Team.WHITE, TypePiece.ROOK);
                return Optional.of(piece);
            case 2:
                piece = new Piece(Team.WHITE, TypePiece.BISHOP);
                return Optional.of(piece);
            case 3:
                piece = new Piece(Team.WHITE, TypePiece.KNIGHT);
                return Optional.of(piece);
            default:
                return Optional.of(piece);
        }
    }
    
    private List<SquareImage> getSquareImagesWithCheck(List<SquareImage> squares) {
        final Optional<Square> checkSquareWhite = boardService.kingInCheck(Team.WHITE);
        final Optional<Square> checkSquareBlack = boardService.kingInCheck(Team.BLACK);

        if (checkSquareWhite.isPresent() || checkSquareBlack.isPresent()) {
            final Optional<Square> checkSquare = checkSquareWhite.isPresent() ? checkSquareWhite : checkSquareBlack;
            return squares.stream()
                    .map(s -> setCheckSquare(s, checkSquare))
                    .collect(Collectors.toList());
        }
        return squares;
    }

    private SquareImage setCheckSquare(SquareImage square, Optional<Square> checkSquare) {
        if (square.equals(checkSquare.get())) {
            square.setCheck(true);
            return square;
        }
        return square;
    }

    private SquareImage toSquareImage(Square square) {
        SquareImage squareImage = new SquareImage();
        squareImage.setDarkColor(square.isDarkColor());
        squareImage.setFile(square.getFile());
        squareImage.setRank(square.getRank());
        squareImage.setOcuppied(square.isOcuppied());
        squareImage.setPiece(square.getPiece());
        return squareImage;
    }

    private List<SquareImage> toListSquareImage(List<Square> squares) {
        return squares.stream().map(s -> toSquareImage(s)).collect(Collectors.toList());
    }
}
