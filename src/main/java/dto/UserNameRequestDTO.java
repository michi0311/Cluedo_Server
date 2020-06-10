package dto;

/****************************
 * Created by Michael Marolt *
 *****************************/

public class UserNameRequestDTO extends RequestDTO {
    private String username;
    private int id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
