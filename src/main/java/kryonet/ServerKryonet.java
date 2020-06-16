package kryonet;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import dto.*;


import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/****************************
 * Created by Michael Marolt *
 *****************************/

public class ServerKryonet implements NetworkServer {
    //private LinkedList<GameRoom> gameRoomLinkedList;
    private HashMap<Integer, GameRoom> gameRoomHashMap;
    private HashMap<Connection, Integer> clientToRoomHashMap;
    private Server server;
    private Callback<RequestDTO> messageCallback;

    public ServerKryonet() {
        server = new Server(16384, 4096);
    }



    public void start() throws IOException {
        server.start();
        server.bind(NetworkConstants.TCP_PORT,NetworkConstants.UDP_PORT);

        //gameRoomLinkedList = new LinkedList<>();
        gameRoomHashMap = new HashMap<>();
        clientToRoomHashMap = new HashMap<>();

        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                System.out.println(object.getClass().toString());
                if (object instanceof RequestDTO) {
                    handleRequest(connection,object);
                }
            }
        });
    }

    private void handleRequest(Connection connection, Object object) {
        for (GameRoom room: gameRoomHashMap.values()) {
            String playerInRoom ="";
            for (ClientData c : room.getClientList()) {
                playerInRoom = playerInRoom + c.getId() + " ";
            }
            System.out.println("Room ID: " + room.getRoomID() + "Players: " + playerInRoom + " " + room.getHost().getId());
        }
        System.out.println("Received Object:" + object.getClass().toString());
        if (object instanceof TextMessage) {
            System.out.println(((TextMessage) object).toString());
        } else if (object instanceof NewGameRoomRequestDTO) {
            handleNewGameRoomRequest(connection, (NewGameRoomRequestDTO) object);
        } else if (object instanceof RoomsDTO) {
            handleRoomRequest(connection, (RoomsDTO) object);
        } else if (object instanceof BroadcastDTO) {
            handleBroadcastRequest(connection, (BroadcastDTO) object);
        } else if (object instanceof SendToOnePlayerDTO) {
            handleSendToOnePlayerRequest(connection, (SendToOnePlayerDTO) object);
        }

        else if (object instanceof RegisterClassDTO) {
            handleClassRegistration(connection, (RegisterClassDTO) object);
        }

        //broadcastMessageTest(object);
    }

    private void handleNewGameRoomRequest(Connection connection, NewGameRoomRequestDTO newGameRoomRequestDTO) {
        ClientData host = new ClientData();
        host.setId();
        host.setConnection(connection);
        host.setUsername(newGameRoomRequestDTO.getUsername());

        GameRoom newRoom = new GameRoom(host);
        gameRoomHashMap.put(newRoom.getRoomID(), newRoom);
        clientToRoomHashMap.put(connection, newRoom.getRoomID());
        //gameRoomLinkedList.add(newRoom);

        System.out.println("New Room initialized: Host: " + host.getUsername() + " Room: " + newRoom.getRoomID());

        NewGameRoomRequestDTO response = new NewGameRoomRequestDTO();
        response.setCreatedRoom("Room " + newRoom.getRoomID());
        response.setHostID(host.getId());
        sendMessageToClient(response,connection);
    }

    private void handleRoomRequest(Connection connection, RoomsDTO roomsDTO) {
        if (roomsDTO.getGameRooms() == null && roomsDTO.getSelectedRoom() == 0) {
            System.out.println("WTF1");
            RoomsDTO availableRooms = new RoomsDTO();

            LinkedList<String> availableRoomsList = new LinkedList<>();
            for (GameRoom gm: gameRoomHashMap.values()) {
                availableRoomsList.add("Room " + gm.getRoomID() + " (" + (gm.getClientList().size() + 1) + "/6)" );
            }
            availableRooms.setGameRooms(availableRoomsList);

            sendMessageToClient(availableRooms,connection);
        } else if (roomsDTO.getSelectedRoom() != 0) {
            int selectedRoom = roomsDTO.getSelectedRoom();
            String username = roomsDTO.getUsername();

            ClientData client = new ClientData();
            client.setUsername(username);
            client.setConnection(connection);
            client.setId();

            GameRoom room = gameRoomHashMap.get(selectedRoom);
            room.addClient(client);

            clientToRoomHashMap.put(connection, selectedRoom);
            System.out.println("Client " + client.getUsername() + " added to GameRoom No " + room.getRoomID());

            UserNameRequestDTO userNameRequestDTO = new UserNameRequestDTO();
            userNameRequestDTO.setId(client.getId());
            userNameRequestDTO.setUsername(client.getUsername());

            //send Message to host from Room
            sendMessageToClient(userNameRequestDTO,room.getHost().getConnection());
        }
    }

    private void handleBroadcastRequest(Connection connection, BroadcastDTO broadcastDTO) {
        //get the gameRoom where the user plays
        GameRoom gameRoom = gameRoomHashMap.get(clientToRoomHashMap.get(connection));
        LinkedList<ClientData> clients = gameRoom.getClientList();
        //clients.add(gameRoom.getHost());
        ClientData host = gameRoom.getHost();

        // this only sends the object to the clients, not the Host
        for (ClientData client: clients) {
            if (client.getConnection() != connection) {
                sendMessageToClient(broadcastDTO,client.getConnection());
            }
        }
        if (host.getConnection()!=connection) {
            sendMessageToClient(broadcastDTO,host.getConnection());
        }
    }

    private void handleSendToOnePlayerRequest(Connection connection, SendToOnePlayerDTO sendToOnePlayerDTO) {
        GameRoom gameRoom = gameRoomHashMap.get(clientToRoomHashMap.get(connection));

        //set Sending ID
        for (ClientData client: gameRoom.getClientList()) {
            if (client.getConnection() == connection) {
                sendToOnePlayerDTO.setSendingPlayerID(client.getId());
            }
        }

        if (sendToOnePlayerDTO.isToHost()) {
            sendMessageToClient(sendToOnePlayerDTO, gameRoom.getHost().getConnection());
            return;
        } else {
            for (ClientData client: gameRoom.getClientList()) {
                if (client.getId() == sendToOnePlayerDTO.getReceivingPlayerID()) {
                    sendMessageToClient(sendToOnePlayerDTO, client.getConnection());
                }
            }
        }


    }

    private void handleClassRegistration(Connection connection, RegisterClassDTO registerClassDTO) {
        System.out.println("Registering Class: "+ registerClassDTO.getClassToRegister());
        SerializedDTO serializedDTO = new SerializedDTO();
        serializedDTO.setSerializedObject(registerClassDTO.getClassToRegister());
        sendMessageToClient(serializedDTO,connection);


    }

    @Override
    public void registerCallback(Callback<RequestDTO> callback) {
        this.messageCallback = callback;
    }

    public void broadcastMessageTest(Object object) {
        for (Connection connection: server.getConnections()) {
            sendMessageToClientTest(object, connection);
        }
    }

    private void sendMessageToClientTest(final Object object, final Connection connection) {
        System.out.println("Send Message To Client");

        new Thread("send") {
            @Override
            public void run() {
                connection.sendTCP(object);
            }
        }.start();
    }

    @Override
    public void broadcastMessage(RequestDTO request) {
        for (Connection connection: server.getConnections()) {
            sendMessageToClient(request, connection);
        }
    }

    private void sendMessageToClient(final RequestDTO request, final Connection connection) {
        System.out.println("Send Message To Client");

        new Thread("send") {
            @Override
            public void run() {
                connection.sendTCP(request);
            }
        }.start();
    }

    public void registerClass(Class c) {
        System.out.println("Registering Class");
        server.getKryo().register(c);

    }

    public void registerClass(Class c, int id) {
        System.out.println("Registering Class");
        server.getKryo().register(c, id);

    }

    public String getAddress() {
        return null;
    }
}
