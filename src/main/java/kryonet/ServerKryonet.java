package kryonet;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import dto.*;


import java.io.IOException;
import java.util.LinkedList;

/****************************
 * Created by Michael Marolt *
 *****************************/

public class ServerKryonet implements NetworkServer {
    private LinkedList<GameRoom> gameRoomLinkedList;
    private Server server;
    private Callback<RequestDTO> messageCallback;

    public ServerKryonet() {
        server = new Server();
    }



    public void start() throws IOException {
        server.start();
        server.bind(NetworkConstants.TCP_PORT,NetworkConstants.UDP_PORT);

        gameRoomLinkedList = new LinkedList<>();

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

        System.out.println("Received Object:" + object.getClass().toString());
        if (object instanceof TextMessage) {
            System.out.println(((TextMessage) object).toString());
        } else if (object instanceof NewGameRoomRequestDTO) {
            handleNewGameRoomRequest(connection, (NewGameRoomRequestDTO) object);
        } else if (object instanceof RoomsDTO) {
            handleRoomRequest(connection, (RoomsDTO) object);
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
        gameRoomLinkedList.add(newRoom);

        System.out.println("New Room initialized: Host: " + host.getUsername() + "Room: " + newRoom.getRoomID());

        NewGameRoomRequestDTO response = new NewGameRoomRequestDTO();
        response.setCreatedRoom("Room" + newRoom.getRoomID());
        sendMessageToClient(response,connection);
    }

    private void handleRoomRequest(Connection connection, RoomsDTO roomsDTO) {
        if (roomsDTO.getGameRooms() == null && roomsDTO.getSelectedRoom() == 0) {
            System.out.println("WTF1");
            RoomsDTO availableRooms = new RoomsDTO();

            LinkedList<String> availableRoomsList = new LinkedList<>();
            for (GameRoom gm: gameRoomLinkedList) {
                availableRoomsList.add("Room " + gm.getRoomID() + " (" + (gm.getClientList().size() + 1) + "/6)" );
            }
            availableRooms.setGameRooms(availableRoomsList);

            sendMessageToClient(availableRooms,connection);
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
