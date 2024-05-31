package Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Message {
    private String from;
    private String id;
    private String timestamp;
    private String type;
    private Text text;
    private Button button;

    private Context context;

    private Interactive interactive;

    public Interactive getInteractive() {
        return interactive;
    }

    public void setInteractive(Interactive interactive) {
        this.interactive = interactive;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from='" + from + '\'' +
                ", id='" + id + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", type='" + type + '\'' +
                ", text=" + text +
                ", button=" + button +
                ", context=" + context +
                '}';
    }
}

class Interactive {
    private String type;
    private NFMReply nfmReply;

    // Getters and Setters
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("nfm_reply")
    public NFMReply getNfmReply() {
        return nfmReply;
    }

    public void setNfmReply(NFMReply nfmReply) {
        this.nfmReply = nfmReply;
    }
}

class NFMReply {
    private String responseJson;
    private String body;
    private String name;

    // Getters and Setters
    @JsonProperty("response_json")
    public String getResponseJson() {
        return responseJson;
    }

    public void setResponseJson(String responseJson) {
        this.responseJson = responseJson;
    }

    @JsonProperty("body")
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getPropertyFromResponseJson(String propertyName) {
        try {
            // Deserialize responseJson into JsonNode
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseJson);

            // Get value of the specified property
            JsonNode propertyNode = rootNode.get(propertyName);
            if (propertyNode != null) {
                return objectMapper.treeToValue(propertyNode, Object.class);
            }
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }
        return null; // Property not found or error occurred
    }
}
