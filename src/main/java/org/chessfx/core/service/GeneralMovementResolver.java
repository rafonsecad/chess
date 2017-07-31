/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.chessfx.core.model.Board;
import org.chessfx.core.model.Square;

/**
 *
 * @author rafael
 */
public class GeneralMovementResolver {
    
    private Board board;
    
    public void setBoard(Board board){
        this.board = board;
    }
    
    public List<Square> getQueenMovements(Square selected){
        List<Square> rookMovements = getRookMovements(selected);
        List<Square> bishopMovements = getBishopMovements(selected);
        return Stream.concat(rookMovements.stream(), bishopMovements.stream()).collect(Collectors.toList());
    }
    
    public List<Square> getBishopMovements (Square selected){
        List<Square> movements = this.board.getSquares().stream()
                                 .filter(square -> isBishopMovement(square, selected))
                                 .collect(Collectors.toList());
        List<Square> NEMovements = movements.stream()
                                   .filter(square -> square.getRank() > selected.getRank() && square.getFile() > selected.getFile())
                                   .sorted(Comparator.comparing(Square::getRank))
                                   .collect(Collectors.toList());
        List<Square> SEMovements = movements.stream()
                                   .filter(square -> square.getRank() < selected.getRank() && square.getFile() > selected.getFile())
                                   .sorted(Comparator.comparing(Square::getFile))
                                   .collect(Collectors.toList());
        List<Square> SWMovements = movements.stream()
                                   .filter(square -> square.getRank() < selected.getRank() && square.getFile() < selected.getFile())
                                   .sorted(Comparator.comparing(Square::getFile).reversed())
                                   .collect(Collectors.toList());
        List<Square> NWMovements = movements.stream()
                                   .filter(square -> square.getRank() > selected.getRank() && square.getFile() < selected.getFile())
                                   .sorted(Comparator.comparing(Square::getFile).reversed())
                                   .collect(Collectors.toList());
        List<Square> NEMovementsWithObstacles = getMovementsWithObstacles(NEMovements, selected);
        List<Square> SEMovementsWithObstacles = getMovementsWithObstacles(SEMovements, selected);
        List<Square> SWMovementsWithObstacles = getMovementsWithObstacles(SWMovements, selected);
        List<Square> NWMovementsWithObstacles = getMovementsWithObstacles(NWMovements, selected);
        List<Square> movementsWithObstacles = Stream.concat(
                                              Stream.concat(
                                              Stream.concat(NEMovementsWithObstacles.stream(), 
                                                            SEMovementsWithObstacles.stream()),
                                                            SWMovementsWithObstacles.stream()),
                                                            NWMovementsWithObstacles.stream())
                                               .collect(Collectors.toList());
        return movementsWithObstacles;
    }
    
    private boolean isBishopMovement(Square square, Square selected){
        if (square.getRank() == selected.getRank() && square.getFile() == selected.getFile()){
            return false;
        }
        int rankDistance = Math.abs(square.getRank() - selected.getRank());
        int fileDistance = Math.abs(square.getFile() - selected.getFile());
        
        return rankDistance == fileDistance;
    }
    
    public List<Square> getRookMovements (Square selected){
        List<Square> movements = this.board.getSquares().stream()
                                 .filter(square -> isRookMovement(square, selected))
                                 .collect(Collectors.toList());
        List<Square> northernMovements = movements.stream()
                                        .filter(square -> square.getRank() > selected.getRank())
                                        .sorted(Comparator.comparing(Square::getRank))
                                        .collect(Collectors.toList());
        List<Square> easternMovements = movements.stream()
                                        .filter(square -> square.getFile() > selected.getFile())
                                        .sorted(Comparator.comparing(Square::getFile))
                                        .collect(Collectors.toList());
        List<Square> southernMovements = movements.stream()
                                        .filter(square -> square.getRank() < selected.getRank())
                                        .sorted(Comparator.comparing(Square::getRank).reversed())
                                        .collect(Collectors.toList());
        List<Square> westernMovements = movements.stream()
                                        .filter(square -> square.getFile() < selected.getFile())
                                        .sorted(Comparator.comparing(Square::getFile).reversed())
                                        .collect(Collectors.toList());
        List<Square> northenMovementsWithObstacles = getMovementsWithObstacles(northernMovements, selected);
        List<Square> easternMovementsWithObstacles = getMovementsWithObstacles(easternMovements, selected);
        List<Square> southernMovementsWithObstacles = getMovementsWithObstacles(southernMovements, selected);
        List<Square> westernMovementsWithObstacles = getMovementsWithObstacles(westernMovements, selected);
        
        List<Square> movementsWithObstacles = Stream.concat(
                                              Stream.concat(
                                              Stream.concat(northenMovementsWithObstacles.stream(), 
                                                            easternMovementsWithObstacles.stream()),
                                                            southernMovementsWithObstacles.stream()),
                                                            westernMovementsWithObstacles.stream())
                                               .collect(Collectors.toList());
        return movementsWithObstacles;
    }
    
    private boolean isRookMovement (Square square, Square selected){
        if (square.getRank() == selected.getRank() && square.getFile() == selected.getFile()){
            return false;
        }
        if(square.getRank() == selected.getRank()){
            return true;
        }
        if(square.getFile() == selected.getFile()){
            return true;
        }
        return false;
    }
    
    private List<Square> getMovementsWithObstacles(List<Square> movements, Square selected){
        List<Square> movementsWithObstacles = new ArrayList<>();
        for(Square square: movements){
            if(square.isOcuppied()){
                if (square.getPiece().getTeam() != selected.getPiece().getTeam()){
                    movementsWithObstacles.add(square);
                }
                break;
            }
            movementsWithObstacles.add(square);
        }
        return movementsWithObstacles;
    }
}
