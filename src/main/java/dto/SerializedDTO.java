package dto;

public class SerializedDTO extends  RequestDTO {
    private String serializedObject;

    public String getSerializedObject() {
        return serializedObject;
    }

    public void setSerializedObject(String serializedObject) {
        this.serializedObject = serializedObject;
    }
}
