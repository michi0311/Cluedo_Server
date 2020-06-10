package dto;

/****************************
 * Created by Michael Marolt *
 *****************************/

public class BroadcastDTO extends RequestDTO {
    private String serializedObject;

    public String getSerializedObject() {
        return serializedObject;
    }

    public void setSerializedObject(String serializedObject) {
        this.serializedObject = serializedObject;
    }
}
