/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.chessfx.core.model.Board;
import org.chessfx.core.model.Square;
import org.chessfx.core.piece.Piece;
import org.chessfx.core.piece.Team;
import org.chessfx.core.piece.TypePiece;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author rafael
 */
@Component("BoardServiceImpl")
public class BoardServiceImpl implements BoardService {

    private Board board;
    private List<Board> historic;
    private List<Piece> deadPieces;
    private List<String> notations;
    
    @Autowired
    private MovementResolver resolver;

    @Override
    public void initBoard() {
        board = new Board();
        historic = new ArrayList<>();
        deadPieces = new ArrayList<>();
        notations = new ArrayList<>();
        List<Square> squares = SquareInitializer.initSquares();
        board.setSquares(squares);
    }

    @Override
    public Optional<Square> kingInCheck(Team team){
        resolver.setBoard(board);
        return resolver.kingInCheck(team);
    }
    
    @Override
    public void movePiece(Square from, Square to) {
        String notationMove = getNotationAfterPieceMoved(from, to);
        List<Square> squaresPieceMoved = getSquaresAfterPieceMoved(from, to);
        if (isEnPassantMovement(from, to)){
            List<Square> squaresEnPassant = getListSquaresEP(from, to, squaresPieceMoved);
            squaresPieceMoved = squaresEnPassant;
        }
        List <Square> squaresWithCastling = copyList(squaresPieceMoved);
        if (isCastlingMovement(from, to)){
            notationMove = getCastlingNotation(to);
            squaresWithCastling = getCastlingSquares(from, to, squaresPieceMoved);
        }
        board.setSquares(squaresWithCastling);
        Board copyBoard = new Board();
        copyBoard.setSquares(copyList(squaresWithCastling));
        historic.add(copyBoard);
        notations.add(notationMove);
    }

    @Override
    public boolean isPiecePromoted(Square square){
        if (!square.isOcuppied() || square.getPiece().getType() != TypePiece.PAWN){
            return false;
        }
        Team team = square.getPiece().getTeam();
        if (team == Team.BLACK){
            return square.getRank() == 1;
        }
        return square.getRank() == 8;
    }
    
    @Override
    public void promotedPiece (Square square, Piece piece){
        List<Square> squaresWithPiecePromoted = board.getSquares().stream()
                     .map(s -> getSquaresWithPiecePromoted(s, square, piece))
                     .collect(Collectors.toList());
        board.setSquares(squaresWithPiecePromoted);
        Board copyBoard = new Board();
        copyBoard.setSquares(copyList(squaresWithPiecePromoted));
        historic.remove(historic.size() - 1);
        historic.add(copyBoard);
        String lastNotation = notations.get(notations.size()-1);
        lastNotation += "=" + piece.getType().getPieceLetter();
        notations.set(notations.size()-1, lastNotation);
    }
    
    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public List<Piece> getDeadPieces(){
        return copyList(deadPieces);
    }
    
    @Override
    public List<String> getNotations(){
        return copyList(notations);
    }
    
    @Override
    public List<Square> getAllowedMovements (Square square){
        Team teamPlayer = square.getPiece().getTeam();
        boolean isWhiteSelected = teamPlayer == Team.WHITE;
        int numberOfMoves = historic.size();
        boolean isWhiteToMove = (numberOfMoves % 2 == 0);
        if((isWhiteSelected && isWhiteToMove) || (!isWhiteSelected && !isWhiteToMove) ){
            resolver.setBoard(board);
            resolver.setHistoricBoards(historic);
            List<Square> movements = resolver.getAllowedMovements(square);
            return resolver.getAllowedMovementsWithoutCheck(movements, square);
        }
        return new ArrayList<>();
    }

    private Square getSquaresWithPiecedMoved(Square from, Square to, Square s) {
        if (from.equals(s)) {
            return new Square(from.getRank(), from.getFile(), false, from.isDarkColor());
        }
        if (to.equals(s)) {
            if (to.isOcuppied()){
                addDeadPiece(to.getPiece());
            }
            Piece fromPiece = from.getPiece();
            Piece piece = new Piece(fromPiece.getTeam(), fromPiece.getType(), true, false);
            return new Square(to.getRank(), to.getFile(), true, to.isDarkColor(), piece);
        }
        return s;
    }
    
    private List<Square> getSquaresAfterPieceMoved (Square from, Square to){
        return board.getSquares().stream()
            .map(s -> getSquaresWithPiecedMoved(from, to, s))
            .collect(Collectors.toList());
    }
    
    private Square getSquaresEnPassant(Square from, Square to, Square s){
        if (from.getRank() == s.getRank() && to.getFile() == s.getFile()){
            if (s.isOcuppied()){
                addDeadPiece(s.getPiece());
            }
            return new Square(s.getRank(), s.getFile(), false, s.isDarkColor());
        }
        return s;
    }
    
    private List<Square> getListSquaresEP(Square from, Square to, List<Square> squaresWithPieceMoved){
        return squaresWithPieceMoved.stream()
                    .map(s -> getSquaresEnPassant(from, to, s))
                    .collect(Collectors.toList());
    }
    
    private List<Square> getCastlingSquares (Square from, Square to, List<Square> squaresWithPiecedMoved){
        char rookFile = (to.getFile() - from.getFile()) == 2 ? 'h' : 'a';
        char nextRookFile = rookFile == 'h' ? 'f' : 'd';
        Square rookSquare = squaresWithPiecedMoved.stream()
                .filter(s -> s.getRank() == from.getRank() && s.getFile() == rookFile).findFirst().get();
        Square nextRookSquare = squaresWithPiecedMoved.stream()
                .filter(s -> s.getRank() == from.getRank() && s.getFile() == nextRookFile).findFirst().get();
        
        List<Square> squaresWithCastling = squaresWithPiecedMoved.stream()
                .map(s -> getSquaresWithPiecedMoved(rookSquare, nextRookSquare, s))
                .collect(Collectors.toList());
        return squaresWithCastling;
    }
    
    private boolean isCastlingMovement(Square from, Square to){
        if (from.getPiece().getType() != TypePiece.KING){
            return false;
        }
        if (Math.abs(to.getFile() - from.getFile()) != 2){
            return false;
        }
        return true;
    }
    
    private Square getSquaresWithPiecePromoted(Square current, Square square, Piece piece){
        if (current.equals(square)){
            return new Square(current.getRank(), current.getFile(), true, current.isDarkColor(), piece);
        }
        return current;
    }
    
    private boolean isEnPassantMovement(Square from, Square to){
        if (from.getPiece().getType() != TypePiece.PAWN){
            return false;
        }
        if (to.isOcuppied()){
            return false;
        }
        if (from.getRank() != to.getRank() + 1 && from.getRank() != to.getRank() - 1){
            return false;
        }
        if (from.getFile() != to.getFile() + 1 && from.getFile() != to.getFile() - 1){
            return false;
        }
        return true;
    }
    
    private String getNotationAfterPieceMoved (Square from, Square to){
        String notationMove = from.getPiece().getType().getPieceLetter();
        if (to.isOcuppied()){
            if (from.isOcuppied() && from.getPiece().getType() == TypePiece.PAWN){
                notationMove += from.getFile();
            }
            notationMove += "x";
        }
        notationMove += to.getFile() + Integer.toString(to.getRank());
        if (isEnPassantMovement(from, to)){
            notationMove = from.getFile() + "x";
            notationMove += to.getFile() + Integer.toString(to.getRank()) + "e.p.";
        }
        return notationMove;
    }
    
    private String getCastlingNotation(Square to){
        String notationMove = "O-O";
        if (to.getFile() == 'c'){
            return "O-O-O";
        }
        return notationMove;
    }
    
    private void addDeadPiece(Piece piece){
        Piece deadPiece = new Piece(piece.getTeam(), piece.getType(), false, false);
        deadPieces.add(deadPiece);
    }
    
    private List copyList (List list){
        return (List) list.stream()
                .map(e -> e)
                .collect(Collectors.toList());
    }
}
