package com.example.SmartMapAssistant.model;

public class RouteRequest {
    private String source;
    private String destination;
    private boolean preferWalking;

    // Getters and setters
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public boolean isPreferWalking() {
        return preferWalking;
    }

    public void setPreferWalking(boolean preferWalking) {
        this.preferWalking = preferWalking;
    }
}
