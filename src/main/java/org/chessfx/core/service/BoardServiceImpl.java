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

/**
 *
 * @author rafael
 */
public class BoardServiceImpl implements BoardService{

    private Board board;
    
    @Override
    public void initBoard() {
        this.board = new Board();
        List<Square> squares = new ArrayList<>();
        char [] files = new char[] {'a', 'b', 'c', 'd', 'f', 'g', 'h', 'i'};
        int [] ranks = new int [] {1, 2, 3, 4, 5, 6, 7, 8};
        Stream<Character> fileStream = IntStream.range(0, files.length).mapToObj(i -> files[i]);
        IntStream rankStream = Arrays.stream(ranks);
        rankStream.forEach(r -> {fileStream.forEach(f -> {squares.add(new Square(r, f));});});
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private Square getSquaresWithPiecedMoved(Square from, Square to, Square s){
        if(from.equals(s)){
            return new Square(from.getRank(), from.getFile(), false);
        }
        if (to.equals(s)){
            return new Square(to.getRank(), to.getFile(), true);
        }
        return s;
    }
}
