/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.core.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.imageio.ImageIO;
import org.chessfx.core.piece.Piece;
import org.chessfx.core.piece.Team;
import org.chessfx.core.piece.TypePiece;
import org.springframework.stereotype.Component;

/**
 *
 * @author rafael
 */
@Component
public class GraphicsServiceImpl implements GraphicsService {

    private final String CONFIG_FILE = "/pieces.properties";
    private Properties properties;
    private InputStream input;
    private Map<Piece, BufferedImage> repository;

    public GraphicsServiceImpl() {
        properties = new Properties();
        repository = new HashMap<>();
        openConfigFile();
        loadImages();
    }

    @Override
    public BufferedImage getImage(Piece piece) {
        return repository.get(piece);
    }

    private void openConfigFile() {
        try {
            input = getClass().getResourceAsStream(CONFIG_FILE);
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadImages() {
        try {
            extractImages();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void extractImages() throws IOException{
        String imagesFile = properties.getProperty("pieces.file");
        InputStream inputImages = getClass().getResourceAsStream(imagesFile);
        BufferedImage image = ImageIO.read(inputImages);

        for (Team team : Team.values()) {
            for (TypePiece type : TypePiece.values()) {
                String property = team.toString() + "." + type.toString();
                int[] coor = this.getCoordinates(property);
                Piece piece = new Piece(team, type);
                BufferedImage pieceImage = image.getSubimage(coor[0], coor[1], coor[2], coor[3]);
                repository.put(piece, pieceImage);
            }
        }
    }

    private int[] getCoordinates(String property) {
        String coordinatesLine = properties.getProperty(property);
        String[] coordinates = coordinatesLine.split(",");
        int[] coor = new int[4];
        for (int index = 0; index < coordinates.length; index++) {
            coor[index] = Integer.parseInt(coordinates[index]);
        }
        return coor;
    }
}
