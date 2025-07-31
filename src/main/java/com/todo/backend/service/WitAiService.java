package com.todo.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class WitAiService {

    @Value("${wit.token}")
    private String witToken;

    public JSONObject analyzeMessage(String message) {
        try {
            String url = "https://api.wit.ai/message?v=20240724&q=" + java.net.URLEncoder.encode(message, "UTF-8");

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", witToken); // pas besoin de "Bearer" s'il est déjà dans application.properties
            headers.set("Content-Type", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return new JSONObject(response.getBody());

        } catch (Exception e) {
            e.printStackTrace(); // LOGGUE L’ERREUR DANS LA CONSOLE
            return new JSONObject().put("error", "Erreur: " + e.getMessage());
        }
    }

    public String extractDatetime(JSONObject witResponse) {
        try {
            JSONObject entities = witResponse.getJSONObject("entities");

            if (!entities.has("wit$datetime:datetime")) return null;

            JSONArray datetimeArray = entities.getJSONArray("wit$datetime:datetime");

            String bestDate = null;
            int bestPrecision = -1;

            for (int i = 0; i < datetimeArray.length(); i++) {
                JSONObject item = datetimeArray.getJSONObject(i);

                String value = null;

                if (item.has("value")) {
                    value = item.getString("value");
                } else if (item.has("values")) {
                    JSONArray values = item.getJSONArray("values");
                    if (values.length() > 0) {
                        value = values.getJSONObject(0).getString("value");
                    }
                }

                String grain = item.optString("grain", "unknown");

                int precision = switch (grain) {
                    case "second" -> 3;
                    case "minute" -> 2;
                    case "hour" -> 1;
                    case "day" -> 0;
                    default -> -1;
                };

                if (value != null && precision > bestPrecision) {
                    bestPrecision = precision;
                    bestDate = value;
                }
            }

            return bestDate;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}

