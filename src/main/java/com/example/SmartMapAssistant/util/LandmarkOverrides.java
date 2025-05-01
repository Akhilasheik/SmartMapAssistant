// LandmarkOverrides.java
package com.example.SmartMapAssistant.util;

import java.util.Map;

public class LandmarkOverrides {
    // Map of exact input strings → [lat, lon]
    public static final Map<String,double[]> OVERRIDES = Map.of(
            "Marathahalli Bridge, Bangalore", new double[]{12.958900, 77.695800},
            "Marathahalli Flyover, Bangalore", new double[]{12.958900, 77.695800},
            "KR Puram, Bangalore",             new double[]{12.992270, 77.709900}
            // add more as needed…
    );
}
