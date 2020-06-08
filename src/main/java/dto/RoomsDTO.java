package dto;

import java.util.LinkedList;

/****************************
 * Created by Michael Marolt *
 *****************************/

public class RoomsDTO extends RequestDTO {
    private LinkedList<String> gameRooms;
    private int selectedRoom;

    public LinkedList<String> getGameRooms() {
        return gameRooms;
    }

    public void setGameRooms(LinkedList<String> gameRooms) {
        this.gameRooms = gameRooms;
    }

    public int getSelectedRoom() {
        return selectedRoom;
    }

    public void setSelectedRoom(int selectedRoom) {
        this.selectedRoom = selectedRoom;
    }
}
