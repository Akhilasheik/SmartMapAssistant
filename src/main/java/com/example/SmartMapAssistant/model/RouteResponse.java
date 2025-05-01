package com.example.SmartMapAssistant.model;

import java.util.List;

public class RouteResponse {
    private List<RouteStep> costEfficientRoute;
    private List<RouteStep> timeEfficientRoute;

    public RouteResponse() {

    }

    // Getters and setters
    public List<RouteStep> getCostEfficientRoute() {
        return costEfficientRoute;
    }

    public void setCostEfficientRoute(List<RouteStep> costEfficientRoute) {
        this.costEfficientRoute = costEfficientRoute;
    }

    public List<RouteStep> getTimeEfficientRoute() {
        return timeEfficientRoute;
    }

    public void setTimeEfficientRoute(List<RouteStep> timeEfficientRoute) {
        this.timeEfficientRoute = timeEfficientRoute;
    }
    public RouteResponse(List<RouteStep> costRoute, List<RouteStep> timeRoute) {
        this.costEfficientRoute = costRoute;
        this.timeEfficientRoute = timeRoute;
    }

}
