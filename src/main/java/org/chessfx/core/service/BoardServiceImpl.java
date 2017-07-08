/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.chessfx.core.model.Board;
import org.chessfx.core.model.Square;
import org.chessfx.core.piece.Piece;
import org.chessfx.core.piece.Team;
import org.chessfx.core.piece.TypePiece;
import org.springframework.stereotype.Component;

/**
 *
 * @author rafael
 */
@Component
public class BoardServiceImpl implements BoardService {

    private Board board;

    @Override
    public void initBoard() {
        this.board = new Board();
        List<Square> squares = new ArrayList<>();
        char[] files = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        int[] ranks = new int[]{8, 7, 6, 5, 4, 3, 2, 1};
        IntStream rankStream = Arrays.stream(ranks);
        rankStream.forEach(r -> {
            IntStream.range(0, files.length).mapToObj(i -> files[i]).forEach(f -> {
                squares.add(initPieces(r, f));
            });
        });
        this.board.setSquares(squares);
    }

    @Override
    public void movePiece(Square from, Square to) {
        List<Square> squaresWithPiecedMoved = board.getSquares().stream()
                .map(s -> getSquaresWithPiecedMoved(from, to, s))
                .collect(Collectors.toList());
        board.setSquares(squaresWithPiecedMoved);
    }

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public List<Square> getAllowedMovements (Square square){
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
            Piece pawn = new Piece(team, type, true);
            return new Square(rank, file, true, darkColor, pawn);
        }
        type = getTypePieceByFile(file);
        Piece piece = new Piece(team, type, true);
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
            return new Square(from.getRank(), from.getFile(), from.isDarkColor(), false);
        }
        if (to.equals(s)) {
            return new Square(to.getRank(), to.getFile(), from.isDarkColor(), true);
        }
        return s;
    }
}
