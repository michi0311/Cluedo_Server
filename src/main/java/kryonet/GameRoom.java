package kryonet;

import java.util.LinkedList;

/****************************
 * Created by Michael Marolt *
 *****************************/

public class GameRoom {
    private static int currentID = 1;
    private LinkedList<ClientData> ClientList;
    private int roomID;

    public GameRoom () {
        ClientList = new LinkedList<>();
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
}
