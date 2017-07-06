/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.service;

import java.awt.image.BufferedImage;
import org.chessfx.core.piece.Piece;

/**
 *
 * @author rafael
 */
public interface GraphicsService {
    
    BufferedImage getImage(Piece piece);
    
}
