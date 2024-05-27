package Service;

import Config.WhatsAppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class WhatsAppService {

    private final WhatsAppConfig config;
    private final RestTemplate restTemplate;

    @Autowired
    public WhatsAppService(WhatsAppConfig config, RestTemplate restTemplate) {
        this.config = config;
        this.restTemplate = restTemplate;
    }

    public void sendTemplateMessage(String recipientPhoneNumber, String scenario, Map<String, String> dynamicParams) {
        WhatsAppConfig.Template templateConfig = config.getTemplates().get(scenario);

        if (templateConfig == null) {
            throw new IllegalArgumentException("No template configured for scenario: " + scenario);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(config.getApi().getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("to", recipientPhoneNumber);
        body.put("type", "template");

        Map<String, Object> template = new HashMap<>();
        template.put("name", templateConfig.getName());
        template.put("language", Collections.singletonMap("code", config.getDefaultLanguage()));

        List<Map<String, Object>> components = new ArrayList<>();
        Map<String, Object> component = new HashMap<>();
        component.put("type", "body");

        List<Map<String, Object>> parameters = new ArrayList<>();
        for (Map.Entry<String, String> entry : templateConfig.getParameters().entrySet()) {
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("type", "text");
            String value = entry.getValue();

            // Replace placeholders with dynamic parameters
            for (Map.Entry<String, String> dynamicEntry : dynamicParams.entrySet()) {
                value = value.replace("{" + dynamicEntry.getKey() + "}", dynamicEntry.getValue());
            }

            parameter.put("text", value);
            parameters.add(parameter);
        }

        component.put("parameters", parameters);
        components.add(component);
        template.put("components", components);

        body.put("template", template);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        restTemplate.exchange(config.getApi().getUrl(), HttpMethod.POST, request, String.class);
    }
}
