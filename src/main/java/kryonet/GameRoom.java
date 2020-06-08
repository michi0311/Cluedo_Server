package kryonet;

import java.util.Date;
import java.util.LinkedList;

/****************************
 * Created by Michael Marolt *
 *****************************/

public class GameRoom {
    private static int currentID = 1;
    private LinkedList<ClientData> ClientList;
    private ClientData Host;
    private int roomID;
    private Date timestamp;

    public GameRoom (ClientData host) {
        ClientList = new LinkedList<>();
        this.Host = host;
        timestamp = new Date();
        roomID = currentID++;
    }

    public int getRoomID() {
        return roomID;
    }

    public LinkedList<ClientData> getClientList() {
        return ClientList;
    }

    public void addClient(ClientData clientData) {
        ClientList.add(clientData);
    }

    public ClientData getHost() {
        return Host;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
