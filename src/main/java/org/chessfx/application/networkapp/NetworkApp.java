/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.application.networkapp;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import org.chessfx.application.model.GameServer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author rafael
 */
public class NetworkApp {

    private final static String HTTP = "http://";
    private final static String HTTP_PORT = ":8080";
    private final static String REQUEST_GAME_ENDPOINT = "/api/server/requestgame";
    private final static String START_GAME_ENDPOINT = "/api/server/startgame";

    public static List<String> address = new ArrayList<>();
    private static String ipServer;
    private static String ipClient;
    private static ObservableList<GameServer> gameServers;
    private static UDPServer udpServer;
    private static UDPClient udpClient;
    private static BorderPane pane;
    private static ConfigurableApplicationContext context;

    public static void getApp(ConfigurableApplicationContext springContext, BorderPane appPane) {
        pane = appPane;
        context = springContext;
        TilePane tile = new TilePane();
        tile.setPadding(new Insets(5, 0, 5, 0));
        tile.setPrefColumns(2);
        Button createGame = new Button("Create New Game");
        udpClient = new UDPClient();
        udpClient.start();
        createGame.setOnMouseClicked(mouseEvent -> startUDPServer());
        tile.getChildren().add(createGame);

        TableView table = new TableView();
        TableColumn server = new TableColumn("IP");
        table.getColumns().addAll(server);
        server.setCellValueFactory(new PropertyValueFactory<>("ip"));

        gameServers = FXCollections.observableArrayList();
        table.setItems(gameServers);
        tile.getChildren().add(table);
        appPane.setCenter(tile);

        table.setRowFactory(tv -> {
            TableRow<GameServer> row = new TableRow<>();
            row.setOnMouseClicked(event -> startClientGame(row, event));
            return row;
        });
    }

    private static void startUDPServer() {
        udpServer = new UDPServer();
        if (udpClient.isAlive()) {
            udpClient.stop();
            udpServer.start();
        }
    }

    public static void startClientGame(TableRow<GameServer> row, MouseEvent event) {
        if (row.isEmpty() || event.getButton() != MouseButton.PRIMARY) {
            return;
        }
        GameServer serverClicked = row.getItem();
        String url = HTTP + serverClicked.getIp() + HTTP_PORT + REQUEST_GAME_ENDPOINT;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> response = restTemplate.getForEntity(url, Void.class);
        if (response.getStatusCode().toString().equals("200")) {
            ipServer = serverClicked.getIp();
            udpClient.stop();
            ClientApp.getApp(context, pane);
            url = HTTP + serverClicked.getIp() + HTTP_PORT + START_GAME_ENDPOINT;
            restTemplate.postForEntity(url, "", Void.class);
        }
    }

    public static void startGame() {
        Platform.runLater(() -> ServerApp.getApp(context, pane, ipClient));
    }

    public static void stopUdpServer() {
        udpServer.stop();
    }

    public static void updateServerList() {
        gameServers.clear();
        address.stream().forEach(addr -> gameServers.add(new GameServer(addr)));
    }

    public static String getIpServer() {
        return ipServer;
    }

    public static String getIpClient() {
        return ipClient;
    }

    public static void setIpClient(String ipClient) {
        NetworkApp.ipClient = ipClient;
    }

    public static void setIpServer(String ipServer) {
        NetworkApp.ipServer = ipServer;
    }
}
