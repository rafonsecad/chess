/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.application.networkapp;

import java.util.Optional;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import org.chessfx.application.model.GameServer;
import org.chessfx.core.piece.Team;
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

    private static String ipServer;
    private static String ipClient;
    private static ObservableList<GameServer> gameServers;
    private static UDPServer udpServer;
    private static UDPClient udpClient;
    private static BorderPane pane;
    private static VBox clientPane;
    private static ConfigurableApplicationContext context;

    public static void getApp(ConfigurableApplicationContext springContext, BorderPane appPane) {
        pane = appPane;
        context = springContext;
        TilePane tile = new TilePane();
        tile.setPadding(new Insets(40));
        tile.setPrefColumns(2);
        
        VBox serverPane = getServerPane();
        tile.getChildren().add(serverPane);

        clientPane = getClientPane();
        tile.getChildren().add(clientPane);
        
        appPane.setCenter(tile);
    }
    
    private static VBox getServerPane(){
        udpServer = new UDPServer();
        VBox serverPane = new VBox();
        serverPane.setPadding(new Insets(20, 20, 20, 20));
        serverPane.setSpacing(20);
        
        ToggleGroup group = new ToggleGroup();
        RadioButton whitePieces = new RadioButton("White");
        RadioButton blackPieces = new RadioButton("Black");
        whitePieces.getStyleClass().add("radio-button");
        blackPieces.getStyleClass().add("radio-button");
        whitePieces.setToggleGroup(group);
        whitePieces.setSelected(true);
        blackPieces.setToggleGroup(group);
        serverPane.getChildren().add(whitePieces);
        serverPane.getChildren().add(blackPieces);
        whitePieces.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                if (isNowSelected) { 
                    udpServer.setTeam(Team.WHITE);
                    return;
                }
                udpServer.setTeam(Team.BLACK);
            }
        });
        
        Button createGame = new Button("Create New Game");
        createGame.getStyleClass().add("ipad-grey");
        udpClient = new UDPClient();
        udpClient.start();
        createGame.setOnMouseClicked(mouseEvent -> startUDPServer(createGame));
        serverPane.getChildren().add(createGame);
        
        return serverPane;
    }

    private static void startUDPServer(Button button) {
        if (udpClient.isAlive()) {
            udpClient.stop();
            clientPane.setVisible(false);
            clientPane.setManaged(false);
            udpServer.start();
            button.setText("Cancel Game");
            return;
        }
        udpServer.stop();
        clientPane.setVisible(true);
        clientPane.setManaged(true);
        udpClient.start();
        button.setText("Create New Game");
    }

    private static VBox getClientPane(){
        VBox clientPane = new VBox();
        Label clientLabel = new Label("Pick One Server");
        clientLabel.getStyleClass().add("label-client-table");
        TableView table = new TableView();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn serverIP = new TableColumn("IP");
        TableColumn serverTeam = new TableColumn("Team");
        table.getColumns().addAll(serverIP, serverTeam);
        serverIP.setCellValueFactory(new PropertyValueFactory<>("ip"));
        serverTeam.setCellValueFactory(new PropertyValueFactory<>("team"));
        clientPane.getChildren().add(clientLabel);
        clientPane.getChildren().add(table);
        
        gameServers = FXCollections.observableArrayList();
        table.setItems(gameServers);

        table.setRowFactory(tv -> {
            TableRow<GameServer> row = new TableRow<>();
            row.setOnMouseClicked(event -> startClientGame(row, event));
            return row;
        });
        return clientPane;
    }
    
    private static void startClientGame(TableRow<GameServer> row, MouseEvent event) {
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

    public static void updateServerList(GameServer server) {
        Optional<GameServer> serverFound = gameServers.stream().filter(s -> s.getIp().equals(server.getIp())).findAny();
        if(!serverFound.isPresent()){
            gameServers.add(server);
        }
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
