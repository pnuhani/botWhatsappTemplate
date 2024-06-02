package Model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "whatsapp")
public class WhatsAppConfig {

    private Api api = new Api();
    private Map<String, Template> templates = new HashMap<>();

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    private Text text = new Text();

    private String defaultLanguage;

    public static class Api {
        private String url;
        private String accessToken;

        // Getters and Setters
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
    }

    public static class Template {
        private String name;
        private Interactive interactive;
        private List<Component> components;
        public List<Component> getComponents() {
            return components;
        }

        public Interactive getInteractive() {
            return interactive;
        }

        public void setInteractive(Interactive interactive) {
            this.interactive = interactive;
        }

        public void setComponents(List<Component> components) {
            this.components = components;
        }

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public static class Component {
        private String type;
        private List<Parameter> parameters;

        // Getters and Setters
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Parameter> getParameters() {
            return parameters;
        }

        public void setParameters(List<Parameter> parameters) {
            this.parameters = parameters;
        }
    }

    public static class Parameter {
        private String type;
        private Image image;

        // Getters and Setters
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }
    }
    public static class Image {
        private String link;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        private String id;

        // Getters and Setters
        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }

    public static class Interactive {
        private String type;
        private Header header;
        private Body body;
        private Footer footer;
        private Action action;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Header getHeader() {
            return header;
        }

        public void setHeader(Header header) {
            this.header = header;
        }

        public Body getBody() {
            return body;
        }

        public void setBody(Body body) {
            this.body = body;
        }

        public Footer getFooter() {
            return footer;
        }

        public void setFooter(Footer footer) {
            this.footer = footer;
        }

        public Action getAction() {
            return action;
        }

        public void setAction(Action action) {
            this.action = action;
        }
    }

    public static class Body {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class Footer {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }


    public static class Action {
        private String name;
        private Parameters parameters;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Parameters getParameters() {
            return parameters;
        }

        public void setParameters(Parameters parameters) {
            this.parameters = parameters;
        }
    }
    public static class Parameters {
        private String displayText;
        private String url;
        private String flowMessageVersion;
        private String flowToken;

        public String getFlowMessageVersion() {
            return flowMessageVersion;
        }

        public void setFlowMessageVersion(String flowMessageVersion) {
            this.flowMessageVersion = flowMessageVersion;
        }

        public String getFlowToken() {
            return flowToken;
        }

        public void setFlowToken(String flowToken) {
            this.flowToken = flowToken;
        }

        public String getFlowId() {
            return flowId;
        }

        public void setFlowId(String flowId) {
            this.flowId = flowId;
        }

        public String getFlowCta() {
            return flowCta;
        }

        public void setFlowCta(String flowCta) {
            this.flowCta = flowCta;
        }

        public String getFlowAction() {
            return flowAction;
        }

        public void setFlowAction(String flowAction) {
            this.flowAction = flowAction;
        }

        public FlowActionPayload getFlowActionPayload() {
            return flowActionPayload;
        }

        public void setFlowActionPayload(FlowActionPayload flowActionPayload) {
            this.flowActionPayload = flowActionPayload;
        }


        private String flowId;
        private String flowCta;
        private String flowAction;
        private FlowActionPayload flowActionPayload;

        public String getDisplayText() {
            return displayText;
        }

        public void setDisplayText(String displayText) {
            this.displayText = displayText;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class FlowActionPayload {
        private String screen;

        // Getters and Setters
        public String getScreen() {
            return screen;
        }

        public void setScreen(String screen) {
            this.screen = screen;
        }
    }
    public static class Header {
        private String type;
        private String text;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class Text {
        private String name;
        private String type;
        private boolean previewUrl;
        private String bodyIfLinkThere;
        private String bodyIfLinkNotThere;

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isPreviewUrl() {
            return previewUrl;
        }

        public void setPreviewUrl(boolean previewUrl) {
            this.previewUrl = previewUrl;
        }

        public String getBodyIfLinkThere() {
            return bodyIfLinkThere;
        }

        public void setBodyIfLinkThere(String bodyIfLinkThere) {
            this.bodyIfLinkThere = bodyIfLinkThere;
        }

        public String getBodyIfLinkNotThere() {
            return bodyIfLinkNotThere;
        }

        public void setBodyIfLinkNotThere(String bodyIfLinkNotThere) {
            this.bodyIfLinkNotThere = bodyIfLinkNotThere;
        }
    }
    // Getters and Setters
    public Api getApi() {
        return api;
    }

    public void setApi(Api api) {
        this.api = api;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public Map<String, Template> getTemplates() {
        return templates;
    }

    public void setTemplates(Map<String, Template> templates) {
        this.templates = templates;
    }
}
