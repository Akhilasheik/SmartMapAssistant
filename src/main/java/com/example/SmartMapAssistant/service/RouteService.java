package com.example.SmartMapAssistant.service;

import com.example.SmartMapAssistant.model.RouteRequest;
import com.example.SmartMapAssistant.model.RouteResponse;
import com.example.SmartMapAssistant.model.RouteStep;
import com.example.SmartMapAssistant.util.GeoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RouteService {
    private static final Logger log = LoggerFactory.getLogger(RouteService.class);

    @Value("${ors.api.key}")
    private String apiKey;

    @Value("${openroute.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public RouteResponse fetchBestRoutes(RouteRequest request) {
        double[] sourceCoords = GeoUtils.getCoordinatesFromAddress(request.getSource(), apiKey);
        double[] destCoords   = GeoUtils.getCoordinatesFromAddress(request.getDestination(), apiKey);

        log.info("Source coords: {}", Arrays.toString(sourceCoords));
        log.info("Destination coords: {}", Arrays.toString(destCoords));

        // 1) Try a direct bus route first
        List<RouteStep> directBus = getRouteSteps(sourceCoords, destCoords, "driving-car", "Bus");
        if (!directBus.isEmpty()) {
            log.info("Direct bus route found with {} step(s)", directBus.size());
            RouteResponse directResponse = new RouteResponse();
            directResponse.setCostEfficientRoute(directBus);
            directResponse.setTimeEfficientRoute(directBus);
            return directResponse;
        }

        // 2) No direct route → multimodal logic
        List<RouteStep> allSteps = new ArrayList<>();
        if (request.isPreferWalking()) {
            allSteps = getRouteSteps(sourceCoords, destCoords, "foot-walking", "Walking");
            if (allSteps.isEmpty()) {
                log.warn("No walking path found → fallback to Rapido end-to-end");
                allSteps = Collections.singletonList(
                        buildRapidoStep(sourceCoords, destCoords, "No walking path found — using Rapido end-to-end"));
            }
        } else {
            List<RouteStep> busSteps = getRouteSteps(sourceCoords, destCoords, "driving-car", "Bus");
            if (busSteps.isEmpty()) {
                log.warn("No bus path found → fallback to Rapido end-to-end");
                allSteps = Collections.singletonList(
                        buildRapidoStep(sourceCoords, destCoords, "No bus path found — using Rapido end-to-end"));
            } else {
                log.info("Bus route found with {} step(s), inserting Rapido fallback", busSteps.size());
                allSteps.addAll(busSteps);
                RouteStep rapido = new RouteStep();
                rapido.setMode("Rapido");
                rapido.setInstruction("Take Rapido between intermediate stops");
                rapido.setDistanceKm(2.0);
                rapido.setDurationMin(5);
                rapido.setCostEstimate(30.0);
                allSteps.add(1, rapido);
            }
        }

        if (allSteps.isEmpty()) {
            log.error("No steps generated — returning empty response");
            return new RouteResponse(Collections.emptyList(), Collections.emptyList());
        }

        RouteResponse response = new RouteResponse();
        response.setCostEfficientRoute(sortByCost(allSteps));
        response.setTimeEfficientRoute(sortByTime(allSteps));
        return response;
    }

    private RouteStep buildRapidoStep(double[] src, double[] dest, String instruction) {
        double dist = calcDistance(src, dest);
        RouteStep r = new RouteStep();
        r.setMode("Rapido");
        r.setInstruction(instruction);
        r.setDistanceKm(dist);
        r.setDurationMin(calcDurationFromDistance(dist));
        r.setCostEstimate(calcRapidoCost(dist));
        return r;
    }

    private List<RouteStep> getRouteSteps(double[] source, double[] dest, String profile, String mode) {
        String routeUrl = apiUrl + "/v2/directions/" + profile + "?api_key=" + apiKey;

        Map<String,Object> body = Map.of(
                "coordinates",
                List.of(
                        List.of(source[1], source[0]),
                        List.of(dest[1], dest[0])
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String,Object>> entity = new HttpEntity<>(body, headers);

        Map<?,?> result;
        try {
            result = restTemplate.postForObject(routeUrl, entity, Map.class);
        } catch (Exception e) {
            log.error("ORS routing failed for mode='{}'", mode, e);
            return Collections.emptyList();
        }

        if (result == null || !result.containsKey("routes")) {
            log.warn("ORS returned no 'routes' key for mode='{}'", mode);
            return Collections.emptyList();
        }
        List<?> routes = (List<?>) result.get("routes");
        if (routes.isEmpty()) {
            log.warn("ORS returned empty routes list for mode='{}'", mode);
            return Collections.emptyList();
        }

        Map<?,?> route = (Map<?,?>) routes.get(0);
        Map<?,?> summary = (Map<?,?>) route.get("summary");
        if (summary == null) {
            log.warn("ORS returned no summary for mode='{}'", mode);
            return Collections.emptyList();
        }

        double distanceMeters = ((Number) summary.get("distance")).doubleValue();
        double durationSeconds = ((Number) summary.get("duration")).doubleValue();

        RouteStep step = new RouteStep();
        step.setMode(mode);
        step.setInstruction("Travel via " + mode);
        step.setDistanceKm(distanceMeters / 1000.0);
        step.setDurationMin((int) Math.round(durationSeconds / 60.0));
        step.setCostEstimate("Bus".equalsIgnoreCase(mode) ? 15.0 : 0.0);

        log.info("Built {} step: {} km, {} min", mode, step.getDistanceKm(), step.getDurationMin());
        return Collections.singletonList(step);
    }

    private List<RouteStep> sortByCost(List<RouteStep> steps) {
        return steps.stream()
                .sorted(Comparator.comparingDouble(RouteStep::getCostEstimate))
                .collect(Collectors.toList());
    }

    private List<RouteStep> sortByTime(List<RouteStep> steps) {
        return steps.stream()
                .sorted(Comparator.comparingInt(RouteStep::getDurationMin))
                .collect(Collectors.toList());
    }

    private double calcDistance(double[] source, double[] destination) {
        return Math.hypot(source[0] - destination[0], source[1] - destination[1]) * 111.0;
    }

    private int calcDurationFromDistance(double distanceKm) {
        return (int) (distanceKm / 40 * 60);
    }

    private double calcRapidoCost(double distanceKm) {
        return distanceKm * 15;
    }
}
