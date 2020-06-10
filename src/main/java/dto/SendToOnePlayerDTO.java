package dto;

/****************************
 * Created by Michael Marolt *
 *****************************/

public class SendToOnePlayerDTO extends RequestDTO {
    private int receivingPlayerID;
    private int sendingPlayerID;
    private boolean toHost;
    private String serializedObject;

    public int getReceivingPlayerID() {
        return receivingPlayerID;
    }

    public void setReceivingPlayerID(int receivingPlayerID) {
        this.receivingPlayerID = receivingPlayerID;
    }

    public int getSendingPlayerID() {
        return sendingPlayerID;
    }

    public void setSendingPlayerID(int sendingPlayerID) {
        this.sendingPlayerID = sendingPlayerID;
    }

    public String getSerializedObject() {
        return serializedObject;
    }

    public void setSerializedObject(String serializedObject) {
        this.serializedObject = serializedObject;
    }

    public void setToHost(boolean toHost) {
        this.toHost = toHost;
    }

    public boolean isToHost() {
        return toHost;
    }
}
