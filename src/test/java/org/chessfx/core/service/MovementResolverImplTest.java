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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rafael
 */
public class MovementResolverImplTest {
    
    public Board board;
    
    public MovementResolverImplTest() {
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
    
    public Square initPieces(int rank, char file){
        boolean darkColor = isDarkSquare(rank, file);
        return new Square(rank, file, false, darkColor);
    }
    
    private boolean isDarkSquare(int rank, char file) {
        if ((rank + 1) % 2 == 0) {
            return file % 2 != 0;
        }
        return file % 2 == 0;
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGetAllowedMovements_PawnE2_FirstMovement() {
        Square square = this.board.getSquares().stream().filter(s -> s.getFile() == 'e' && s.getRank() == 2).findFirst().get();
        Piece piece = new Piece(Team.WHITE, TypePiece.PAWN, true);
        piece.setFirstMovement(true);
        square.setPiece(piece);
        MovementResolverImpl instance = new MovementResolverImpl();
        instance.setBoard(board);
        Square s1 = this.board.getSquares().stream().filter(s -> s.getFile() == 'e' && s.getRank() == 3).findFirst().get();
        Square s2 = this.board.getSquares().stream().filter(s -> s.getFile() == 'e' && s.getRank() == 4).findFirst().get();
        List<Square> expResult = Stream.of(s1, s2).collect(Collectors.toList());
        List<Square> result = instance.getAllowedMovements(square);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetAllowedMovements_PawnE3_NOTFirstMovement() {
        Square square = this.board.getSquares().stream().filter(s -> s.getFile() == 'e' && s.getRank() == 3).findFirst().get();
        Piece piece = new Piece(Team.WHITE, TypePiece.PAWN, true);
        piece.setFirstMovement(false);
        square.setPiece(piece);
        MovementResolverImpl instance = new MovementResolverImpl();
        instance.setBoard(board);
        Square s1 = this.board.getSquares().stream().filter(s -> s.getFile() == 'e' && s.getRank() == 4).findFirst().get();
        List<Square> expResult = Stream.of(s1).collect(Collectors.toList());
        List<Square> result = instance.getAllowedMovements(square);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetAllowedMovements_PawnB5_NOTFirstMovement() {
        Square square = this.board.getSquares().stream().filter(s -> s.getFile() == 'b' && s.getRank() == 5).findFirst().get();
        Piece piece = new Piece(Team.WHITE, TypePiece.PAWN, true);
        piece.setFirstMovement(false);
        square.setPiece(piece);
        MovementResolverImpl instance = new MovementResolverImpl();
        instance.setBoard(board);
        Square s1 = this.board.getSquares().stream().filter(s -> s.getFile() == 'b' && s.getRank() == 6).findFirst().get();
        List<Square> expResult = Stream.of(s1).collect(Collectors.toList());
        List<Square> result = instance.getAllowedMovements(square);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetAllowedMovements_PawnB2_FirstMovement() {
        Square square = this.board.getSquares().stream().filter(s -> s.getFile() == 'b' && s.getRank() == 2).findFirst().get();
        Piece piece = new Piece(Team.WHITE, TypePiece.PAWN, true);
        piece.setFirstMovement(true);
        square.setPiece(piece);
        MovementResolverImpl instance = new MovementResolverImpl();
        instance.setBoard(board);
        Square s1 = this.board.getSquares().stream().filter(s -> s.getFile() == 'b' && s.getRank() == 3).findFirst().get();
        Square s2 = this.board.getSquares().stream().filter(s -> s.getFile() == 'b' && s.getRank() == 4).findFirst().get();
        List<Square> expResult = Stream.of(s1, s2).collect(Collectors.toList());
        List<Square> result = instance.getAllowedMovements(square);
        assertEquals(expResult, result);
    }
}
