package kryonet;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import dto.RegisterClassDTO;
import dto.RequestDTO;
import dto.SerializedDTO;
import dto.TextMessage;

import java.io.IOException;

/****************************
 * Created by Michael Marolt *
 *****************************/

public class ServerKryonet implements NetworkServer {
    private Server server;
    private Callback<RequestDTO> messageCallback;

    public ServerKryonet() {
        server = new Server();
    }



    public void start() throws IOException {
        server.start();
        server.bind(NetworkConstants.TCP_PORT,NetworkConstants.UDP_PORT);

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
        }

        else if (object instanceof RegisterClassDTO) {
            handleClassRegistration(connection, (RegisterClassDTO) object);
        }

        //broadcastMessageTest(object);
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
