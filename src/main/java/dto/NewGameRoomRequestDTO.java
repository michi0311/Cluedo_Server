package dto;

/****************************
 * Created by Michael Marolt *
 *****************************/

public class NewGameRoomRequestDTO extends RequestDTO {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
