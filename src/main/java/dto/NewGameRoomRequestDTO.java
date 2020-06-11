package dto;

/****************************
 * Created by Michael Marolt *
 *****************************/

public class NewGameRoomRequestDTO extends RequestDTO {
    private String username;
    private String createdRoom;
    private int hostID;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatedRoom() {
        return createdRoom;
    }

    public void setCreatedRoom(String createdRoom) {
        this.createdRoom = createdRoom;
    }

    public int getHostID() {
        return hostID;
    }

    public void setHostID(int hostID) {
        this.hostID = hostID;
    }
}
