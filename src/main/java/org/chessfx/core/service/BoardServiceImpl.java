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
import java.util.stream.Stream;
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

    private Square initPieces(int rank, char file) {
        boolean darkColor = isDarkSquare(rank, file);

        if (rank == 2) {
            Piece whitePawn = new Piece();
            whitePawn.setTeam(Team.WHITE);
            whitePawn.setType(TypePiece.PAWN);
            whitePawn.setActive(true);
            return new Square(rank, file, true, darkColor, whitePawn);
        }
        if (rank == 7) {
            Piece blackPawn = new Piece();
            blackPawn.setTeam(Team.BLACK);
            blackPawn.setType(TypePiece.PAWN);
            blackPawn.setActive(true);
            return new Square(rank, file, true, darkColor, blackPawn);
        }
        return new Square(rank, file, false, darkColor);
    }

    private boolean isDarkSquare(int rank, char file) {
        if ((rank + 1) % 2 == 0) {
            return file % 2 != 0;
        }
        return file % 2 == 0;
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
