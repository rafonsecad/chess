/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
    
    @Autowired
    private MovementResolver resolver;

    @Override
    public void initBoard() {
        board = new Board();
        historic = new ArrayList<>();
        deadPieces = new ArrayList<>();
        List<Square> squares = new ArrayList<>();
        char[] files = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        int[] ranks = new int[]{8, 7, 6, 5, 4, 3, 2, 1};
        IntStream rankStream = Arrays.stream(ranks);
        rankStream.forEach(r -> {
            IntStream.range(0, files.length).mapToObj(i -> files[i]).forEach(f -> {
                squares.add(initPieces(r, f));
            });
        });
        board.setSquares(squares);
    }

    @Override
    public Optional<Square> kingInCheck(Team team){
        resolver.setBoard(board);
        return resolver.kingInCheck(team);
    }
    
    @Override
    public void movePiece(Square from, Square to) {
        List<Square> squaresWithPiecedMoved = board.getSquares().stream()
                .map(s -> getSquaresWithPiecedMoved(from, to, s))
                .collect(Collectors.toList());
        List <Square> squaresWithCastling = squaresWithPiecedMoved.stream()
                .map(s->s)
                .collect(Collectors.toList());
        if (isCastlingMovement(from, to)){
            squaresWithCastling = getCastlingSquares(from, to, squaresWithPiecedMoved);
        }
        board.setSquares(squaresWithCastling);
        Board copyBoard = new Board();
        copyBoard.setSquares(squaresWithCastling.stream().map(s->s).collect(Collectors.toList()));
        historic.add(copyBoard);
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
        copyBoard.setSquares(squaresWithPiecePromoted.stream().map(s->s).collect(Collectors.toList()));
        historic.remove(historic.size() - 1);
        historic.add(copyBoard);
    }
    
    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public List<Piece> getDeadPieces(){
        return deadPieces.stream()
                .map(p -> p)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Square> getAllowedMovements (Square square){
        Team teamPlayer = square.getPiece().getTeam();
        boolean isWhiteSelected = teamPlayer == Team.WHITE;
        int numberOfMoves = historic.size();
        boolean isWhiteToMove = (numberOfMoves % 2 == 0);
        if((isWhiteSelected && isWhiteToMove) || (!isWhiteSelected && !isWhiteToMove) ){
            resolver.setBoard(board);
            List<Square> movements = resolver.getAllowedMovements(square);
            return resolver.getAllowedMovementsWithoutCheck(movements, square);
        }
        return new ArrayList<>();
    }
    
    private Square initPieces(int rank, char file) {
        boolean darkColor = isDarkSquare(rank, file);

        if (rank != 1 && rank != 2 && rank != 7 && rank != 8){
            return new Square(rank, file, false, darkColor);
        }
        
        Team team = getTeamByRank (rank);
        TypePiece type = TypePiece.PAWN;
        
        if (rank == 2 || rank == 7){
            Piece pawn = new Piece(team, type, true, true);
            return new Square(rank, file, true, darkColor, pawn);
        }
        type = getTypePieceByFile(file);
        Piece piece = new Piece(team, type, true, true);
        return new Square(rank, file, true, darkColor, piece);
    }

    private boolean isDarkSquare(int rank, char file) {
        if ((rank + 1) % 2 == 0) {
            return file % 2 != 0;
        }
        return file % 2 == 0;
    }

    private Team getTeamByRank (int rank){
        if (rank == 1 || rank == 2){
            return Team.WHITE;
        }
        return Team.BLACK;
    }
    
    private TypePiece getTypePieceByFile(char file){
        TypePiece type = TypePiece.PAWN;
        switch (file){
            case 'a': 
                type = TypePiece.ROOK; 
                break;
            case 'b': 
                type = TypePiece.KNIGHT; 
                break;
            case 'c': 
                type = TypePiece.BISHOP; 
                break;
            case 'd': 
                type = TypePiece.QUEEN; 
                break;
            case 'e': 
                type = TypePiece.KING; 
                break;
            case 'f': 
                type = TypePiece.BISHOP; 
                break;
            case 'g': 
                type = TypePiece.KNIGHT; 
                break;
            case 'h': 
                type = TypePiece.ROOK; 
                break;
        }
        return type;
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
    
    private void addDeadPiece(Piece piece){
        Piece deadPiece = new Piece(piece.getTeam(), piece.getType(), false, false);
        deadPieces.add(deadPiece);
    }
}
