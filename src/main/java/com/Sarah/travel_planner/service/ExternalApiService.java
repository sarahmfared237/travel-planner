package com.Sarah.travel_planner.service;

import com.Sarah.travel_planner.model.Destination;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExternalApiService {

    private static final String REST_COUNTRIES_URL = "https://restcountries.com/v3.1/region/";
    private final RestTemplate restTemplate = new RestTemplate();

    @SuppressWarnings("unchecked")
    public List<Destination> fetchDestinationsByRegion(String region) {
        String url = REST_COUNTRIES_URL + region;
        List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);

        List<Destination> destinations = new ArrayList<>();

        if (response != null) {
            for (Map<String, Object> country : response) {
                try {
                    Destination destination = mapToDestination(country);
                    destinations.add(destination);
                } catch (Exception ignored) {}
            }
        }

        return destinations;
    }

    @SuppressWarnings("unchecked")
    private Destination mapToDestination(Map<String, Object> country) {
        Map<String, Object> nameMap = (Map<String, Object>) country.get("name");
        String name = nameMap != null ? (String) nameMap.get("common") : "Unknown";

        String capital = "";
        List<String> capitals = (List<String>) country.get("capital");
        if (capitals != null && !capitals.isEmpty()) capital = capitals.get(0);

        String region = (String) country.getOrDefault("region", "");

        Number pop = (Number) country.getOrDefault("population", 0);
        long population = pop.longValue();

        String currency = "";
        Map<String, Object> currencies = (Map<String, Object>) country.get("currencies");
        if (currencies != null && !currencies.isEmpty()) {
            currency = currencies.keySet().iterator().next();
        }

        String flagUrl = "";
        Map<String, Object> flags = (Map<String, Object>) country.get("flags");
        if (flags != null) flagUrl = (String) flags.getOrDefault("png", "");

        return Destination.builder()
                .name(name)
                .capital(capital)
                .region(region)
                .population(population)
                .currency(currency)
                .flagUrl(flagUrl)
                .approved(false)
                .build();
    }
}