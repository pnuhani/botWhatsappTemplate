package Model;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.Map;

public class HttpEntityFormatter {

    public static String formatHttpEntity(HttpEntity<Map<String, Object>> httpEntity) {
        StringBuilder sb = new StringBuilder();
        HttpHeaders headers = httpEntity.getHeaders();
        Map<String, Object> body = httpEntity.getBody();

        sb.append("Headers:\n");
        headers.forEach((key, value) -> sb.append(key).append(": ").append(value).append("\n"));

        sb.append("Body:\n");
        if (body != null) {
            body.forEach((key, value) -> sb.append(key).append(": ").append(value).append("\n"));
        }

        return sb.toString();
    }
}
