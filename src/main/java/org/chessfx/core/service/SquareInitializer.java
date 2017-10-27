/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import org.chessfx.core.model.Square;
import org.chessfx.core.piece.Piece;
import org.chessfx.core.piece.Team;
import org.chessfx.core.piece.TypePiece;

/**
 *
 * @author rafael
 */
public class SquareInitializer {
    public static List<Square> initSquares(){
        List<Square> squares = new ArrayList<>();
        char[] files = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        int[] ranks = new int[]{8, 7, 6, 5, 4, 3, 2, 1};
        IntStream rankStream = Arrays.stream(ranks);
        rankStream.forEach(r -> {
            IntStream.range(0, files.length).mapToObj(i -> files[i]).forEach(f -> {
                squares.add(initSquare(r, f));
            });
        });
        return squares;
    }
    
    private static Square initSquare (int rank, char file){
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
    
    private static boolean isDarkSquare(int rank, char file) {
        if ((rank + 1) % 2 == 0) {
            return file % 2 != 0;
        }
        return file % 2 == 0;
    }

    private static Team getTeamByRank (int rank){
        if (rank == 1 || rank == 2){
            return Team.WHITE;
        }
        return Team.BLACK;
    }
    
    private static TypePiece getTypePieceByFile(char file){
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
}
