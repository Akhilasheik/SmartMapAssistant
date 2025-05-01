package com.example.SmartMapAssistant.controller;


import com.example.SmartMapAssistant.model.RouteRequest;
import com.example.SmartMapAssistant.model.RouteResponse;
import com.example.SmartMapAssistant.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @PostMapping("/best")
    public RouteResponse getBestRoutes(@RequestBody RouteRequest request) {
        return routeService.fetchBestRoutes(request);
    }
}


