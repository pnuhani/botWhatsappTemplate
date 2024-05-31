package Model;

public class Button {
    private String payload;
    private String text;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Button{" +
                "payload='" + payload + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
