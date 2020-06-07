package dto;

public class TextMessage {
    public String text;

    public TextMessage () {

    }

    public TextMessage(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return String.format("TextMessage: %s", text);
    }
}
