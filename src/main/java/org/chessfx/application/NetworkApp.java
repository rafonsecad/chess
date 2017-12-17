/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chessfx.application;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
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
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author rafael
 */
public class NetworkApp {
    
    public static List<String> address = new ArrayList<>();
    private static String ipServer;
    private static String ipClient;
    private static ObservableList<Server> data;
    private static ServerUDP serverUDP;
    private static BorderPane pane;
    private static ConfigurableApplicationContext context;
    
    public static void getApp(ConfigurableApplicationContext springContext, BorderPane appPane){
        pane = appPane;
        context = springContext;
        TilePane tile = new TilePane();
        tile.setPadding(new Insets(5, 0, 5, 0));
        tile.setPrefColumns(2);
        Button create = new Button("Create New Game");
        ClientUDP udp = new ClientUDP();
        udp.start();
        create.setOnMouseClicked((MouseEvent t) -> {
            serverUDP = new ServerUDP();
            if (udp.isAlive()){
                udp.pause();
                serverUDP.start();
            }
        });
        tile.getChildren().add(create);
        
        TableView table = new TableView();
        TableColumn server = new TableColumn("IP");
        table.getColumns().addAll(server);
        server.setCellValueFactory(new PropertyValueFactory<>("ip"));
        
        data = FXCollections.observableArrayList();
        table.setItems(data);
        tile.getChildren().add(table);
        appPane.setCenter(tile);
        
        table.setRowFactory(tv -> {
            TableRow<Server> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY ){
                    Server serverClicked = row.getItem();
                    System.out.println(serverClicked.getIp());
                    String url = "http://" + serverClicked.getIp() + ":8080/api/server/requestgame";
                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<Void> response = restTemplate.getForEntity(url, Void.class);
                    if (response.getStatusCode().toString().equals("200")){
                        ipServer = serverClicked.getIp();
                        udp.pause();
                        ClientApp.getApp(springContext, appPane);
                        url = "http://" + serverClicked.getIp() + ":8080/api/server/startgame";
                        restTemplate.postForEntity(url, "" ,Void.class);
                    }
                }
            });
            return row;
        });
    }
    
    public static void startGame(){
        Platform.runLater(()->ServerApp.getApp(context, pane, ipClient));
    }
    
    public static void stopServer(){
        serverUDP.pause();
    }
    
    public static void updateServerList(){
        data.clear();
        address.stream().forEach(addr -> data.add(new Server(addr)));
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
    
    public static class Server {
        private final SimpleStringProperty ip;

        public Server(String ip){
            this.ip = new SimpleStringProperty(ip);
        }
        
        public String getIp() {
            return ip.get();
        }

        public void setIp(String ip) {
            this.ip.set(ip);
        }
    }
    
    public static class ServerUDP extends Thread{
        private boolean alive;
        @Override
        public void run(){
            try{
                alive = true;
                while(alive){
                    byte[] buf = new byte[256];
                    DatagramSocket socket;
                    socket = new DatagramSocket(4445);
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    InetAddress address = packet.getAddress();
                    String url = "http://" + address.getHostAddress() + ":8080/api/client/server";
                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                }
            }
            catch(Exception e){
                
            }
        }
        public void pause(){
            alive = false;
        }
    }
    
    public static class ClientUDP extends Thread{
        private boolean alive;
        @Override
        public void run(){
            alive = true;
            try{
                while(alive){
                    DatagramSocket socket = new DatagramSocket();
                    InetAddress address = InetAddress.getByName("255.255.255.255");
                    byte[] buf = "ChessFX".getBytes();
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
                    socket.setBroadcast(true);
                    socket.send(packet);
                    socket.close();
                    Thread.sleep(1000);
                }
            }
            catch(Exception e){
                
            }
        }
        public void pause(){
            alive = false;
        }
    }
}
