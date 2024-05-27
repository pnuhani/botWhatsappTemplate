package Model;


import java.util.List;

public class WebhookRequest {
    private List<Entry> entry;

    // Getters and Setters
    public List<Entry> getEntry() {
        return entry;
    }

    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }
}
