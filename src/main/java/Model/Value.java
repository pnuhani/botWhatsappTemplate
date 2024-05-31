package Model;


import java.util.List;

public class Value {
    private List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Value{" +
                "messages=" + messages +
                '}';
    }
}
