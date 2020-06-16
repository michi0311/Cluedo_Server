package kryonet;

import com.esotericsoftware.kryonet.Connection;

/****************************
 * Created by Michael Marolt *
 *****************************/

public class ClientData {
    private static int currentID = 1;

    private int id;
    private String username;
    private Connection connection;

    public int getId() {
        return id;
    }

    public void setId() {
        this.id = currentID++;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
