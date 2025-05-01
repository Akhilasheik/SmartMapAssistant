package com.example.SmartMapAssistant.util;

import com.example.SmartMapAssistant.model.ORSGeocodeResponse;
import com.example.SmartMapAssistant.model.NominatimLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GeoUtils {
    private static final Logger log = LoggerFactory.getLogger(GeoUtils.class);
    private static final RestTemplate restTemplate = new RestTemplate();

    public static double[] getCoordinatesFromAddress(String address, String apiKey) {
        // 1) Check for manual overrides first
        double[] manualOverride = LandmarkOverrides.OVERRIDES.get(address.trim());
        if (manualOverride != null) {
            log.info("Using manual override for: {}", address);
            return manualOverride;
        }

        // 2) Try OpenRouteService
        try {
            String orsUrl = "https://api.openrouteservice.org/geocode/search?api_key="
                    + apiKey + "&text=" + URLEncoder.encode(address, StandardCharsets.UTF_8);
            log.info("Calling ORS: {}", orsUrl);

            ORSGeocodeResponse orsResult = restTemplate.getForObject(orsUrl, ORSGeocodeResponse.class);
            if (orsResult != null && orsResult.getFeatures() != null && !orsResult.getFeatures().isEmpty()) {
                var coords = orsResult.getFeatures()
                        .get(0)
                        .getGeometry()
                        .getCoordinates();
                // ORS returns [lon, lat]
                return new double[]{ coords.get(1), coords.get(0) };
            }
            log.warn("ORS returned no features for address: {}", address);
        } catch (Exception e) {
            log.error("ORS geocoding failed for {}: {}", address, e.getMessage());
        }

        // 3) Fallback to Nominatim
        try {
            String nomUrl = "https://nominatim.openstreetmap.org/search?q="
                    + URLEncoder.encode(address, StandardCharsets.UTF_8)
                    + "&format=json&limit=1";
            log.info("Calling Nominatim: {}", nomUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "SmartMapAssistant/1.0 (your_email@example.com)");
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<NominatimLocation[]> response = restTemplate.exchange(
                    nomUrl, HttpMethod.GET, entity, NominatimLocation[].class);

            NominatimLocation[] nomList = response.getBody();
            if (nomList != null && nomList.length > 0) {
                double lat = Double.parseDouble(nomList[0].getLat());
                double lon = Double.parseDouble(nomList[0].getLon());
                return new double[]{ lat, lon };
            }
            log.warn("Nominatim returned no results for address: {}", address);
        } catch (Exception e) {
            log.error("Nominatim geocoding failed for {}: {}", address, e.getMessage());
        }

        // If both fail
        log.error("Unable to geocode address after both ORS and Nominatim: {}", address);
        throw new RuntimeException("Unable to geocode address: " + address);
    }
}
