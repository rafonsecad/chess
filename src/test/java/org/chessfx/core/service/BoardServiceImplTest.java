/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.service;

import java.util.Collections;
import java.util.List;
import org.chessfx.application.MainApp;
import org.chessfx.core.model.Square;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 *
 * @author rafael
 */
public class BoardServiceImplTest {
    
    public BoardServiceImplTest() {
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
    public void allowedMovements_Players() {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(MainApp.class);
        BoardServiceImpl service = (BoardServiceImpl) context.getBean("BoardServiceImpl");
        service.initBoard();
        Square square = service.getBoard().getSquares().stream()
                        .filter(s -> s.getFile() == 'd' && s.getRank() == 7)
                        .findAny().get();
        List<Square> movements = service.getAllowedMovements(square);
        List<Square> expMovements = Collections.emptyList();
        assertEquals(expMovements, movements);
    }
    
}
