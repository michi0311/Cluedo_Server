package dto;

import java.util.LinkedList;

/****************************
 * Created by Michael Marolt *
 *****************************/

public class RoomsDTO extends RequestDTO {
    private LinkedList<String> gameRooms;
    private int selectedRoom;
    private String username;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
