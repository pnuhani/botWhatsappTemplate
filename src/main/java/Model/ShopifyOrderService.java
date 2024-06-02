package Model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Optional;

@Service
public class ShopifyOrderService {

    @Value("${shopify.api.url}")
    private String shopifyApiUrl;

    @Value("${shopify.api.accessToken}")
    private String accessToken;

    private final RestTemplate restTemplate;

    public ShopifyOrderService() {
        this.restTemplate = new RestTemplate();
    }

    public Optional<String> getTrackingUrl(String orderId) {
        String url = String.format("%s/orders.json?name=%s&status=any&fields=fulfillments", shopifyApiUrl, orderId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Shopify-Access-Token", accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            // Parse the JSON response to extract tracking_url
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode orders = root.path("orders");

                if (orders.isArray() && orders.size() > 0) {
                    JsonNode fulfillments = orders.get(0).path("fulfillments");
                    if (fulfillments.isArray() && fulfillments.size() > 0) {
                        JsonNode trackingUrls = fulfillments.get(0).path("tracking_urls");
                        if (trackingUrls.isArray() && trackingUrls.size() > 0) {
                            return Optional.of(trackingUrls.get(0).asText());
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Optional.empty();
    }
}
