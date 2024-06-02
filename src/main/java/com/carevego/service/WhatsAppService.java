package com.carevego.service;

import com.carevego.config.WhatsAppConfig;
import com.carevego.model.HttpEntityFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
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

    public void sendTemplateMessage(String recipientPhoneNumber, String scenario, Map<String, Object> dynamicParams) {
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
        //template.put("language",  config.getDefaultLanguage());

        if (templateConfig.getComponents() != null) {
            List<Map<String, Object>> components = buildComponents(templateConfig.getComponents());
            template.put("components", components);
        }

        body.put("template", template);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        /*
        {
	"messaging_product": "whatsapp",
	"to": "17788394648",
	"type": "template",
	"template": {
		"name": "dwpe",
		"language": {
			"code": "en"
		}
	}

}
         */
        // Print the request object for debugging
        System.out.println(HttpEntityFormatter.formatHttpEntity(request));
        restTemplate.exchange(config.getApi().getUrl(), HttpMethod.POST, request, String.class);
    }

    private List<Map<String, Object>> buildComponents(List<WhatsAppConfig.Component> componentsConfig) {
        List<Map<String, Object>> components = new ArrayList<>();
        for (WhatsAppConfig.Component componentConfig : componentsConfig) {
            Map<String, Object> component = new HashMap<>();
            component.put("type", componentConfig.getType());

            List<Map<String, Object>> parameters = new ArrayList<>();
            for (WhatsAppConfig.Parameter parameterConfig : componentConfig.getParameters()) {
                Map<String, Object> parameter = new HashMap<>();
                parameter.put("type", parameterConfig.getType());

                if ("image".equals(parameterConfig.getType())) {
                    parameter.put("image", Collections.singletonMap("link", parameterConfig.getImage().getLink()));
                    parameter.put("image", Collections.singletonMap("id", parameterConfig.getImage().getId()));
                }

                parameters.add(parameter);
            }

            component.put("parameters", parameters);
            components.add(component);
        }
        return components;
    }

    public void sendInteractiveMessage(String recipientPhoneNumber, String scenario, Map<String, Object> dynamicParams) {
        {
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
            body.put("type", "interactive");
            Map<String, Object> interactive = new HashMap<>();
            interactive.put("type", templateConfig.getInteractive().getType());
            //template.put("language",  config.getDefaultLanguage());

            Map<String, Object> header = new HashMap<>();
            header.put("type", templateConfig.getInteractive().getHeader().getType());
            header.put("text", templateConfig.getInteractive().getHeader().getText());
            interactive.put("header", header);

            Map<String, Object> bodyText = new HashMap<>();
            bodyText.put("text", templateConfig.getInteractive().getBody().getText());
            interactive.put("body", bodyText);

            Map<String, Object> footer = new HashMap<>();
            footer.put("text", templateConfig.getInteractive().getFooter().getText());
            interactive.put("footer", footer);

            Map<String, Object> action = new HashMap<>();
            action.put("name", templateConfig.getInteractive().getAction().getName());

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("display_text", templateConfig.getInteractive().getAction().getParameters().getDisplayText());
            parameters.put("url", templateConfig.getInteractive().getAction().getParameters().getUrl());
            action.put("parameters", parameters);

            interactive.put("action", action);

            body.put("interactive", interactive);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            // Print the request object for debugging
            System.out.println(HttpEntityFormatter.formatHttpEntity(request));

            restTemplate.exchange(config.getApi().getUrl(), HttpMethod.POST, request, String.class);
        }
    }

    public void sendFlowMessage(String recipientPhoneNumber, String scenario, Map<String, Object> dynamicParams) {
        WhatsAppConfig.Template templateConfig = config.getTemplates().get(scenario);

        if (templateConfig == null) {
            throw new IllegalArgumentException("No template configured for scenario: " + scenario);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(config.getApi().getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("recipient_type", "individual");
        body.put("to", recipientPhoneNumber);
        body.put("type", "interactive");

        Map<String, Object> interactive = new HashMap<>();
        interactive.put("type", templateConfig.getInteractive().getType());

        if (templateConfig.getInteractive().getBody() != null) {
            Map<String, Object> bodyText = new HashMap<>();
            bodyText.put("text", templateConfig.getInteractive().getBody().getText());
            interactive.put("body", bodyText);
        }

        if (templateConfig.getInteractive().getFooter() != null) {
            Map<String, Object> footer = new HashMap<>();
            footer.put("text", templateConfig.getInteractive().getFooter().getText());
            interactive.put("footer", footer);
        }

        Map<String, Object> action = new HashMap<>();
        action.put("name", templateConfig.getInteractive().getAction().getName());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("flow_message_version", templateConfig.getInteractive().getAction().getParameters().getFlowMessageVersion());
        parameters.put("flow_token", templateConfig.getInteractive().getAction().getParameters().getFlowToken());
        parameters.put("flow_id", templateConfig.getInteractive().getAction().getParameters().getFlowId());
        parameters.put("flow_cta", templateConfig.getInteractive().getAction().getParameters().getFlowCta());
        parameters.put("flow_action", templateConfig.getInteractive().getAction().getParameters().getFlowAction());

        Map<String, Object> flowActionPayload = new HashMap<>();
        flowActionPayload.put("screen", templateConfig.getInteractive().getAction().getParameters().getFlowActionPayload().getScreen());
        parameters.put("flow_action_payload", flowActionPayload);

        action.put("parameters", parameters);

        interactive.put("action", action);

        body.put("interactive", interactive);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        System.out.println(HttpEntityFormatter.formatHttpEntity(request));

        restTemplate.exchange(config.getApi().getUrl(), HttpMethod.POST, request, String.class);
    }

    public void sendTextMessage(String to, boolean trackingLinkFound, String trackingUrl) {
        String body;
        if (trackingLinkFound) {
            body = config.getText().getBodyIfLinkThere() + "\n"+trackingUrl;
        } else {
            body = config.getText().getBodyIfLinkNotThere();
        }

        Map<String, Object> message = new HashMap<>();
        message.put("messaging_product", "whatsapp");
        message.put("recipient_type", "individual");
        message.put("to", to);
        message.put("type", config.getText().getType());

        Map<String, Object> text = new HashMap<>();
        text.put("preview_url", config.getText().isPreviewUrl());
        text.put("body", body);

        message.put("text", text);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(config.getApi().getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(message, headers);
        ResponseEntity<String> response = restTemplate.exchange(config.getApi().getUrl(), HttpMethod.POST, entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            System.err.println("Failed to send message: " + response.getBody());
        }
    }

}
