package com.example.SmartMapAssistant.model;

import java.util.List;

public class ORSGeocodeResponse {
    private List<Feature> features;

    public List<Feature> getFeatures() {
        return features;
    }
    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public static class Feature {
        private Geometry geometry;
        public Geometry getGeometry() {
            return geometry;
        }
        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }
    }

    public static class Geometry {
        private List<Double> coordinates;
        public List<Double> getCoordinates() {
            return coordinates;
        }
        public void setCoordinates(List<Double> coordinates) {
            this.coordinates = coordinates;
        }
    }
}
