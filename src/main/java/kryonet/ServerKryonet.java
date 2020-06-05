package kryonet;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import dto.RequestDTO;
import jdk.internal.jline.internal.Log;

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
                if (object instanceof RequestDTO) {
                    handleRequest(connection,object);
                }
            }
        });
    }

    private void handleRequest(Connection connection, Object object) {
        Log.debug("Received Object:" + object.getClass().toString());
        broadcastMessage(object);
    }

    @Override
    public void registerCallback(Callback<RequestDTO> callback) {
        this.messageCallback = callback;
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
        server.getKryo().register(c);
    }

    public String getAddress() {
        return null;
    }
}
