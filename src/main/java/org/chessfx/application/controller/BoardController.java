/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.application.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.chessfx.core.model.Square;
import org.chessfx.core.piece.Piece;
import org.chessfx.core.piece.Team;
import org.chessfx.core.service.BoardService;
import org.chessfx.application.ChessBoardDrawer;
import static org.chessfx.application.adapter.SquareImageAdapter.toListSquareImage;
import static org.chessfx.application.adapter.SquareImageAdapter.toSquareImage;
import org.chessfx.application.model.ChessBoard;
import org.chessfx.application.model.SquareImage;
import org.springframework.stereotype.Component;

/**
 *
 * @author rafael
 */
@Component("BoardController")
public class BoardController implements EventHandler<MouseEvent> {

    final private BoardService boardService;

    private ChessBoardDrawer drawer;
    private final int WIDTH = 80;
    private ChessBoard chessBoard;
    private PromotionController promotionController;

    public BoardController (BoardService boardService){
        this.boardService = boardService;
    }
    
    public void init(ChessBoardDrawer drawer) {
        this.drawer = drawer;
        boardService.initBoard();
        chessBoard = new ChessBoard();
        List<Square> squares = boardService.getBoard().getSquares();
        chessBoard.setSquareImages(toListSquareImage(squares));
        promotionController = new PromotionController(boardService, chessBoard);
        this.drawer.draw(chessBoard);
    }

    @Override
    public void handle(MouseEvent event) {
        if (chessBoard.hasPromotion()){
            PromotePiece(event);
            return;
        }
        Optional<Square> square = getSquareByCoordinates(event.getX(), event.getY());
        if (!square.isPresent()) {
            return;
        }
        List<SquareImage> squareImages = handleChessBoardRequests(square);
        updateView(squareImages);
    }
    
    private void PromotePiece (MouseEvent event){
        Optional<Piece> piecePromoted; 
        piecePromoted = promotionController.getPiecePromoted(event.getX(), event.getY());
        List<SquareImage> squareImages = promotionController.getPromotionSquares(piecePromoted);
        if (!squareImages.isEmpty()){
            updateView(squareImages);
        }
    }

    private void updateView(List<SquareImage> squareImages){
        chessBoard.setDeadPieces(boardService.getDeadPieces());
        chessBoard.setSquareImages(getSquareImagesWithCheck(squareImages));
        chessBoard.setNotations(boardService.getNotations());
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
        Square squareWithPiece = new Square(squareToMove.getRank(), 
                squareToMove.getFile(), 
                true, 
                squareToMove.isDarkColor(), 
                squareSelected.getPiece());
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
}
